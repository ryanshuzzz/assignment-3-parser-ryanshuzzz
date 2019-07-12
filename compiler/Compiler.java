package compiler;

import ast.*;
import parser.Parser;
import visitor.*;

/**
 *  The Compiler class contains the main program for compiling
 *  a source program to bytecodes
*/
public class Compiler {

/**
 * The Compiler class reads and compiles a source program
*/
	
	String sourceFile;
	
    public Compiler(String sourceFile) {
    	this.sourceFile = sourceFile;
    }
    
    void compileProgram() {
        try {
//            System.out.println("---------------TOKENS-------------");
            Parser parser = new Parser(sourceFile);
            AST t = parser.execute();
            System.out.println("---------------AST-------------");
            PrintVisitor pv = new PrintVisitor();
            t.accept(pv);
            CountVisitor cv = new CountVisitor();
            t.accept(cv);
            OffsetVisitor ov = new OffsetVisitor();
            t.accept(ov);
            DrawOffsetVisitor dv = new DrawOffsetVisitor(cv.getCount(), ov.getOffset(), ov.getMaxOffset());
            t.accept(dv);
            dv.display();
/*  COMMENT CODE FROM HERE UNTIL THE CATCH CLAUSE WHEN TESTING PARSER
            Constrainer con = new Constrainer(t,parser);
            con.execute();
            System.out.println("---------------DECORATED AST-------------");
            t.accept(pv);
/*  COMMENT CODE FROM HERE UNTIL THE CATCH CLAUSE WHEN TESTING CONSTRAINER
            Codegen generator = new Codegen(t);
            Program program = generator.execute();
            System.out.println("---------------AST AFTER CODEGEN-------------");
            t.accept(pv);
            System.out.println("---------------INTRINSIC TREES-------------");
            System.out.println("---------------READ/WRITE TREES-------------");
            Constrainer.readTree.accept(pv);
            Constrainer.writeTree.accept(pv);
            System.out.println("---------------INT/BOOL TREES-------------");
            Constrainer.intTree.accept(pv);
            Constrainer.boolTree.accept(pv);
            program.printCodes(sourceFile + ".cod");*/
            // if the source file is "abc" print bytecodes to abc.cod
        }catch (Exception e) {
            System.out.println("********exception*******"+e.toString());
         };
    }
    
    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("***Incorrect usage, try: java compiler.Compiler <file>");
            System.exit(1);
        }
        (new Compiler(args[0])).compileProgram();
    }
}
