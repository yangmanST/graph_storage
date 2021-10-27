# coding=gbk
import time
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split, StratifiedKFold
import matplotlib.pyplot as plt
from cost_evaluation.dcnn import new_model2
from cost_evaluation.gru import GetModel
from cost_evaluation.sampling import RandomSample
from cost_evaluation.scnn import new_model
tf.compat.v1.disable_eager_execution()
import random
from itertools import combinations
from tensorflow.keras.callbacks import ModelCheckpoint





'''
节点个数 边个数 节点种类 边种类 属性种类 后3个可以进行log处理
前2个信息
option = np.array([[0.5, 0.3, 1, 1814, 3],
              [4, 3.1, 1, 2912, 3],
              [0.184, 15, 8, 15, 62]])
对数据集信息进行预处理
'''
def Embbed1(input):
    output = np.empty([input.shape[0], 5], dtype=float)
    output[:, 0] = np.round(input[:, 0])
    output[:, 1] = np.round(input[:, 1])
    # 属性个数进行log处理
    output[:, 2] = len(input[:, 2].astype(np.int).astype(np.str))
    output[:, 3] = len(input[:, 3].astype(np.int).astype(np.str))
    return output


'''
处理数据库信息，输出2个结果
1 0
或0 1
表示neo4j或titan
'''
def Embbed2(input):
    output = np.empty([input.shape[0], 2], dtype=float)
    output[:, 0] = input[:, 0]
    output[:, 1] = input[:, 3]
    return output


'''
合并嵌入层处理结果 得到第2层的输入
'''
def layer2(input1, input2, input3, input4):
    temp = np.append(input1, input2, axis=1)
    temp1 = np.append(temp, input3, axis=1)
    output = np.append(temp1, input4, axis=1)
    return output


def initConv(shape, dtype=None):
    res = np.zeros(shape)
    res[0], res[-1] = 1, 1
    return tf.constant_initializer(res)

'''
处理索引和属性信息
用CNN模型处理
'''
def Embbed3(n):
    # input = tf.keras.layers.Masking(mask_value=-1, input_shape=[n, 400])
    filter_sizes = 3

    # apply a convolution 1d of length 3 to a sequence with 10 timesteps, with 64 output filters
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Masking(mask_value=-1, input_shape=[None, 130]))
    model.add(tf.keras.layers.Convolution1D(32, 3, padding='same', input_shape=(None, 130)))
    model.summary()
    return model


'''
读取打标签的数据 
组合生成模型的输入
 返回结果为n*400 矩阵 和每个样本的标签
'''
def read_data(filenames):
    freebase1 = []  # 3个数据集
    freebase2 = []
    ldbc = []
    for filename in filenames:
        f = open(filename, "r")  # 设置文件对象
        # 读取样本数据提取数据集信息作为key
        '''
        np.array([[0.5, 0.3, 1, 1814, 3],
                  [4, 3.1, 1, 2912, 3],
                  [0.184, 15, 8, 15, 62]])
        '''
        # 3个数据库的特征
        s1 = "0.5 0.3 1.0 1814.0"
        s2 = "4.0 3.1 1.0 2912.0"
        s3 = "0.184 15.0 8.0 15.0"

        line = f.readline()
        line = line[:-1]
        while line:  # 直到读取完文件
            if s1 in line:
                freebase1.append(line)
            elif s2 in line:
                freebase2.append(line)
            elif s3 in line:
                ldbc.append(line)
            line = f.readline()  # 读取一行文件，包括换行符
            line = line[:-1]  # 去掉换行符，也可以不去
        f.close()  # 关闭文件

    d1 = list(combinations(freebase1, 2))
    d2 = list(combinations(freebase2, 2))
    d3 = list(combinations(ldbc, 2))
    d1.extend(d2)
    d1.extend(d3)
    c = len(d1)
    input_x = np.ones([c, 400]) * -1  # 定义模型的输入
    labels = np.zeros(c)
    index = 0
    for d in d1:  # 所有的组合情况 需要筛选出负载相同的向量
        item0 = d[0].split(",")[0]
        label0 = np.long(d[0].split(",")[1])
        item1 = d[1].split(",")[0]
        label1 = np.long(d[1].split(",")[1])
        i0 = item0.split(" ")
        i1 = item1.split(" ")
        i0 = i0[:-1]
        i1 = i1[:-1]
        compare_index = int(float(i0[4]))
        compare_flag = 1
        for i in range(32):
            if i0[9 + compare_index + i] != i1[9 + compare_index + i]:
                compare_flag = 0
                # print("跳出循环")
                break
        if compare_flag != 0:
            input_x[index][0:len(i0)] = [float(i) for i in i0]
            input_x[index][200:200 + len(i1)] = [float(i) for i in i1]
            if label0 < label1:
                labels[index] = 1
            index = index + 1
    print("初步")
    print(index)
    return input_x[0:index - 1, :], labels[0:index - 1]

'''
对输入向量进行预处理，得到送入神经网络的输入
'''
def rewrite_input(x, y):
    np.random.seed(1)
    # 向量构成：
    # 数据库（4）+属性个数n+n位的索引信息+4个数据库信息+32个负载+每个属性频率
    # 添加嵌入层
    # print(x[0])
    #数据库信息
    input1 = x[:, 0:4]
    output1 = Embbed2(input1)  # 数据库信息

    input2 = np.empty([len(x), 4], dtype='float')  # 数据集信息
    input3 = np.ones([len(x), 130]) * -1  # 存储索引和负载信息

    #32个负载的频率
    output4 = np.empty([len(x), 32], dtype='float')
    print("总个数"+str(len(x)))
    for i in range(len(x)):
        n = int(x[i][4])  # 属性的个数
        # print(n)
        index = n + 5  # 数据集信息所在的索引下标
        # print("数据集索引"+str(index))
        input2[i][0:3] = x[i][index:index + 3]
        input3[i][0:n - 1] = x[i][5:4 + n]
        input3[i][n:2 * n - 1] = x[i][42 + n:41 + 2 * n]
        output4[i][0:31] = x[i][10 + n:41 + n]
    output2=Embbed1(input2) #数据集信息
    emdded_model=Embbed3(len(x)) # 索引和属性信息经过CNN后的输出
    input3=np.expand_dims(input3, axis=0)
    output3=emdded_model(input3)
    print(output3.shape)

    #in2 = layer2(output1, output2, output3[0], output4)  # 经过嵌入层处理得到的模型输入
    #in2 = layer2(output1, output2, input3, output4)  # 经过嵌入层处理得到的模型输入
    in2 = layer2(output1, input2, input3[0], output4)  # 经过嵌入层处理得到的模型输入

    ################第二个样本数据
    input1_1 = x[:, 200:204]
    output1_1 = Embbed2(input1_1)  # 数据库信息
    input2_1 = np.empty([len(x), 4], dtype='float')
    input3_1 = np.ones([len(x), 130]) * -1
    output4_1 = np.empty([len(x), 32], dtype='float')

    for i in range(len(x)):
        n = int(x[i][204])
        index = n + 5
        input2_1[i][0:3] = x[i][200 + index:index + 203]
        input3_1[i][0:n - 1] = x[i][205:204 + n]
        input3_1[i][n:2 * n - 1] = x[i][242 + n:241 + 2 * n]
        output4_1[i][0:31] = x[i][210 + n:241 + n]

    output2_1 = Embbed1(input2_1)  # 数据集信息
    emdded_model_1 = Embbed3(len(x))  # 索引和属性信息经过CNN后的输出
    input3_1 = np.expand_dims(input3_1, axis=0)
    output3_1 = emdded_model_1(input3_1)
    #in2_1 = layer2(output1_1, output2_1, output3_1[0], output4_1)  # 经过嵌入层处理得到的模型输入
    #in2_1 = layer2(output1_1, output2_1, input3_1, output4_1)  # 经过嵌入层处理得到的模型输入
    in2_1 = layer2(output1_1, input2_1, input3_1[0], output4_1)  # 经过嵌入层处理得到的模型输入

    ##########得到嵌入层输出结果
    new_x = np.ones([len(x), 400]) * -1
    s1 = in2.shape[1] - 1
    s2 = in2_1.shape[1] - 1
    for i in range(len(x)):
        new_x[i][0:s1] = in2[i][0:s1]
        new_x[i][200:200 + s2] = in2_1[i][0:s2]
    #new_x = np.expand_dims(new_x, axis=1)
    #y = np.expand_dims(y, axis=1)
    return new_x, y


'''
测试样本数据得到标签
'''
# def test_():
def rewrite():
    list = []
    list.append("1r_ldbc_neo4j.txt")
    list.append("1r_ldbc_pri_neo4j.txt")
    list.append("1r_ldbc_pri_titan.txt")
    list.append("1r_ldbc_titan.txt")

    for i in range(4):
        filename = "2r_" + list[i]
        fw1 = open(filename, 'w')
        f1 = open(list[i], "r")  # 设置文件对象
        line = f1.readline()
        line1 = line[:-1]
        while line:
            line = line[:-1] + " " + '\n'
            fw1.write(line)
            line = f1.readline()
            line1 = line[:-1]
        f1.close()
        fw1.close()

'''
从样本集中筛选已经打过标签的数据，
将剩余样本写入文件
'''
def write_sample():
    list = []
    list.append("1r_ldbc_neo4j.txt")
    list.append("1r_ldbc_pri_neo4j.txt")
    list.append("1r_ldbc_pri_titan.txt")
    list.append("1r_ldbc_titan.txt")
    list.append("1r_newdata_neo4j.txt")
    list.append("1r_newdata_titan.txt")
    # 首先读取所有打过标签的数据
    f = open("sample.txt", "r")  # 设置文件对象
    line = f.readline()
    line = line[:-1]
    list_temp = []
    while line:  # 直到读取完文件
        #tline = line.split(",")[0]
        # print()
        print(line+"rrrrr")
        list_temp.append(line)
        line = f.readline()  # 读取一行文件，包括换行符
        line = line[:-1]  # 去掉换行符，也可以不去
    f.close()  # 关闭文件
    print("读取结束")
    for i in range(6):
        print(i)
        f1 = open(list[i], "r")  # 设置文件对象
        line = f1.readline()
        line1 = line[:-2]

        filename = "2r_" + list[i]
        fw1 = open(filename, 'w')
        debug = 0
        while line:
            debug = debug + 1
            if debug % 100 == 0:
                print(debug)
                print(line1+"rrrrr")
            if line1 in list_temp:
                line = f1.readline()
                line1 = line[:-2]
                continue
            else:
                fw1.write(line)
            line = f1.readline()
            line1 = line[:-2]
        fw1.close()
def final_test():
    total_count = []
    total_count.append(600)
    total_count.append(900)
    total_count.append(1200)
    total_count.append(1427)
    #
    # 3.0 0.0 1.0 1.0 0.5 0.3 1.0 1814.0
    # 62.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
    dataset_control = "0.5 0.3 1.0 1814.0"
    dataset_control2 = "4.0 3.1 1.0 2912.0"
    dataset_control3 = "0.184 15.0 8.0 15.0"

    for train_i in range(len(total_count)):
        #np.random.seed(1)
        x, y = read_total_sample("total_sample", "total_label", total_count[train_i], dataset_control)
        read_total_sample("total_sample", "total_label", total_count[train_i], dataset_control2)
        read_total_sample("total_sample", "total_label", total_count[train_i], dataset_control3)
'''
3个模型训练过程
'''
def first_train():
    filename = 'log.txt'
    f1 = open(filename, 'a+')
    total_count=[]
    total_count.append(600)
    total_count.append(900)
    total_count.append(1200)
    total_count.append(1427)
    total_accuracy1=[]
    total_accuracy2 = []
    total_accuracy3 = []
    total_loss1 = []
    total_loss2 = []
    total_loss3 = []
    debug_count=0
    # 3.0 0.0 1.0 1.0 4.0 3.1 1.0 2912.0
    # 3.0 0.0 1.0 1.0 0.5 0.3 1.0 1814.0
    # 62.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
    #dataset_control="3.0 0.0 1.0 1.0 0.5 0.3 1.0 1814.0"
    #dataset_control = "0.5 0.3 1.0 1814.0"
    #dataset_control = "4.0 3.1 1.0 2912.0"
    #dataset_control = "0.184 15.0 8.0 15.0"
    dataset_control=" "
    while debug_count<1:
        debug_count=debug_count+1

        for train_i in range(len(total_count)):
            np.random.seed(1)
            x,y=read_total_sample("total_sample","total_label",total_count[train_i],dataset_control)
            new_x,y=rewrite_input(x,y)

            model1=new_model()
            new_x1 = np.expand_dims(new_x, axis=2)
            y1 = np.expand_dims(y, axis=1)
            opt=tf.optimizers.Adam(lr=0.01)
            model1.compile(loss=tf.losses.binary_crossentropy,optimizer=opt,metrics=["accuracy"])
            X_train,Y_train=new_x1,y1
            hist = model1.fit(X_train, Y_train, epochs=100, batch_size=64, )
            checkpoint_filepath = str(debug_count)+str(1)+str(train_i) + "weights.best.hdf5"
            checkpoint1 = ModelCheckpoint(checkpoint_filepath, monitor='accuracy', verbose=1, save_best_only=True,
                                         mode='max')
            callbacks_list = [checkpoint1]
            model1.fit(X_train, Y_train, epochs=100, batch_size=64, callbacks=callbacks_list, verbose=0)


            #method1_acc,loss1=k_test(X_train, Y_train, model1)
            test_acc_=retrain1(checkpoint_filepath,1,dataset_control)
            alltest_acc_ ,time= total_train(checkpoint_filepath, 1, dataset_control)

            #total_accuracy1.append(method1_acc)
            #total_loss1.append(loss1)
            f1.write("模型1("+str(total_count[train_i])+"):\n")
            f1.write("交叉验证：" + "\n")
            #for temp_acc in method1_acc:
             #   f1.write(str(temp_acc)+" ")
            f1.write("\n")
            #for temp_acc in loss1:
             #   f1.write(str(temp_acc)+" ")
            f1.write("\n")
            f1.write("测试集结果：" +str(test_acc_)+"\n")
            f1.write("总空间测试集结果：" +str(alltest_acc_)+"\n")
            f1.write("分类测试样本时间：" + str(time) + "\n")




            model2 = new_model2()
            new_x2 = np.expand_dims(new_x, axis=2)
            y2 = np.expand_dims(y, axis=1)
            opt = tf.optimizers.Adam(lr=0.01)
            model2.compile(loss=tf.losses.binary_crossentropy, optimizer=opt, metrics=["accuracy"])
            X_train, Y_train = new_x2, y2
            hist = model2.fit(X_train, Y_train, epochs=100, batch_size=64, )
            checkpoint_filepath = str(2) + str(train_i) + "weights.best.hdf5"
            checkpoint2 = ModelCheckpoint(checkpoint_filepath, monitor='accuracy', verbose=1, save_best_only=True,
                                         mode='max')
            callbacks_list = [checkpoint2]
            model2.fit(X_train, Y_train, epochs=100, batch_size=64, callbacks=callbacks_list, verbose=0)
            #method2_acc,loss2 = k_test(X_train, Y_train, model2)

            test_acc_=retrain1(checkpoint_filepath,2,dataset_control)
            alltest_acc_ ,time= total_train(checkpoint_filepath, 2, dataset_control)
            #total_accuracy2.append(method2_acc)
            #total_loss2.append(loss2)
            f1.write("模型2(" + str(total_count[train_i]) + "):\n")
            f1.write("交叉验证：" + "\n")
            #for temp_acc in method2_acc:
             #   f1.write(str(temp_acc) + " ")
            f1.write("\n")
            #for temp_acc in loss2:
             #   f1.write(str(temp_acc)+" ")
            f1.write("\n")
            f1.write("测试集结果：" + str(test_acc_) + "\n")
            f1.write("总样本测试集结果：" + str(alltest_acc_) + "\n")
            f1.write("分类测试样本时间：" + str(time) + "\n")

            model3 = GetModel()  # 得到训练模型
            new_x3 = np.expand_dims(new_x, axis=1)
            y3 = np.expand_dims(y, axis=1)
            opt = tf.optimizers.Adam(lr=0.01)
            model3.compile(loss=tf.losses.binary_crossentropy, optimizer=opt, metrics=["accuracy"])
            X_train, Y_train = new_x3, y3
            hist = model3.fit(X_train, Y_train, epochs=100, batch_size=64, )
            checkpoint_filepath = str(3) + str(train_i) + "weights.best.hdf5"
            checkpoint3 = ModelCheckpoint(checkpoint_filepath, monitor='accuracy', verbose=1, save_best_only=True,
                                         mode='max')
            callbacks_list = [checkpoint3]
            model3.fit(X_train, Y_train, epochs=100, batch_size=64, callbacks=callbacks_list, verbose=0)
            #method3_acc,loss3 = k_test(X_train, Y_train, model3)
            test_acc_=retrain1(checkpoint_filepath,3,dataset_control)
            alltest_acc_,time = total_train(checkpoint_filepath, 3, dataset_control)

            #total_accuracy3.append(method3_acc)
            #total_loss3.append(loss3)
            f1.write("模型3(" + str(total_count[train_i]) + "):\n")
            f1.write("交叉验证：" + "\n")
            #for temp_acc in method3_acc:
             #   f1.write(str(temp_acc) + " ")
            f1.write("\n")
            #for temp_acc in loss3:
             #   f1.write(str(temp_acc)+" ")
            f1.write("\n")
            f1.write("测试集结果：" + str(test_acc_) + "\n")
            f1.write("总样本测试集结果：" + str(alltest_acc_) + "\n")
            f1.write("分类测试样本时间：" + str(time) + "\n")
'''
        x_axix = [x for x in range(1, 11)]  # 开始画图
        plt.title('dataset size='+str(total_count[train_i]))
        plt.plot(x_axix, method1_acc, color='green', label='model1 accuracy')
        plt.plot(x_axix, method2_acc, color='skyblue', label='model2 accuracy')
        plt.plot(x_axix, method3_acc, color='blue', label='model3 accuracy')
        plt.legend()  # 显示图例
        plt.xlabel('iteration times')
        plt.ylabel('accuracy')
        plt.show()
    draw(total_accuracy1,1)
    draw(total_accuracy2, 2)
    draw(total_accuracy3, 3)
    draw_loss(total_loss1, 1)
    draw_loss(total_loss2, 2)
    draw_loss(total_loss3, 3)
'''


'''
k折交叉验证
'''
def k_test(X,Y,model):
    seed = 7
    np.random.seed(seed)
    kfold = StratifiedKFold(n_splits=10, shuffle=True, random_state=seed)
    cvscores = []
    method_acc = []
    loss=[]
    for train, test in kfold.split(X, Y):
        opt = tf.optimizers.Adam(lr=0.01)
        model.compile(loss=tf.losses.binary_crossentropy, optimizer=opt, metrics=["accuracy"])
        #model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
        # Fit the model
        model.fit(X[train], Y[train], epochs=100, batch_size=64, verbose=0)
        # evaluate the model
        scores = model.evaluate(X[test], Y[test], verbose=0)
        loss.append(scores[0])
        method_acc.append(scores[1])
        print("%s: %.2f%%" % (model.metrics_names[1], scores[1] * 100))
        cvscores.append(scores[1] * 100)
    print("k-折验证结果")
    print("%.2f%% (+/- %.2f%%)" % (np.mean(cvscores), np.std(cvscores)))
    return method_acc,loss


'''
利用4层一维卷积进行二分类，输入数据中X是3维[n,400,1],y是一维数据
'''






def read_sample(count):
    list = []
    list.append("ldbc_neo4j.txt")
    list.append("ldbc_pri_neo4j.txt")
    list.append("ldbc_pri_titan.txt")
    list.append("ldbc_titan.txt")
    list.append("newdata_neo4j.txt")
    list.append("newdata_titan.txt")
    data_list = []
    for i in range(len(list)):
        f = open(str(count)+'r_'+list[i], "r")  # 设置文件对象
        line = f.readline()
        line = line[:-1]
        temp = []
        while line:
            temp.append(line)
            line = f.readline()
            line = line[:-1]
        data_list.append(temp)
    return data_list
#测试集测试结果
def retrain1(checkpoint_path,control,dataset_control):
    #x, y = read_total_sample("test_sample.txt","test_label.txt",158,dataset_control)
    x, y = read_total_sample("f1", "l1", 121, dataset_control)
    start_time=time.time()
    #x, y = read_total_sample("total_sample", "total_label", 900)
    new_x,y=rewrite_input(x, y)
    if control==1:
      new_x = np.expand_dims(new_x, axis=2)
      y = np.expand_dims(y, axis=1)
      model = new_model()
    elif control==2:
        new_x = np.expand_dims(new_x, axis=2)
        y = np.expand_dims(y, axis=1)
        model = new_model2()
    elif control==3:
        model = GetModel()  # 得到训练模型
        new_x = np.expand_dims(new_x, axis=1)
        y = np.expand_dims(y, axis=1)
    opt = tf.optimizers.Adam(lr=0.01)
    seed = 7
    np.random.seed(seed)
    model.load_weights(checkpoint_path)
    model.compile(loss=tf.losses.binary_crossentropy,
                  optimizer=opt,
                  # optimizer=opt,
                  # optimizer=tf.optimizers.RMSprop(lr=0.01),
                  metrics=["accuracy"])
    _, accuracy = model.evaluate(new_x, y)
    print("测试集准确率：")
    print(accuracy)
    end_time = time.time()
    print("测试集shijian：")
    print((end_time-start_time)/158)
    return accuracy

def total_train(checkpoint_path,control,dataset_control,):
    #x, y = read_total_sample("all_sample.txt","all_label.txt",classcount,dataset_control)
    #x, y = read_total_sample("total_sample", "total_label", 900)
    x, y = read_total_sample("f", "l", 1219, dataset_control)
    start_time = time.time()
    new_x,y=rewrite_input(x, y)
    if control==1:
      new_x = np.expand_dims(new_x, axis=2)
      y = np.expand_dims(y, axis=1)
      model = new_model()
    elif control==2:
        new_x = np.expand_dims(new_x, axis=2)
        y = np.expand_dims(y, axis=1)
        model = new_model2()
    elif control==3:
        model = GetModel()  # 得到训练模型
        new_x = np.expand_dims(new_x, axis=1)
        y = np.expand_dims(y, axis=1)
    opt = tf.optimizers.Adam(lr=0.01)
    seed = 7
    np.random.seed(seed)
    model.load_weights(checkpoint_path)
    model.compile(loss=tf.losses.binary_crossentropy,
                  optimizer=opt,
                  # optimizer=opt,
                  # optimizer=tf.optimizers.RMSprop(lr=0.01),
                  metrics=["accuracy"])
    _, accuracy = model.evaluate(new_x, y)
    print("总样本空间准确率：")
    print(accuracy)
    end_time = time.time()
    print("全集时间：")
    rtime=(end_time - start_time) /50
    print(rtime)
    return accuracy,rtime




def choose_sample_for_next(count):
    data_list = read_sample(count)
    size=20
    sample_col = []
    data_sample_path="sample.txt"
    list = []
    list.append("ldbc_neo4j.txt")
    list.append("ldbc_pri_neo4j.txt")
   # list.append("ldbc_pri_titan.txt")
    #list.append("ldbc_titan.txt")
    list.append("newdata_neo4j.txt")
    #list.append("newdata_titan.txt")
    count = count + 1
    for i in range(len(data_list)):
        sample = RandomSample(data_list[i], size)
        temp_sample = sample.reshape(size, 1)
        for temp_index in range(size):
            sample_col.append(data_list[i][temp_sample[temp_index][0]])
        # sample_col.extend(data_list[i][sample])
        data_list[i] = np.delete(data_list[i], sample)

        fw_new = open(str(count)+'r_'+list[i], 'w')
        for j in range(len(data_list[i])):
            fw_new.write(data_list[i][j]+"\n")
        fw_new.close()
    fw_sample = open(data_sample_path, 'w')
    for s in sample_col:
        fw_sample.write(s + "\n")
    fw_sample.close()

def choose_testset():
    f = open("total_sample1", "r")  # 设置文件对象
    line = f.readline()

    temp = []
    temp1 = []
    while line:
        temp.append(line)
        line = f.readline()
    f = open("total_label1", "r")  # 设置文件对象
    line = f.readline()
    while line:
        temp1.append(line)
        line = f.readline()
    size=300
    sample_col = []
    sample_label=[]
    sample = RandomSample(temp, size)
    temp_sample = sample.reshape(size, 1)
    for temp_index in range(size):
        sample_col.append(temp[temp_sample[temp_index][0]])
    for temp_index in range(size):
        sample_label.append(temp1[temp_sample[temp_index][0]])
    temp = np.delete(temp, sample)
    temp1 = np.delete(temp1, sample)
    fw_new = open("sample2", 'w')
    fw_new1 = open("label2", 'w')
    for j in range(len(temp)):
        fw_new.write(temp[j])
    for j in range(len(temp1)):
        fw_new1.write(temp1[j])

    fw_sample = open("sampletest2", 'w')
    for s in sample_col:
        fw_sample.write(s)
    fw_sample.close()
    fw_sample1 = open("labeltest2", 'w')
    for s in sample_label:
        fw_sample1.write(s)
    fw_sample1.close()

def read_total_sample(file1,file2,count,dataset_control):
    #三个数据集的特征
    #3.0 0.0 1.0 1.0 4.0 3.1 1.0 2912.0
    #3.0 0.0 1.0 1.0 0.5 0.3 1.0 1814.0
    #62.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0
    f1 = open(file1, 'r')
    f2 = open(file2, 'r')
    input_x = np.ones([count, 400]) * -1  # 定义模型的输入
    labels = np.zeros(count)
    sample_index=0
    for index in range(count):
        line=f1.readline()
        line=line[:-1]
        l=f2.readline()
        l=l[:-2]
        item = line.split(" ")
        item=item[:-1]
        #print(item)
        #input_x[index][0:len(item)] = [float(i) for i in item]
        #labels[index] = float(l)
        if dataset_control in line:
            input_x[sample_index][0:len(item)] = [float(i) for i in item]
            labels[sample_index] = float(l)
            sample_index=sample_index+1

    result_input_x=input_x[0:sample_index]
    result_labels=labels[0:sample_index]
    print(sample_index)
    #print("\r\n")

    return result_input_x, result_labels

def shuffle_train():
    f = open("total_sample1", "r")  # 设置文件对象
    line = f.readline()

    temp = []
    temp1 = []
    while line:
        temp.append(line)
        line = f.readline()
    f = open("total_label1", "r")  # 设置文件对象
    line = f.readline()
    while line:
        temp1.append(line)
        line = f.readline()
    temp_s=[]
    print(len(temp1))
    for i in range(len(temp)):
        s=temp[i]+","+temp1[i]
        temp_s.append(s)
    random.shuffle(temp_s)
    fw_sample = open("sampletest2", 'w')
    fw_sample1 = open("labeltest2", 'w')
    print(len(temp_s))
    print(temp_s[1584])
    for s in temp_s:
        ts=s.split(",")
        fw_sample.write(ts[0])
        fw_sample1.write(ts[1])
    fw_sample.close()
    fw_sample1.close()




def train_round():
    trainsets = []
    trainsets.append("trainset.txt")
    trainsets.append("trainset1.txt")
    trainsets.append("trainset2.txt")
    trainsets.append("trainset3.txt")
    x,y=read_data(trainsets)
    fw1 = open("total_sample1", 'w')
    fw2=open("total_label1",'w')
    for v in x:
        s=''
        for i in range(len(v)):
           s=s+str(v[i])+" "
        fw1.write(s+"\n")
    for l in y:
        fw2.write(str(l)+"\n")



'''
def retrain(new_x, y):
    model = load_model('first_train.h5')
    hist = model.fit(new_x, y)
    acc = hist.history['accuracy'][0]
    epoch = 0
    data_list = read_sample()
    size = 20
    acc_list = []
    data_sample_path = "/home/yangman/data_sample.txt"
    print("retrain")
    while acc < 0.9 :
        sample_col = []  # 存储抽取的样本数据，写入data_sample_path中
        # 抽样
        for i in range(len(data_list)):
            sample = RandomSample(data_list[i], size)
            temp_sample = sample.reshape(size, 1)
            for temp_index in range(size):
                sample_col.append(data_list[i][temp_sample[temp_index][0]])
            # sample_col.extend(data_list[i][sample])
            data_list[i] = np.delete(data_list[i], sample)
        fw_sample = open(data_sample_path, 'w')
        for s in sample_col:
            fw_sample.write(s + "\n")
        fw_sample.close()

        # 将抽样出来的数据进行主动学习得到标签y
        # 调用java函数到数据库中测试
        # 得到标签
        # 加载刚才打包的jar文件
        jarpath = os.path.join(os.path.abspath("."), "/home/yangman/graph-databases-testsuite-master/test.jar")

        # 获取jvm.dll 的文件路径
        jvmPath = jpype.getDefaultJVMPath()
        print(jvmPath)
        # 开启jvm
        jpype.startJVM(jvmPath, "-ea", "-Djava.class.path=%s" % (jarpath))
        # ②、加载java类（参数是java的长类名）
        javaClass = jpype.JClass("pre_data.Preprocessing")
        # 实例化java对象
        javaInstance = javaClass()
        # ③、调用java方法，由于我写的是静态方法，直接使用类名就可以调用方法
        javaInstance.label(data_sample_path)
        # ④、关闭jvm
        jpype.shutdownJVM()
        # 读取打好标签的数据
        test_result_path = "/home/yangman/results.txt"
        tes_paths = []
        tes_paths.append(test_result_path)
        tes_paths.append("trainset.txt")
        test_x, test_y = read_data(tes_paths)
        x_train, y_train = rewrite_input(test_x, test_y)
        model.fit(x_train, y_train)
        _, accuracy = model.evaluate(x_train, y_train)
        acc = accuracy
        acc_list.append(accuracy)
'''
def show_the_results(checkpoint_path,count,control):
    x, y = read_total_sample("total_sample", "total_label", count)
    new_x, y = rewrite_input(x, y)
    if control == 1:
        new_x = np.expand_dims(new_x, axis=2)
        y = np.expand_dims(y, axis=1)
        model = new_model()
    elif control == 2:
        new_x = np.expand_dims(new_x, axis=2)
        y = np.expand_dims(y, axis=1)
        model = new_model2()
    elif control == 3:
        model = GetModel()  # 得到训练模型
        new_x = np.expand_dims(new_x, axis=1)
        y = np.expand_dims(y, axis=1)
    opt = tf.optimizers.Adam(lr=0.01)
    seed = 7
    np.random.seed(seed)
    model.load_weights(checkpoint_path)
    model.compile(loss=tf.losses.binary_crossentropy,
                  optimizer=opt,
                  # optimizer=opt,
                  # optimizer=tf.optimizers.RMSprop(lr=0.01),
                  metrics=["accuracy"])
    _, accuracy = model.evaluate(new_x, y)
    print("训练集准确率：")
    print(accuracy)
    return accuracy


if __name__ == "__main__":

   # count= classify_data()
    #print("样本条数")
    #print(count)
    #first_train()
    #choose_ldbc_data()

    #draw_for1()
    #draw_col()
    exit(0)
    model_path="./round3/"
    count=[]
    count.append(600)
    count.append(900)
    count.append(1200)
    count.append(1427)

    for i in range(3):
        for j in range(4):
            index=i+1
            checkpoint_path=model_path+str(index)+str(j)+"weights.best.hdf5"
            show_the_results(checkpoint_path, count[j], index)
            retrain1(checkpoint_path,index)


    #train_round()
   # choose_testset(1)
    #choose_testset()
    #retrain1("11weights.best.hdf5",1)
    #exit(0)
    #shuffle_train()
    #draw_for()
    #exit(0)


    #first_train()

