Part A

-source: src/main/java
-build: build
-dependency: jars
-log: the output.txt file will be in this directory
-run.sh: see below


To compile, 
> ./run.sh compile

To run, first put your file in to src/main/resources directory. My code only recognize the file in that directory. When input file name, make sure to include .txt as well.

To run, some example commands,

> ./run.sh q1 complete_graph.txt 1
> ./run.sh q2 complete_graph.txt 1
> ./run.sh q3 complete_graph.txt 1

