# cs367_p1_grading

## Testing files
### Input files
We provide three input files to construct your database: input1.txt input2.txt input3.txt
### Operation files
The commands we test are in the following files: case1.txt, d1.txt, r1.txt, sf1.txt, i2.txt, user3.txt
The number in the filename is used to indicate which input file they work with
### Expected output
Check the expected result in these files. For example, "expected_case1.txt" is the output when you use input1 to construct the database and enter the commands in case1.txt

## Using the grading script

First, copy all the .txt and .java files in this repository to your directory, which should have your own CustomerDatabase.java and InteractiveDBTester.java

If you have python installed in your environment, then it is easy:
```
python grade_learnUW.py -p [the path of your directory]
```

Use editor to open the file "grade.out" in your directory.

## Compare your program's output with what we expecte
For example, you want to compare what your program output when passing "input1.txt" and running the command in "case1.txt":

```
java InteractiveDBTester input1.txt < case1.txt
```

The expected result is in the file "expected_case1.txt"

* hint: you can use `diff` to quickly do the comparison like
```
java InteractiveDBTester input1.txt < case1.txt | diff - expected_case1.txt
```
