# graph_storage
## Background
We design an active auto-estimator to implement the graph storage solution. Considering the complex characteristics of evaluation task, we carefully design
the feature engineering, including graph data features, graph workload features and graph storage solution. Our model could be used to evaluate which one is the best storage solution incertain graph and workload. In the experiment, we utilize two open source graph datasets in micro-benchmark  to train models(SCNN,DCNN,GRU),which are Freebase and LDBC datasets.
#Experiments
Our experiments are conducted on the Ubuntu 18.04 with 8G memory and 80G disk. The deep classfier is trained with CPU and 8G memory.

## Code introduction
Dataset description:  
directory:[dataset](https://github.com/yangmanST/graph_storage/tree/master/dataset/)  
[initial](https://github.com/yangmanST/graph_storage/tree/master/dataset/initial):A dataset of feature vectors generated based on the graph dataset and load and storage scheme  
[processing](https://github.com/yangmanST/graph_storage/tree/master/dataset/processing):Labeled samples obtained after stratified sampling  
[train](https://github.com/yangmanST/graph_storage/tree/master/dataset/train):The initial training set obtained by preprocessing and the new training set obtained by active learning  
[test](https://github.com/yangmanST/graph_storage/tree/master/dataset/test):Labeled test set  
[full_dataset](https://github.com/yangmanST/graph_storage/tree/master/dataset/full_dataset):Training set and test set  

Module description:
directory:[processing](https://github.com/yangmanST/graph_storage/tree/master/processing)  
Generate the initial feature vector  
directory:[cost_evalution](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution)  
[scnn.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/scnn.py),[dcnn.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/dcnn.py) and [gru.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/gru.py) are the three models;[sampling.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/sampling.py) is responsible for sampling in each round of active learning;[classify.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/classify.py) is the main program responsible for training and evaluation.

java project description：
preprocessing.java：It parses the feature vector and interacts with the database to obtain the vector label.
## run
labeling phase：Package preprocesing into a jar package, and then run in the ubuntu.Before run the jar package,you need to deploy the [benchmark](https://github.com/kuzeko/graph-databases-testsuite)
train phase:the main program responsible for training and evaluation is [classify.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/classify.py)

## Results
We train the models with CPU and 8G memory.The accuracy evaluation on full dataset(LDBC) is as follows:
|Model | SCNN  | DCNN    | GRU  |
|------| ----------------| ---- |
|42%   | 0.84	|0.76	    |0.66  |
|63%   | 0.89	|0.77	    |0.70  |
|84%   | 0.92	|0.90	    |0.77  |
|100%  | 0.96	|0.90	    |0.82  |



