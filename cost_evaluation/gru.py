# coding=gbk
import tensorflow as tf
'''
����rnnģ�� ��ǩ�����㷨
�����ĵ÷ֺ͵�ǰƵ����ѯ���ֶ���û�н�������ϢϢ��أ���������+�ֶε�Ƶ����
dataset
'''
def GetModel():
    model = tf.keras.Sequential()
    #### ���masking �㣬����䳤������
    model.add(tf.keras.layers.Masking(mask_value=-1, input_shape=[None, 400]))
    #### rnn ��
    # model.add(tf.keras.layers.LSTM(32,kernel_initializer=tf.keras.initializers.Orthogonal(gain=1.0, seed=None)))
    model.add(tf.keras.layers.GRU(32, kernel_initializer=tf.keras.initializers.Orthogonal(gain=1.0, seed=None)))
    # model.add(tf.keras.layers.GRU(32,kernel_initializer=tf.keras.initializers.RandomUniform(minval=-0.05, maxval=0.05, seed=None)))
    # model.add(tf.keras.layers.c)

    ####
    model.add(tf.keras.layers.Dense(1, activation='sigmoid'))
    model.summary()
    return model