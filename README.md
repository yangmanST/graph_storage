# graph_storage

## Environment Setup
Setup:
* clone the project with the command "git clone https://github.com/yangmanST/graph_storage.git"
* python environment:python version-3.7,tensorflow version-2.4.1


## Dataset 
* deploy the [benchmark](https://github.com/kuzeko/graph-databases-testsuite):deploy the benchmark in the linux ,the installation process is detailed in the benchmark link above.
* Graph datasets:In the experiment, we utilize two open source graph datasets in micro-benchmark to train models(SCNN,DCNN,GRU),which are Freebase and LDBC datasets.Download the datasets from [here](https://graphbenchmark.com/).The datasets are put in the corresponding directory of benchmark above.


## Project Structure
* coding:The related functions are in the [python file](https://github.com/yangmanST/graph_storage/tree/master/processing),just call them in the main program to coding.The results of coding are in the directory [initial](https://github.com/yangmanST/graph_storage/tree/master/dataset/initial).  
* labeling:Package [preprocesing](https://github.com/yangmanST/graph_storage/blob/master/pre_data) into a jar package, and then run in the ubuntu shell with the command "java -jar pre_data.jar" to label the sample.When run the jar package,you need to input some control information(such as 0 or 1) to choose the database.Our experiments are conducted on the Ubuntu 18.04 with 8G memory and 80G disk.
* train & test:The deep classfier is trained with CPU and 8G memory.The main program responsible for training and evaluation is in the directory [cost_evalution](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution).Just call the different functions in the main program to realize train and test process.Read the log.txt to check the experiment results.The results got include the accuracy and the labeling time and so on.  


## Code introduction
* Dataset description:  
directory:[dataset](https://github.com/yangmanST/graph_storage/tree/master/dataset/)  
[initial](https://github.com/yangmanST/graph_storage/tree/master/dataset/initial):A dataset of feature vectors generated based on the graph dataset and load and storage scheme  
[processing](https://github.com/yangmanST/graph_storage/tree/master/dataset/processing):Labeled samples obtained after stratified sampling  
[train](https://github.com/yangmanST/graph_storage/tree/master/dataset/train):The initial training set obtained by preprocessing and the new training set obtained by active learning  
[test](https://github.com/yangmanST/graph_storage/tree/master/dataset/test):Labeled test set  
[full_dataset](https://github.com/yangmanST/graph_storage/tree/master/dataset/full_dataset):Training set and test set  

* Module description:  
directory:[processing](https://github.com/yangmanST/graph_storage/tree/master/processing) Generate the initial feature vector  
directory:[cost_evalution](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution) There are three models(SCNN,DCNN,GRU) you can adjust model parameters and the main program responsible for training and evaluation.

* java project description：  
[preprocessing.java](https://github.com/yangmanST/graph_storage/blob/master/pre_data/src/pre_data/Preprocessing.java)：It parses the feature vector and interacts with the database to obtain the vector label.  


