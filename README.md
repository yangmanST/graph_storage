# graph_storage
#Background
We design an active auto-estimator to implement the graph storage solution. Considering the complex characteristics of evaluation task, we carefully design
the feature engineering, including graph data features, graph workload features and graph storage solution. Our model could be used to evaluate which one is the best storage solution incertain graph and workload. In the experiment, we utilize two open source graph datasets in micro-benchmark  to train models(SCNN,DCNN,GRU),which are Freebase and LDBC datasets.
#Experiments
Our experiments are conducted on the Ubuntu 18.04 with 8G memory and 80G disk. The deep classfier is trained with CPU and 8G memory.

#Code introduction
Dataset description:
directory:dataset/
initial:A dataset of feature vectors generated based on the graph dataset and load and storage scheme
processing:Labeled samples obtained after stratified sampling
train:The initial training set obtained by preprocessing and the new training set obtained by active learning
test:Labeled test set
full_dataset:Training set and test set

Module description:
directory:processing
Generate the initial feature vector
directory:cost_evalution
scnn.py,dcnn.py and gru.py are the three models;sampling.py is responsible for sampling in each round of active learning;classify.py is the main program responsible for training and evaluation.

java project description：
preprocessing.java：It parses the feature vector and interacts with the database to obtain the vector label.

#Results
We train the models with CPU and 8G memory.The accuracy evaluation on full dataset(LDBC) is as follows:
Model | SCNN  | DCNN    | GRU
------| ----------------| ---- 
42%   | 0.84	|0.76	    |0.66
63%   | 0.89	|0.77	    |0.70
84%   | 0.92	|0.90	    |0.77
100%  | 0.96	|0.90	    |0.82



