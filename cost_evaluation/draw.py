# coding=gbk
import matplotlib.pyplot as plt
from matplotlib.pyplot import MultipleLocator
def draw_col():
    name_list = ['Freebase-small', 'Freebase-medium', 'LDBC']

    num_list = [0.016, 0.012, 0.014]
    num_list1 = [0.025, 0.018, 0.026]
    num_list2=[0.048,0.037,0.052]
    '''
    num_list = [0.017, 0.017, 0.016]
    num_list1 = [0.028, 0.023, 0.024]
    num_list2 = [0.049, 0.047, 0.046]
    '''
    x = list(range(len(num_list)))
    total_width, n = 0.8, 3
    width = total_width / n

    plt.bar(x, num_list, width=width, label='SCNN', fc='b')
    for i in range(len(x)):
        x[i] = x[i] + width
    plt.bar(x, num_list1, width=width, label='DCNN', tick_label=name_list, fc='r')
    for i in range(len(x)):
        x[i] = x[i] + width
    plt.bar(x, num_list2, width=width, label='GRU', tick_label=name_list, fc='g')
    plt.legend()
    #plt.xlabel('the rate of training set')
    # plt.xlabel('round')
    plt.ylabel('time(second)')
    plt.title('the time of predicting a single sample (database:Titan)')
    plt.show()


def draw_for():
    list1 = []
    t11 = [0.98, 0.94, 0.95, 0.91]
    t12 = [0.68, 0.76, 0.79, 0.81]
    t13 = [0.81, 0.83, 0.83, 0.76]

    list2 = []
    t21 = [0.89, 0.64, 0.89, 0.82]
    t22 = [0.63, 0.43, 0.77, 0.72]
    t23 = [0.77, 0.54, 0.71, 0.71]
    list3 = []
    t31 = [0.86, 0.73, 0.81, 0.76]
    t32 = [0.65, 0.65, 0.68, 0.66]
    t33 = [0.88, 0.79, 0.79, 0.72]
    list1.append(t11)
    list1.append(t12)
    list1.append(t13)
    list2.append(t21)
    list2.append(t22)
    list2.append(t23)
    list3.append(t31)
    list3.append(t32)
    list3.append(t33)
    x_axix = [600, 900, 1200, 1427]  # 开始画图
    plt.title('different performance comparison of 3 models')
    plt.plot(x_axix, list1[0], color='red', label='model1 train accuracy', linewidth=3)
    plt.plot(x_axix, list2[0], color='green', label='model2 train accuracy', linewidth=3)
    plt.plot(x_axix, list3[0], color='blue', label='model3 train accuracy', linewidth=3)
    plt.plot(x_axix, list1[1], color='red', label='model1 test accuracy', linewidth=4, linestyle=':')
    plt.plot(x_axix, list2[1], color='green', label='model2 test accuracy', linewidth=4, linestyle=':')
    plt.plot(x_axix, list3[1], color='blue', label='model3 test accuracy', linewidth=4, linestyle=':')
    plt.plot(x_axix, list1[2], color='red', label='model1 average accuracy', linewidth=1, linestyle='--')
    plt.plot(x_axix, list2[2], color='green', label='model2 average accuracy', linewidth=1, linestyle='--')
    plt.plot(x_axix, list3[2], color='blue', label='model3 average accuracy', linewidth=1, linestyle='--')
    plt.legend()  # 显示图例
    plt.xlabel('dataset size')
    plt.ylabel('accuracy')
    plt.show()


def draw_for1():
    list1 = []
    t11 = [0.94, 0.91, 0.94, 0.93]
    # t12=[0.84,0.89,0.92,0.96]
    # t12=[0.69,0.80,0.83,0.86]
    # t12=[0.67,0.89,0.89,0.89,0.78,0.56,0.67,0.89,1.00,0.75]
    t12 = [0.86, 0.90, 0.91, 0.90, 0.97, 0.90, 0.97, 0.94, 0.98, 0.91]
    t13 = [0.81, 0.83, 0.83, 0.76]

    list2 = []
    t21 = [0.89, 0.64, 0.83, 0.84]
    # t22=[0.62,0.64,0.79,0.80]
    # t22=[0.56,0.89,0.67,0.67,0.56,0.33,0.67,0.78,0.78,0.75]
    t22 = [0.81, 0.87, 0.91, 0.82, 0.82, 0.87, 0.81, 0.79, 0.87, 0.81]
    # t22=[0.76,0.77,0.89,0.90]
    t23 = [0.77, 0.54, 0.71, 0.71]
    list3 = []
    t31 = [0.77, 0.75, 0.85, 0.65]
    # t32=[0.66,0.68,0.69,0.74]
    # t32=[0.67,0.89,0.67,0.67,0.78,0.44,0.78,0.67,0.89,0.88]
    t32 = [0.81, 0.91, 0.94, 0.95, 0.97, 0.94, 0.95, 0.93, 0.94, 0.94]
    # t32=[0.66,0.70,0.77,0.82]
    t33 = [0.88, 0.79, 0.79, 0.72]
    list1.append(t11)
    list1.append(t12)
    list1.append(t13)
    list2.append(t21)
    list2.append(t22)
    list2.append(t23)
    list3.append(t31)
    list3.append(t32)
    list3.append(t33)
    # x_axix = [0.42,0.63,0.84,1.00]  # 开始画图
    x_axix = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    # plt.title('test accuracy of different models in LDBC dataset' )
    # plt.title('the accuracy of full dataset(LDBC)')
    # plt.title('freebase-medium dataset(49%) cross-validation')
    # plt.title('ldbc dataset(62%) cross-validation')

    x_major_locator = MultipleLocator(1)
    # 把x轴的刻度间隔设置为1，并存在变量里
    y_major_locator = MultipleLocator(0.2)
    # 把y轴的刻度间隔设置为10，并存在变量里
    ax = plt.gca()
    # ax为两条坐标轴的实例
    ax.xaxis.set_major_locator(x_major_locator)
    # 把x轴的主刻度设置为1的倍数
    ax.yaxis.set_major_locator(y_major_locator)
    # 把y轴的主刻度设置为10的倍数
    plt.xlim(1, 10)
    # 把x轴的刻度范围设置为-0.5到11，因为0.5不满一个刻度间隔，所以数字不会显示出来，但是能看到一点空白
    plt.ylim(0, 1)
    # 把y轴的刻度范围设置为-5到110，同理，-5不会标出来，但是能看到一点空白

    '''
    plt.plot(x_axix, list1[0], color='red', label='model1 train accuracy',linewidth=3)
    plt.plot(x_axix, list2[0], color='green', label='model2 train accuracy',linewidth=3)
    plt.plot(x_axix, list3[0], color='blue', label='model3 train accuracy',linewidth=3)
'''

    plt.plot(x_axix, list1[1], color='red', label='SCNN', linewidth=1, linestyle='--')
    plt.plot(x_axix, list2[1], color='green', label='DCNN', linewidth=1, linestyle='--')
    plt.plot(x_axix, list3[1], color='blue', label='GRU', linewidth=1, linestyle='--')
    '''
    plt.plot(x_axix, list1[2], color='red', label='model1 average accuracy', linewidth=1, linestyle='--')
    plt.plot(x_axix, list2[2], color='green', label='model2 average accuracy', linewidth=1, linestyle='--')
    plt.plot(x_axix, list3[2], color='blue', label='model3 average accuracy', linewidth=1, linestyle='--')
'''
    plt.legend()  # 显示图例
    plt.xlabel('the rate of training set')
    # plt.xlabel('round')
    plt.ylabel('accuracy')
    plt.show()

def draw_loss(acc,count):
    x_axix = [x for x in range(1, 11)]  # 开始画图
    plt.title('model' + str(count))
    plt.plot(x_axix, acc[0], color='green', label='600 loss')
    plt.plot(x_axix, acc[1], color='skyblue', label='900 loss')
    plt.plot(x_axix, acc[2], color='blue', label='1200 loss')
    plt.plot(x_axix, acc[3], color='red', label='1427 loss')
    plt.legend()  # 显示图例
    plt.xlabel('iteration times')
    plt.ylabel('loss')
    plt.show()
'''
绘制图像
'''
def draw(acc,count):
    x_axix = [x for x in range(1, 11)]  # 开始画图
    plt.title('model' + str(count))
    plt.plot(x_axix, acc[0], color='green', label='600 accuracy')
    plt.plot(x_axix, acc[1], color='skyblue', label='900 accuracy')
    plt.plot(x_axix, acc[2], color='blue', label='1200 accuracy')
    plt.plot(x_axix, acc[3], color='red', label='1427 accuracy')
    plt.legend()  # 显示图例
    plt.xlabel('iteration times')
    plt.ylabel('accuracy')
    plt.show()