package ast;

import visitor.ASTVisitor;

public class ForTree extends AST {

    public ForTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitForTree(this);
    }

}

