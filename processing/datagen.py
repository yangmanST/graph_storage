import numpy as np


'''
生成数据集
'''
def GenDataset():
    option = np.array([[0.5, 0.3, 1, 1814, 3],
              [4, 3.1, 1, 2912, 3],
              [0.184, 15, 8, 15, 62]])
    #option = option*1000000
    item = np.random.randint(0, len(option), 1)[0]
    return option[item]


'''
生成数据库类型
'''
def GenDatabase():
    option = np.array([[1, 0, 0, 0],
              [0, 1, 0, 0],
              [0, 0, 1, 0],
              [0, 0, 0, 1]])
    item = np.random.randint(0, len(option), 1)[0]
    return option[item]

'''
根据选择的数据集生成索引向量
'''
def GenIndex(n):
    BUDGTE = 50
    res = np.zeros(n)
    for i in range(0,n):
        item = np.random.randint(0,100,1)[0]%2
        res[i] = item
    flag = [1,13,23,31,36,41,47,53]
    if n == 62:
        for i in flag:
            res[i-1] = res[0]
    return res


'''
生成n个数，n个数的加和为100 
'''
def GenN(n):
    internal = np.random.randint(0, 100, n-1)
    internal.sort()
    res = np.zeros(n)
    res[0] = internal[0]
    for i in range(1,n-1):
        res[i] = internal[i] - internal[i-1]
    res[n-1] = 100 - internal[-1]
    '''
    # 测试加和是否为100
    count = 0
    for i in res:
        count = count + i
    '''
    return res


'''
    生成节点规模大小
    数据信息固定，负载可改（32），存储和索引可改

    输入数据：标签（非法）
    聚类和深度学习必要性
    数据库+属性个数+n索引 + 
    1 0 0 0 3 1 0 0 0.5 0.3 1 1814 1 … 99 20 20 60
'''
def GenVector():
    TYPE = 32
    res = GenDatabase() # 4
    dataset = GenDataset() # 5
    res = np.append(res, dataset[-1]) #倒数第一维决定了属性的个数
    n = dataset[-1].astype(int)
    index = GenIndex(n) #n维索引情况
    res = np.append(res, index)
    res = np.append(res, dataset[:-1])# 一维的标记位删去
    workloadtype = GenN(TYPE)
    res = np.append(res, workloadtype)
    attr = GenN(n)
    res = np.append(res, attr)
    #res = np.append(database, np.array(dataset[-1]), index, dataset[:-2],workloadtype, attr)
    #print(res)
    return res

#新生成的2个向量除了索引位之外其他都相同
#需要重新写生成索引的函数 62个属性的限制属性的个数
def GenVector_new():
    TYPE = 32
    res = GenDatabase() # 4
    dataset = GenDataset() # 5
    res = np.append(res, dataset[-1]) #倒数第一维决定了属性的个数
    n = dataset[-1].astype(int)
    index = GenIndex(n) #n维索引情况
    res = np.append(res, index)
    res = np.append(res, dataset[:-1])# 一维的标记位删去
    workloadtype = GenN(TYPE)
    res = np.append(res, workloadtype)
    attr = GenN(n)
    res = np.append(res, attr)
    #res = np.append(database, np.array(dataset[-1]), index, dataset[:-2],workloadtype, attr)
    #print(res)
    return res
'''
统一向量
'''
def GenUniVector():
    TYPE = 32
    res = GenDatabase()  # 存储维度的特征一致
    dataset = GenDataset()  # 5
    res = np.append(res, dataset)  # 倒数第一维决定了属性的个数
    n = dataset[-1].astype(int)
    index = GenIndex(n)  # n维索引情况
    workloadtype = GenN(TYPE)
    res = np.append(res, workloadtype)
    attr = GenN(n)
    mean = 100/n
    flag = np.zeros(n)
    for i in range(0,n):
        #访问次数较多的属性
        if attr[i] > mean :
            flag[i] = 1
        else:
            flag[i] = -1

    indexscore = index*flag
    indexscore = np.dot(indexscore, attr)

    res = np.append(res, indexscore)
    # res = np.append(database, np.array(dataset[-1]), index, dataset[:-2],workloadtype, attr)
    # print(res)
    return res

'''
n 数据条数
'''

def GenSyn(n):
    #指定随机种子
    np.random.seed(1)
    res = np.ones([n, 400])*-1
    labels = np.zeros(n)
    for i in range(0, n):
        item1 = GenVector()
        item2 = GenVector()
        res[i][0:len(item1)] = item1
        res[i][200:200+len(item2)]=item2
    return res, labels

if __name__=="__main__":
    # 指定随机种子
    np.random.seed(1)
    # 生成数据条数
    N = [10000]
    k = 100
    for i in range(0, len(N)):
        n = N[i]
        # res = {}
        filename = str(i + 1) + ".txt"
        f = open(filename, 'w')
        sample = np.random.choice(n, k, replace=False)
        for j in range(0, n):
            item = GenVector()
            if j in sample:
                for v in item:
                    print(v, end='', file=f)
                    print(' ', end='', file=f)
                print('\n', end='', file=f)
            # res[i] = item
    print(1)
