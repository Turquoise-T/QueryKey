import multiprocessing
import time
import threading

filePath = ''


def split_data(thread_num):
    f = open(filePath, encoding='utf-8')
    text = []
    for line in f:
        text.append(line.strip())
    length = len(text)
    group_size = length / thread_num
    data = []
    if thread_num == 0:
        data.append(text)
    else:
        for i in range(1, thread_num):
            from_index = (i - 1) * thread_num
            to_index = i * thread_num
            if i == thread_num:
                to_index = i * thread_num + length % thread_num
            data.append(text[from_index:to_index])
    return data

