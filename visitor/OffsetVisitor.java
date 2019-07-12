package visitor;

import ast.AST;

import java.util.HashMap;


/**
 *
 * @author Lowell Milliken
 *  Updated by Ryan Shu
 */
@SuppressWarnings("ALL")
public class OffsetVisitor extends ASTVisitor {

    private int [] nCount = new int[ 100 ];
    private int [] currOffset = new int[ 100 ];

    private HashMap<AST, Integer> intOffset = new HashMap<>();
    private int depth = 0;
    private int maxDepth = 0;
    private int offsetCount = 0;
    private int call = 0;

    private void offset( AST t ) {

        if (depth > maxDepth) {
            maxDepth = depth;
        }
        intOffset.put(t, currOffset[depth]);
        currOffset[depth] += 2;
        for (AST kid : t.getKids()) {
            depth++;
            offset(kid);
            depth--;
        }
        if (t.kidCount() != 0) {
            AST leftMostChild = t.getKid(1);
            AST rightMostChild = t.getKid(t.kidCount());
            int calculatedOffset = (intOffset.get(leftMostChild) + intOffset.get(rightMostChild)) / 2;
            if (intOffset.get(t) != null) {
                if (calculatedOffset > intOffset.get(t)) {
                    intOffset.put(t, calculatedOffset);
                    currOffset[depth] = intOffset.get(t) + 2;
                } else if (calculatedOffset < intOffset.get(t)) {
                    int offsetDifference = intOffset.get(t) - calculatedOffset;
                    adjustKids(t, offsetDifference);
                }
            }
        }
    }

    private void adjustKids(AST t, int offsetCount) {
        if (t.kidCount() == 0) return;
        if (intOffset.get(t) != null) {

            for (int i = 1; i <= t.kidCount(); i++) {
                intOffset.put(t.getKid(i), intOffset.get(t.getKid(i)) + offsetCount);
            }
            currOffset[depth + 1] = intOffset.get(t.getKid(t.kidCount())) + 2;
            for (AST kid : t.getKids()) {
                depth++;
                adjustKids(kid, offsetCount);
                depth--;
            }
        }
    }
    public int getMaxOffset(){
        int max = 0;
        for(int i = 0; i < currOffset.length; i++){
            if(currOffset[i] > max){
                max = (currOffset[i]);
            }
        }
        return max;
    }


    public HashMap<AST, Integer> getOffset() {
        HashMap<AST, Integer> offset = intOffset;
        return offset;
    }

    public void printCount() {
        for( int i = 0; i <= maxDepth; i++ ) {
            System.out.println( "Depth: " + i + " currOffset: " + currOffset[ i ] );
        }
    }

    public Object visitProgramTree( AST t ) { offset( t ); return null; }
    public Object visitBlockTree( AST t ) { offset( t ); return null; }
    public Object visitFunctionDeclTree( AST t ) { offset( t ); return null; }
    public Object visitCallTree( AST t ) { offset( t ); return null; }
    public Object visitDeclTree( AST t ) { offset( t ); return null; }
    public Object visitIntTypeTree( AST t ) { offset( t ); return null; }
    public Object visitFloatTypeTree( AST t ) { offset( t ); return null; }
    public Object visitVoidTypeTree( AST t ) { offset( t ); return null; }
    public Object visitBoolTypeTree( AST t ) { offset( t ); return null; }
    public Object visitFormalsTree( AST t ) { offset( t ); return null; }
    public Object visitActualArgsTree( AST t ) { offset( t ); return null; }
    public Object visitIfTree( AST t ) { offset( t ); return null; }
    public Object visitForTree( AST t ) { offset( t ); return null; }
    public Object visitWhileTree( AST t ) { offset( t ); return null; }
    public Object visitReturnTree( AST t ) { offset( t ); return null; }
    public Object visitAssignTree( AST t ) { offset( t ); return null; }
    public Object visitIntTree( AST t ) { offset( t ); return null; }
    public Object visitFloatTree( AST t ) { offset( t ); return null; }
    public Object visitVoidTree( AST t ) { offset( t ); return null; }
    public Object visitCharTree(AST t) { offset( t ); return null; }
    public Object visitCharTypeTree(AST t) { offset( t ); return null; }
    public Object visitScientificTree(AST t) { offset( t ); return null; }
    public Object visitScientificLitTree(AST t) { offset( t ); return null; }
    public Object visitScientificTypeTree(AST t) { offset( t ); return null; }
    public Object visitIdTree( AST t ) { offset( t ); return null; }
    public Object visitRelOpTree( AST t ) { offset( t ); return null; }
    public Object visitAddOpTree( AST t ) { offset( t ); return null; }
    public Object visitMultOpTree( AST t ) { offset( t ); return null; }
    public Object visitRepeatTree( AST t ) { offset( t ); return null; }
    public Object visitUnlessTree( AST t ) { offset( t ); return null; }
    public Object visitEHeadTree( AST t ) { offset( t ); return null; }
}