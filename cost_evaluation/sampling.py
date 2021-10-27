# coding=gbk
import numpy as np
import random

'''
随机抽样策略
从现有数据中抽取k个样本,返回样本索引
'''
def RandomSample(current, k):
    sample = np.random.choice(range(len(current)), k, replace=False)
    # new_current = np.delete(current, sample)
    # return sample, new_current
    return sample




'''
有偏抽样策略
以概率proba列表进行抽样
'''
def BiasSample(proba, k):
    res = np.zeros(k)
    count = 0
    while count < k:
        for i in range(len(proba)):
            flag = random.uniform(0, 1)
            if flag > proba[i]:
                res[count] = i
                count += 1

    return res

def choose_ldbc_data():
    #f = open("all_sample.txt", "r")  # 设置文件对象
    f = open("test_sample.txt", "r")  # 设置文件对象
    line = f.readline()

    temp = []
    temp1 = []
    while line:
        temp.append(line)
        line = f.readline()
    #f = open("all_label.txt", "r")  # 设置文件对象
    f = open("test_label.txt", "r")  # 设置文件对象
    line = f.readline()
    while line:
        temp1.append(line)
        line = f.readline()

    temp_s = []
    print(len(temp1))
    #dataset_control = "0.5 0.3 1.0 1814.0"
    #dataset_control = "4.0 3.1 1.0 2912.0"
    dataset_control = "0.184 15.0 8.0 15.0"
    #database_control="1.0 0.0 0.0 0.0 62.0"
    #database_control="0.0 0.0 0.0 1.0 62.0"
    fname=["f1","f2","f3","f4","f5","f6"]
    lname=["l1","l2","l3","l4","l5","l6"]
    fw_sample = open("f1", 'w')
    fw_sample1 = open("l1", 'w')
    count=0
    for i in range(len(temp)):#遍历所有的样本数据
        if dataset_control in temp[i] :
            fw_sample.write(temp[i])
            fw_sample1.write(temp1[i])
            count=count+1
    fw_sample.close()
    fw_sample1.close()
    return count

'''
读取文件样本，将其分为6类，分别是ldbc+2个数据库和freebase+2个数据库
'''
def classify_data():
    f = open("all_sample.txt", "r")  # 设置文件对象
    line = f.readline()

    temp = []
    temp1 = []
    while line:
        temp.append(line)
        line = f.readline()
    f = open("all_label.txt", "r")  # 设置文件对象
    line = f.readline()
    while line:
        temp1.append(line)
        line = f.readline()

    temp_s = []
    print(len(temp1))
    #dataset_control = "0.5 0.3 1.0 1814.0"
    #dataset_control = "4.0 3.1 1.0 2912.0"
    dataset_control = "0.184 15.0 8.0 15.0"
    #database_control="1.0 0.0 0.0 0.0 62.0"
    database_control="0.0 0.0 0.0 1.0 62.0"
    fname=["f1","f2","f3","f4","f5","f6"]
    lname=["l1","l2","l3","l4","l5","l6"]
    fw_sample = open("f", 'w')
    fw_sample1 = open("l", 'w')
    count=0
    for i in range(len(temp)):#遍历所有的样本数据
        if dataset_control in temp[i] and database_control in temp[i]:
            fw_sample.write(temp[i])
            fw_sample1.write(temp1[i])
            count=count+1
    fw_sample.close()
    fw_sample1.close()
    return count

