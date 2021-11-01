# graph_storage

## Environment Setup
Setup:
* git clone https://github.com/yangmanST/graph_storage.git
* Ubuntu 18.04, python 3.7, tensorflow 2.4.1


## Dataset 
&emsp; We use the open source [micro-benchmark](https://github.com/kuzeko/graph-databases-testsuite)  to test our graph workload. Specifically, we choose the Freebase and LDBC dataset which is widely used in many existing works.  
&emsp;In Freebase, a subgraph (Frb-O), which only has nodes related to organization, business, government, finance, geography, and military are considered, is created. Then, by randomly selecting 0.1 percent and 1 percent edges of this subgraph, two datasets, Freebase-small and Freebase-middle are derived. For the LDBC dataset, the synthetic dataset (LDBC) simulates the characteristics of a real social network with a power law structure and the characteristics of the real world. 


## Project Structure
Our project consists of four main parts, including processing,pre-data,model, cost evaluation. 
* Coding part [processing](https://github.com/yangmanST/graph_storage/tree/master/processing) is responsible for extracting features from the graph workload, graph data, graph storage solution. Users could run the main program to obtain the unlabeled samples [initial](https://github.com/yangmanST/graph_storage/tree/master/dataset/initial).
* Labeling part [pre_data](https://github.com/yangmanST/graph_storage/blob/master/pre_data) is used to label the sample.Package [preprocesing](https://github.com/yangmanST/graph_storage/blob/master/pre_data) into a jar package, and then run in the ubuntu shell with the command "java -jar pre_data.jar" to label the sample。
* After obtaining some labeled data, we utilize the train part which in the  directory [model](https://github.com/yangmanST/graph_storage/tree/master/model) to achieve complete the active learning.

* Finally, we test the active learning performance, deep classifiers comparisons and time efficiency  using the [cost_evalution](https://github.com/yangmanST/graph_storage/tree/master/cost_evalution). Read the log.txt to check the experiment results.

