Download the latest source code from:
https://github.com/allenlsy/knn/zipball/master

Or, if you are using git, after you configure your git environment, please use the following commands:

```sh
mkdir knn
cd knn
git clone git@github.com:allenlsy/knn.git
```

----------------------------------------
This is a eclipse project. You can open the whole project with eclipse using Java project
wizard.

To run the program, please ensure that JDK and JRE is correctly configured on your 
computer. Then go to the `/src/knn` directory in command line(in Windows) or terminal(in 
Unix-based OS).

__1. To compile the project__

```sh
javac -d . *.java
```

OR

```sh
sh comp
```

__2. Run__

Type `java knn.Main` to view the command line help message

__3. Report__

Report is in prediction.txt.

__4. Some Examples__

```sh
# sh comp
# java knn.Main
# java knn.Main knn 
# java knn.Main knn dna 5 0
# java knn.Main knn dna.train.txt dna.test.txt 5 0
# java knn.Main lsh dna 10 10 5 0
# java knn.Main lsh dna 10 10 5 0 5 5
# java knn.Main lsh dna 10 10 5 0 5 5 d
```

-----------------------------------------------------------
For any further information, please email cafe@allenlsy.com   
