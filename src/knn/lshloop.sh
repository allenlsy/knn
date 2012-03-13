#!/bin/bash
# $1: data set name
# $2: max k
# for ds in svmguide4 vowel splice dna svmguide4 satimage usps letter
# do
    for i in 1 3 5
    do

        java knn.Main lslsh $1 5 5 $i 0 1 1 $2
    done
#    echo $i
# done
