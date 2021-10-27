# coding=gbk
import tensorflow as tf
'''
定义rnn模型 标签传播算法
索引的得分和当前频繁查询的字段有没有建立索引息息相关：索引向量+字段的频繁度
dataset
'''
def GetModel():
    model = tf.keras.Sequential()
    #### 添加masking 层，处理变长的输入
    model.add(tf.keras.layers.Masking(mask_value=-1, input_shape=[None, 400]))
    #### rnn 层
    # model.add(tf.keras.layers.LSTM(32,kernel_initializer=tf.keras.initializers.Orthogonal(gain=1.0, seed=None)))
    model.add(tf.keras.layers.GRU(32, kernel_initializer=tf.keras.initializers.Orthogonal(gain=1.0, seed=None)))
    # model.add(tf.keras.layers.GRU(32,kernel_initializer=tf.keras.initializers.RandomUniform(minval=-0.05, maxval=0.05, seed=None)))
    # model.add(tf.keras.layers.c)

    ####
    model.add(tf.keras.layers.Dense(1, activation='sigmoid'))
    model.summary()
    return model