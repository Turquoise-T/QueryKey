# encoding=utf-8
import jieba
# jieba.enable_paddle()  # 启动paddle模式。 0.40版之后开始支持，早期版本不支持

# def data_clean(path):
#     train_data = read_file(path)
#     for line in train_data:
#         word_list = line.split('\t')
#         if word_list[0].find('ｗｗｗ' or 'http') != -1:
#             continue
#         elif word_list[0].isdigit():
#             continue
#         else:
#             line_string = "\t".join(word_list) + '\n'
#             print(line_string)


def read_file(path):
    """
    :param path: 文件的路径
    :return:
    """
    datalist = []
    with open(path, "r", encoding='utf-8') as f:
        for line in f.readlines():  # 读取文件
            # \t和\n全部可以作为替换的依据
            line = line.strip('\t\n')
            datalist.append(line)
    print(datalist)
    print("读文件完成")
    return datalist


def stop_word(str):
    """
    添加停用词表，只有不在这个表里面的才要被输出
    :param str: 传入的每个词
    :return: ok no
    """
    with open('../file/cn_stopwords.txt', 'r', encoding='utf-8') as f:

        for line in f.readlines():
            if str in line:
                return "no"
        return "ok"

def final_data():
    with open("../file/final.txt", 'a+', encoding='utf-8') as writers:
        with open('../file/doneData.txt', "r", encoding='utf-8') as f:
            for line in f.readlines():
                # 这个里面最后空格还占据一个位置
                if len(line) > 2 and stop_word(line) is 'ok':
                    writers.writelines(line)
    print("最终处理完成")


def process_data(datalist):
    """
    :param datalist: 处理之后的数据，需要继续进行分词的判断
    :return:数据清洗的时候将全部的字符都过滤掉，所以可以按照/来分类
    """
    with open('../file/doneData.txt', 'a+', encoding='utf-8') as writers:
        for data in datalist:
            atom = data.replace('/', '\n')
            writers.writelines(atom)
            writers.writelines("\n")
    print("处理数据完成")


def spilt_data(path):
    datalist = read_file(path)
    proData = []
    for data in datalist:
        seg_list = jieba.cut(data, use_paddle=True)
        pre_seg = '/'.join(list(seg_list))
        proData.append(pre_seg)
    print(proData)
    process_data(proData)
    print("分割数据完成")


if __name__ == '__main__':
    spilt_data("../file/cleanResult.txt")
    final_data()