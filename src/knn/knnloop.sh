#!/bin/bash
# $1: data set name
# $2: max k
for i in 1 3 5
do
    java knn.Main $1 $2 $i $3 $4
#    echo $i
done
