import time
import threading
import jieba
import random
# 多线程部分
data_path = '../file/cleanResult.txt'

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
            else:
                return "ok"


def final_data():
    with open("../file/final.txt", 'a+', encoding='utf-8') as writers:
        with open('../file/doneData.txt', "r", encoding='utf-8') as f:
            for line in f.readlines():
                # 这个里面最后空格还占据一个位置
                if len(line) > 2 and stop_word(line) is 'ok':
                    writers.writelines(line)
                    writers.writelines("\n")
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


# 读取文件
class DataSource(object):

    def __init__(self, file_name, start_line=0, max_count=None):
        self.file_name = file_name
        self.start_line = start_line  # 第一行行号为1，按行来读取的话，对于大文件的分配处理非常有效
        self.line_index = start_line  # 当前读取位置
        self.max_count = max_count  # 读取最大行数
        self.lock = threading.RLock()  # 同步锁

        self.__data__ = open(self.file_name, 'r', encoding='utf-8')
        for _ in range(self.start_line):
            self.__data__.readline()

    def get_line(self):
        self.lock.acquire()
        try:
            if self.max_count is None or self.line_index < (self.start_line + self.max_count):
                line = self.__data__.readline()
                if line:
                    self.line_index += 1
                    return True, line
                else:
                    return False, None
            else:
                return False, None
        except Exception as e:
            return False, "error:" + e.args
        finally:
            self.lock.release()

    def __del__(self):
        if not self.__data__.closed:
            self.__data__.close()
            print("关闭读取文件：" + self.file_name)


# 业务逻辑处理
def process(worker_id, data_source):
    count = 0
    while True:
        status, data = data_source.get_line()
        if status:
            print(f'线程{worker_id}获取数据，正在处理...')
            # 真正的逻辑代码处理内容，处理一行代码
            spilt_data("../file/cleanResult.txt")
            final_data()

            print(f'线程{worker_id}数据处理完毕')
            count += 1
        else:
            break  # 退出循环
    print(f"线程{worker_id}结束，共处理{count}条数据！!!")


if __name__ == '__main__':
    data_source = DataSource(data_path)
    worker_count = 10  # 开启10个线程，注意：并不是线程越多越好

    workers = []
    for i in range(worker_count):
        worker = threading.Thread(target=process, args=(i + 1, data_source))
        worker.start()
        workers.append(worker)

    for worker in workers:
        worker.join()
    print("总程序执行完毕！！！")