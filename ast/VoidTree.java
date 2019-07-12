package ast;

import visitor.*;

public class VoidTree extends AST {

    public VoidTree() {
    }

    public Object accept(ASTVisitor v) {
        return v.visitVoidTree(this);
    }

}
