import numpy as np
'''
重新生成样本数据的思路如下：
首先将原来的样本数据按属性个数分开
对于属性个数为3的2个数据集选择20个负载，分别生成相应的样本数据（对于每个负载来说有7个存储方案）
对于属性个数为62的数据集选择10个负载，生成相应的样本数据（选择小于等于6个的索引组合）

'''
from itertools import combinations

'''
筛选出合适的样本 索引数较少
'''
def  read_data():
    f = open("titan_new3.txt", "r")  # 设置文件对象
    line = f.readline()
    line = line[:-1]
    filename = 'titan_new4.txt'
    f1=open(filename, 'w')
    while line:
        temp=line.split(",")[0] #得到样本数据
        i0 = temp.split(" ")
        i0 = i0[:-1]
        n = int(float(i0[4])) #属性个数
        count=0
        for i in range(n):
            if float(i0[5+i])>0:
                count=count+1
        if count<10:
            f1.write(line+"\n")
        line = f.readline()  # 读取一行文件，包括换行符
        line = line[:-1]  # 去掉换行符，也可以不去

    f.close()
    f1.close()
'''
重写样本数据
'''
def rewrite():
    f = open("titan_new2.txt", "r")  # 设置文件对象
    line = f.readline()
    line = line[:-1]
    filename = 'titan_new3.txt'
    f1 = open(filename, 'w')
    while line:
        temp = line.split(",")[0]  # 得到样本数据
        label=line.split(",")[1]
        i0 = temp.split(" ")
        i0 = i0[:-1]
        n = int(float(i0[4]))  # 属性个数
        for i in range(n):
            if float(i0[5+i])>0:
                if float(i0[41+n+i]) ==0.0:
                    i0[5+i]="0.0"
        temp=""
        for i in range(len(i0)-1):
            temp=temp+i0[i]+" "
        temp=temp+i0[len(i0)-1]
        temp=temp+","+label
        print(line)
        print(temp)
        f1.write(temp + "\n")
        line = f.readline()  # 读取一行文件，包括换行符
        line = line[:-1]  # 去掉换行符，也可以不去

'''
为3个属性的生成样本数据
'''
def gen_freebase():
    f = open("neo4j_new.txt", "r")  # 设置文件对象
    line = f.readline()
    line = line[:-1]
    filename = 'newdata.txt'
    f1 = open(filename, 'a+')
    while line:
        temp = line.split(",")[0]  # 得到样本数据
        label = line.split(",")[1]
        i0 = temp.split(" ")
        i0 = i0[:-1]
        index0="0.0 0.0 0.0 "
        index1="1.0 0.0 0.0 "
        index2= "0.0 1.0 0.0 "
        index3 = "0.0 0.0 1.0 "
        index4 = "1.0 1.0 0.0 "
        index5 = "1.0 0.0 1.0 "
        index6 = "0.0 1.0 1.0 "
        index7 = "1.0 1.0 1.0 "
        list=[]
        list.append(index0)
        list.append(index1)
        list.append(index2)
        list.append(index3)
        list.append(index4)
        list.append(index5)
        list.append(index6)
        list.append(index7)
        n = int(float(i0[4]))  # 属性个数
        compare_index=""
        for i in range(n):
            compare_index=compare_index+i0[5+i]+" "
        s1 = "0.5 0.3 1.0 1814.0 "
        s2 = "4.0 3.1 1.0 2912.0 "
        head="1.0 0.0 0.0 0.0 3.0 "
        print("原样本")
        print(line)
        for i in range(8):
            if compare_index in list[i]:
                continue
            else:
                if s1 in line:
                    tail=line.split(s1)[1]
                    ve=head+list[i]+s1+tail.split(",")[0]

                    print(ve)
                    f1.write(ve + "\n")



                elif  s2 in line:
                    tail=line.split(s2)[1]
                    ve=head+list[i]+s2+tail.split(",")[0]
                    print(ve)
                    f1.write(ve + "\n")
        line = f.readline()  # 读取一行文件，包括换行符
        line = line[:-1]  # 去掉换行符，也可以不去
    f.close()
    f1.close()

'''
为62个属性的数据集生成测试数据，选择1-6个索引生成样本数据
根据负载中的属性出现的频率选择属性构成属性集合，然后在该集合中选择最多
为6个的属性设计存储方案，构造生成最终的样本数据

首先选择属性频率最高的k个属性，然后在k个属性中排列组合生成索引集合，
对于主索引：在生成的数据集中随机选择一定比例的方案加入主属性
样本空间大小：2500条
分层采样共30条
分层标准：是否含有主属性
k值取决于样本空间的大小

'''
def gen_ldbc():
    f = open("titan_new2.txt", "r")  # 设置文件对象
    line = f.readline()
    line = line[:-1]
    #filename = 'newdata.txt'
    #f1 = open(filename, 'a+')
    filename = 'test_titan.txt'
    f1 = open(filename, 'a+')
    filename2 = 'test2_titan.txt'
    f2 = open(filename2, 'a+')

    # 主属性所在的位置：12 22 30 34 39 44 50 56
    primary_set=set([12,22,30,34,39,44,50,56])
   # input_set.intersection(valid)


    while line:
        temp = line.split(",")[0]  # 得到样本数据
        i0 = temp.split(" ")
        i0 = i0[:-1]
        n = int(float(i0[4]))  # 属性个数
        #生成索引部分 生成规则 从属性频率中出现比较高的里面选择一部分
        dic=dict()
        for i in range(n):
            dic[i]=float(i0[41+n+i])
        d_order = sorted(dic.items(), key=lambda x: x[1], reverse=True)
        temp_list=[]
        count=0
        limit=min(15,len(d_order))
        for key in d_order:
            count=count+1
            temp_list.append(key[0])
            if count> limit:
                break
        d0 = list(combinations(temp_list, 1))
        d1 = list(combinations(temp_list, 2))
        d2 = list(combinations(temp_list, 3))
        d3 = list(combinations(temp_list, 4))
        d4 = list(combinations(temp_list, 5))
        d5 = list(combinations(temp_list, 6))
        d0.extend(d1)
        d0.extend(d2)
        d0.extend(d3)
        d0.extend(d4)
        d0.extend(d5)  #得到所有可能的索引组合

        head = "0.0 0.0 0.0 1.0 62.0 "
        s="0.184 15.0 8.0 15.0 "
        print("原样本")
        print(line)
        print("组合结果个数：")
        print(len(d0))
        #从d0中抽取150个样本，然后从中选择一半加入主索引
        temp_sample=RandomSample(d0, 150)


        for i in range(len(temp_sample)): #遍历所有可能的索引组合
            print("索引的坐标：")
            sample_index=temp_sample[i]

            tail = line.split(s)[1]
            #构造索引部分
            temp_set=set(d0[sample_index])
            if i%2==0: #偶数则加入主属性索引
                temp_set=temp_set.union(primary_set)
            new_index=""
            print(temp_set)

            for j in range(n):
                if j in temp_set:
                    new_index=new_index+"1.0 "
                else:
                    new_index=new_index+"0.0 "
            ve = head +  new_index+ s + tail.split(",")[0]
            print(ve)
            if i % 2 == 0:  # 偶数则加入主属性索引
                f2.write(ve+"\n")
            else:
                f1.write(ve + "\n")
        line = f.readline()  # 读取一行文件，包括换行符
        line = line[:-1]  # 去掉换行符，也可以不去
    f.close()
    f1.close()
    f2.close()



def RandomSample(current, k):
    sample = np.random.choice(range(len(current)),k,replace=False)
    #new_current = np.delete(current, sample)
    return sample
'''
随机抽取k个样本数据
'''
def choose_data():
    f = open("ldbc_titan.txt", "r")  # 设置文件对象
    line = f.readline()
    #line = line[:-1]
    # filename = 'newdata.txt'
    # f1 = open(filename, 'a+')
    filename = 'ldbc_titan_sample.txt'
    f1 = open(filename, 'a+')
    temp_list=[]
    while line:
        temp_list.append(line)
        line=f.readline()
    current = np.array(temp_list)
    #print(current)
    k=20
    sample=RandomSample(current, k)
    temp_sample=sample.reshape(20,1)

    for s in range(k):
        f1.write(current[(temp_sample[s][0])])
    f1.close()
    f.close



def titan_spe():
    f = open("titan_sample.txt", "r")  # 设置文件对象
    line = f.readline()
    temp_list1 = []
    temp_list2 = []
    temp_list3 = []
    temp_list4 = []
    temp_list5 = []
    temp_list6 = []
    temp_list7 = []
    temp_list8 = []
    count=0
    while line:
        count=count+1
        if line.startswith("0.0 0.0 0.0 1.0 3.0 0.0 0.0"):
            if "1814" in line:
                temp_list1.append(count)
            if "2912" in line:
                temp_list2.append(count)
        if line.startswith("0.0 0.0 0.0 1.0 3.0 0.0 1.0"):
            if  "1814" in line:
                temp_list3.append(count)
            if "2912" in line:
                temp_list4.append(count)
        if line.startswith("0.0 0.0 0.0 1.0 3.0 1.0 0.0"):
            if  "1814" in line:
                temp_list5.append(count)
            if "2912" in line:
                temp_list6.append(count)
        if line.startswith("0.0 0.0 0.0 1.0 3.0 1.0 1.0"):
            if "1814" in line:
                temp_list7.append(count)
            if "2912" in line:
                temp_list8.append(count)


        line = f.readline()
    print(temp_list1)
    print(temp_list2)
    print(temp_list3)
    print(temp_list4)
    print(temp_list5)
    print(temp_list6)
    print(temp_list7)
    print(temp_list8)

'''
数据库对比：
数据库不一样+索引不一样
'''
#def  neo4j_titan():



if __name__=="__main__":
    #read_data()
    #rewrite()
    #gen_freebase()
    #gen_ldbc()
    #titan_spe()

    choose_data()

