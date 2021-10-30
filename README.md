# graph_storage

## Environment Setup
Setup:
* clone the project with the command "git clone https://github.com/yangmanST/graph_storage.git"
* install python dependencies:just install -dependencies
* deploy the [benchmark](https://github.com/kuzeko/graph-databases-testsuite):deploy the benchmark in the linux ,the installation process is detailed in the benchmark link above.

## Dataset 
* Graph datasets:In the experiment, we utilize two open source graph datasets in micro-benchmark to train models(SCNN,DCNN,GRU),which are Freebase and LDBC datasets.Download the datasets from [here](https://graphbenchmark.com/).The datasets are put in the corresponding directory of benchmark above.
* Feature vector datasets:After the coding phase,the related datasets need to be put in the directory "pre_data/src/pre_data/ve/".

## labeling  
Package [preprocesing](https://github.com/yangmanST/graph_storage/blob/master/pre_data) into a jar package, and then run in the ubuntu shell with the command "java -jar pre_data.jar" to label the sample.Our experiments are conducted on the Ubuntu 18.04 with 8G memory and 80G disk.

## Train & Test  
The deep classfier is trained with CPU and 8G memory.
* train phase:the main program responsible for training and evaluation is [classify.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/classify.py).The train process is realized by the function "first_train"
* test phase:the function "retrain1" realizes the test phase
* cross-validation: the function "k_test" realizes the cross-validation 
Just call the different functions in the main program to realize train and test process.


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
[scnn.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/scnn.py),[dcnn.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/dcnn.py) and [gru.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/gru.py) are the three models,here you can adjust model parameters;[sampling.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/sampling.py) is responsible for sampling in each round of active learning;[classify.py](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution/classify.py) is the main program responsible for training and evaluation.

java project description：  
[preprocessing.java](https://github.com/yangmanST/graph_storage/blob/master/pre_data/src/pre_data/Preprocessing.java)：It parses the feature vector and interacts with the database to obtain the vector label.  



## Results
We train the models with CPU and 8G memory.  
Test accuracy of different models in LDBC dataset is as follows:  
| Model  | SCNN  | DCNN  | GRU  |
| ---  | ----  | ---- | ---  |
| 42%  | 0.69  |	0.62  |	0.66  |
| 63%  | 0.80  |	0.64  |	0.68  |
| 84%  | 0.83  |	0.79  |	0.69  |
| 100%  | 0.86  |	0.80  |	0.74  |

The accuracy evaluation on full dataset(LDBC) is as follows:  
| Model  | SCNN  | DCNN  | GRU  |
| ---  | ----  | ---- | ---  |
| 42%  | 0.84	 | 0.76  | 0.66  |
| 63%  | 0.89	 | 0.77  | 0.70  |
| 84%  | 0.92	 | 0.90	 | 0.77  |
| 100%  | 0.96	 | 0.90	 | 0.82  |
