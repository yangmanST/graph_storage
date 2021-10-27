# coding=gbk
import tensorflow as tf
'''
利用6层一维卷积进行二分类，输入数据中X是3维[n,400,1],y是一维数据
'''
def new_model2():
    model = tf.keras.Sequential()
    #model.add(tf.keras.layers.Masking(mask_value=-1, input_shape=[None, 400]))
    model.add(tf.keras.layers.Conv1D(16, 3, input_shape=(400, 1)))
    model.add(tf.keras.layers.Conv1D(16, 3, activation='tanh'))
    model.add(tf.keras.layers.MaxPooling1D(3))
    model.add(tf.keras.layers.Conv1D(64, 3, activation='tanh'))
    model.add(tf.keras.layers.Conv1D(64, 3, activation='tanh'))
    model.add(tf.keras.layers.MaxPooling1D(3))
    model.add(tf.keras.layers.Conv1D(64, 3, activation='tanh'))
    model.add(tf.keras.layers.Conv1D(64, 3, activation='tanh'))
    model.add(tf.keras.layers.MaxPooling1D(3))
    model.add(tf.keras.layers.Flatten())
    #model.add(tf.keras.layers.Dense(256, activation='relu'))
    model.add(tf.keras.layers.Dense(1, activation='sigmoid'))
    model.summary()
    return model