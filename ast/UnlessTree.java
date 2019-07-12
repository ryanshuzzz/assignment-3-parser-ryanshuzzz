package ast;

import visitor.ASTVisitor;

public class UnlessTree extends AST {

    public UnlessTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitUnlessTree(this);
    }

}

