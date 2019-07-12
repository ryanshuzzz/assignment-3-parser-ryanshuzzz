# assignment-3-parser-ryanshuzzz
assignment-3-parser-ryanshuzzz<br />
Ryan Shu (916850524)<br />
GitHub Repo<br />
(https://github.com/sfsu-csc-413-spring-2019/assignment-3-parser-ryanshuzzz.git)<br />
CSC 413-05<br />
<br />
### Overview
<br />
### Project Introduction
This repository contains the X language compiler. The goal of this specific repository is to continue to extend the capabilities of the compiler and extend it in order to handle the new tokens and production rules added to the X language compiler. The packages that will need to be updated are ast, compiler, parser, and visitor.  In the ast package I created new classes for the types void, char, char literals, scientific  and scientific literals. In the compiler package I have updated it to call DrawOffsetVisitor, instead of DrawVisitor in order to output the correct tree. In the parser package we have to add the grammar for 
S → ‘if’ E ‘then’ BLOCK if statement (without else)<br />
S → ‘unless’ E ‘then’ BLOCK unless statement<br />
S → ‘unless’ E ‘then’ BLOCK ‘else’ BLOCK unless/else statementv
S → ‘for’ EHEAD BLOCK for statement<br />
EHEAD → ‘(‘ ASSIGN? ‘;’ E? ‘;’ ASSIGN? ‘)’ for statement control<br />
E → SE ‘>’ SE Greater token<br />
E → SE ‘>=’ SE GreaterEqual token<br />
<br />
In the ast package we have to create OffsetVisitor.java, and DrawOffsetVisitor.java to display a tree where the nodes are correctly aligned.
<br />

### Summary of Technical Work
After adding the previously mentioned classes and methods, I can run compiler.Compiler with some of the sample files found in /sample_files/ to see if the correct output is outputted. 

### Execution and Development Environment Described
I developed from this code base using IntelliJ IDEA Community v. 2018.3.4. This Java application was
compiled using Java JDK version 11.0.2 which is the most up to date version of the JDK.

### Scope of Work Described
    • Create classes in package ast for distinct trees<br />
        ◦ VoidTree.java<br />
        ◦ CharTree.java<br />
        ◦ CharTypeTree.java<br />
        ◦ ScientificTree.java<br />
        ◦ ScientificTypeTree.java<br />
        ◦ ScientificLitTree.java<br />
        ◦ ReturnTree.java<br />
    • Create Class OffsetVisitor.java in package visitor<br />
        ◦ Create HashMap containing the Node, and correct offset<br />
        ◦ Create method to pass HashMap into DrawoffsetVisitor.java<br />
        ◦ Create method to pass int maxOffset for DrawOffsetVisitor to find width of window<br />
        ◦ Use recursive calls to make sure every node is visited and has an updated offset<br />
    • Create Class DrawOffsetVisitor.java<br />
        ◦ Use maxOffset to create window with the correct size<br />
        ◦ Use the Hashmap to place nodes at correct offset<br />
        ◦ Create JFrame to show proper nodes with lines to display tree<br />

### Instructions to Compile and Execute
Dependencies:<br />
Java JDK 11.0.2<br />

Java JRE 18.9 or Newer<br />
    1. Clone this repository<br />
    2. Navigate into the repository folder(assignment-3-parser-ryanshuzzz)<br />
    3. Compile using javac compiler/Compiler.java<br />
    4. Run using java compiler.Compiler.java [test file location]<br />
    5. ◦ given test files are in /sample_files/<br />

### Assumptions Made
No assumptions were made for this project.

### Implementation Discussion

### Implementation Decisions
For this assignment I found that there were many decisions that I needed to make during implementation. Initially, my decision was to attempt to iterate through all of the nodes. I soon found that this would be nearly impossible and would be much easier to do it recursively. After doing this, and following the algorithm provided in the lecture slides, I created a second recursive method to update the child nodes to the correct offset.
### Code Organization
To organize the code as much as possible, in the visitor package, I make the visitors calls only one line to reduce the amount of lines the file is as a whole. Also, in OffsetVisitor, I created two recursive calls, one for the initial childs, and one to re-update the child nodes with the initial incorrect offset. 
### Results/Conclusion
The Parser works as described and outputs the correct values, with the right offsets. Also, the JFrame that the DrawOffsetVisitor creates is accurately placing all of the nodes at the correct offsets.

### Sample Output
```
compiler.Compiler sample_files/simple.x<br />
1: program { int i int j
2:    i = i + j + 7
3:    j = write(i)
4: }
---------------AST-------------
1:  Program
2:    Block
5:      Decl
3:        IntType
4:        Id: i
8:      Decl
6:        IntType
7:        Id: j
10:     Assign
9:        Id: i
14:       AddOp: +
12:         AddOp: +
11:           Id: i
13:           Id: j
15:         Int: 7
17:     Assign
16:       Id: j
19:       Call
18:         Id: write
20:         Id: i
```
### Class Diagram

### Visitor Package
![alt text](https://github.com/sfsu-csc-413-spring-2019/assignment-3-parser-ryanshuzzz/blob/master/VISITOR1.png)
![alt text](https://github.com/sfsu-csc-413-spring-2019/assignment-3-parser-ryanshuzzz/blob/master/VISITOR2.png)
### Parser Package
![alt text](https://github.com/sfsu-csc-413-spring-2019/assignment-3-parser-ryanshuzzz/blob/master/PARSER.png)
### AST Package
![alt text](https://github.com/sfsu-csc-413-spring-2019/assignment-3-parser-ryanshuzzz/blob/master/AST.png)
