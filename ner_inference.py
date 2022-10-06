import json
import os

import numpy as np
import torch
from torch.utils.data import SequentialSampler, DataLoader, DistributedSampler
from transformers import AutoTokenizer, AutoModelForTokenClassification, pipeline, BertTokenizer
import torch.nn.functional as F

# from knlp.common.constant import KNLP_PATH
# from knlp.seq_labeling.NER.bert.run_ner_softmax import load_and_cache_examples
# from knlp.seq_labeling.NER.trie_seg.ner_util import FinetuneTrie
from ner_seq import CluenerProcessor as processors, logger, collate_fn, InputFeatures
from bert_for_ner import BertSoftmaxForNer
# from knlp.utils.get_entity import get_entities
from tokenization import BasicTokenizer, FullTokenizer

BERT_MODEL_PATH = "output_modelbert/checkpoint-448"

texts = [
    ('1945年', 1, None),
    ('8月', 1, None),
    ('爱琴海', 1, 'sce')
]


class NER_inference:
    def __init__(self, task, model_name=BERT_MODEL_PATH, log=True):
        self.task = task
        self.model = model_name
        self.tag = []
        self.token = []
        self.log = log
        # self.tokenizer

    def run(self, input):
        processor = processors()
        label_list = processor.get_labels()
        id2label = {i: label for i, label in enumerate(label_list)}
        model = AutoModelForTokenClassification.from_pretrained(self.model)
        tokenizer = BertTokenizer.from_pretrained(self.model)
        classifier = pipeline("ner", model=model, tokenizer=tokenizer)
        result = classifier(input)
        token_out = []
        label_out = []
        for dictionary in result:
            token_out.append(dictionary['word'])
            label_raw = dictionary['entity']
            label_id = label_raw.strip('LABEL_')
            label_out.append(id2label[int(label_id)])
        if self.log:
            print(token_out)
            print(label_out)

        ner_res = self.cut(token_out, label_out)

        self.tag = label_out
        self.token = token_out

        return ner_res

    def cut(self, sentence1, sentence2):
        """
        按照BIO标签做中文分词，切割句子。
        Args:
            sentence1: 文本序列
            sentence2: 标注序列

        Returns:

        """
        out_sent = []
        begin = 0
        for idx in range(len(sentence1)):
            # print(sentence1[idx], sentence2[idx])
            if sentence2[idx][0] == 'B':
                begin = idx
            elif sentence2[idx][0] == 'I':
                idx += 1
                if idx == len(sentence1):
                    str = "".join(sentence1[begin:idx])
                    out_sent.append(str)
                    return out_sent
                elif sentence2[idx][0] == 'O' or sentence2[idx][0] == 'B':
                    str = "".join(sentence1[begin:idx])
                    out_sent.append(str)
                    begin = idx
            elif sentence2[idx][0] == 'O':
                out_sent.append(sentence1[idx])

        return out_sent

    def tag_predict(self):
        return self.tag

    def token_predict(self):
        return self.token

    def predict(self,text, model, mask_padding_with_zero=True, max_seq_length=256, sep_token="[SEP]",
                sequence_a_segment_id=0, cls_token="[CLS]", cls_token_segment_id=1, pad_token=0,
                pad_token_segment_id=0):
        features = []
        basicTokenizer = BasicTokenizer(do_lower_case=True)
        full = FullTokenizer(vocab_file='output_modelbert/vocab.txt', do_lower_case=True)
        input_tokens = basicTokenizer.tokenize(text)

        special_tokens_count = 2
        if len(input_tokens) > max_seq_length - special_tokens_count:
            input_tokens = input_tokens[: (max_seq_length - special_tokens_count)]
        input_tokens += [sep_token]
        segment_ids = [sequence_a_segment_id] * len(input_tokens)
        input_tokens = [cls_token] + input_tokens
        segment_ids = [cls_token_segment_id] + segment_ids

        input_ids = full.convert_tokens_to_ids(tokens=input_tokens)
        input_mask = [1 if mask_padding_with_zero else 0] * len(input_ids)

        padding_length = max_seq_length - len(input_ids)

        input_ids += [pad_token] * padding_length
        input_mask += [0 if mask_padding_with_zero else 1] * padding_length
        segment_ids += [pad_token_segment_id] * padding_length
        input_len = len(input_ids)

        assert len(input_ids) == max_seq_length
        assert len(input_mask) == max_seq_length
        assert len(segment_ids) == max_seq_length
        features.append(InputFeatures(input_ids=input_ids, input_mask=input_mask, input_len=input_len,
                                      segment_ids=segment_ids, label_ids=None))

        all_input_ids = torch.tensor([f.input_ids for f in features], dtype=torch.long)
        all_input_mask = torch.tensor([f.input_mask for f in features], dtype=torch.long)

        inputs = {"input_ids": all_input_ids, "attention_mask": all_input_mask, "labels": None}

        output = model(**inputs)
        # print(output)
        logits = output[0]
        preds = logits.detach().cpu().numpy()
        preds = np.argmax(preds, axis=2).tolist()
        preds = preds[0][1:-1]

        processor = processors()
        label_list = processor.get_labels()
        id2label = {i: label for i, label in enumerate(label_list)}
        tags = [id2label[x] for x in preds]

        sentence = input_tokens[1:-1]
        tag_list = tags[:len(input_tokens) - 2]
        print(tag_list)
        result = self.cut(sentence, tag_list)

        self.tag = tag_list
        self.token = sentence

        return result
        # print(tags[:len(input_tokens) - 2])
        # print(input_tokens[1:-1])
        # print(input_ids)
        # print(input_mask)


if __name__ == '__main__':
    inference = NER_inference('cluener', BERT_MODEL_PATH)
    # trieTree = FinetuneTrie()
    # 1945年8月斯坦福大学计算机学院阿克琉斯，想去雅典和爱琴海。中国与欧盟海军爱玩dota
    to_be_pred = '我爱看微微一笑很倾城'
    model = BertSoftmaxForNer.from_pretrained('output_modelbert/checkpoint-448')
    model.to('cpu')
    result = inference.predict(to_be_pred, model)
    print(result)

    # trieTree.construct_text_dict([s for s in to_be_pred], inference.tag_predict())
    # for word_tuples in texts:
    #     trieTree.insert(word_tuples[0], word_tuples[1], word_tuples[2])
    # print(trieTree.trie)
    # print(trieTree.find_all_prefix('英国馆'))
    # print(trieTree.knlp_seg(to_be_pred)[0])
    # print(trieTree.knlp_seg(to_be_pred)[1])
