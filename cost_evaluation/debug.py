# coding=gbk
from itertools import combinations
def debug_read_sample():
    list = []
    list.append("ldbc_neo4j.txt")
    list.append("ldbc_pri_neo4j.txt")
    list.append("ldbc_pri_titan.txt")
    list.append("ldbc_titan.txt")
    list.append("newdata_neo4j.txt")
    list.append("newdata_titan.txt")
    data_list = []
    for i in range(len(list)):
        f = open(list[i], "r")  # �����ļ�����
        line = f.readline()
        line = line[:-1]
        while line:
            data_list.append(line)
            line = f.readline()
            line = line[:-1]
    return data_list
def debug_count():
    data=debug_read_sample()
    freebase1 = []  # 3�����ݼ�
    freebase2 = []
    ldbc = []
    for line in data:

        # ��ȡ����������ȡ���ݼ���Ϣ��Ϊkey
        '''
        np.array([[0.5, 0.3, 1, 1814, 3],
                  [4, 3.1, 1, 2912, 3],
                  [0.184, 15, 8, 15, 62]])
        '''
        # 3�����ݿ������
        s1 = "0.5 0.3 1.0 1814.0"
        s2 = "4.0 3.1 1.0 2912.0"
        s3 = "0.184 15.0 8.0 15.0"

        if s1 in line:
            freebase1.append(line)
        elif s2 in line:
            freebase2.append(line)
        elif s3 in line:
            ldbc.append(line)
    d1 = list(combinations(freebase1, 2))
    d2 = list(combinations(freebase2, 2))
    d3 = list(combinations(ldbc, 2))
    d1.extend(d2)
    d1.extend(d3)
    c = len(d1)
    input_x = np.ones([c, 400]) * -1  # ����ģ�͵�����
    index = 0
    for d in d1:  # ���е������� ��Ҫɸѡ��������ͬ������
        item0 = d[0].split(",")[0]
        item1 = d[1].split(",")[0]
        i0 = item0.split(" ")
        i1 = item1.split(" ")
        i0 = i0[:-1]
        i1 = i1[:-1]
        compare_index = int(float(i0[4]))
        compare_flag = 1
        for i in range(32):
            if i0[9 + compare_index + i] != i1[9 + compare_index + i]:
                compare_flag = 0
                # print("����ѭ��")
                break
        if compare_flag != 0:
            input_x[index][0:len(i0)] = [float(i) for i in i0]
            input_x[index][200:200 + len(i1)] = [float(i) for i in i1]
            index = index + 1
    print("����")
    print(index)
