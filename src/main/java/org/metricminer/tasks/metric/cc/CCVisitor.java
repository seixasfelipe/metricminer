package org.metricminer.tasks.metric.cc;

import static org.metricminer.tasks.metric.common.FullMethodName.fullMethodName;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.WhileStmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.metricminer.tasks.metric.common.ClassInfoVisitor;


public class CCVisitor extends ClassInfoVisitor {

    private Map<String, Integer> ccPerMethod;
    private Stack<String> methodStack;

    public CCVisitor() {
        ccPerMethod = new HashMap<String, Integer>();
        methodStack = new Stack<String>();
    }

    public void visit(ConstructorDeclaration expr, Object arg) {
        methodStack.push(fullMethodName(expr.getName(), expr.getParameters()));
        increaseCc();
        super.visit(expr, arg);
        methodStack.pop();
    }

    public void visit(InitializerDeclaration expr, Object arg) {
        if (expr.isStatic()) {
            methodStack.push("(static block)");
            increaseCc();
            super.visit(expr, arg);
            methodStack.pop();
        }
    }

    public void visit(MethodDeclaration expr, Object arg) {
        methodStack.push(fullMethodName(expr.getName(), expr.getParameters()));
        increaseCc();
        super.visit(expr, arg);
        methodStack.pop();
    }

    public void visit(ForeachStmt expr, Object arg) {
        increaseCc();

        super.visit(expr, arg);

    }

    public void visit(IfStmt expr, Object arg) {
        increaseCc();

        super.visit(expr, arg);
    }

    public void visit(ForStmt expr, Object arg) {
        increaseCc();
        super.visit(expr, arg);
    }

    public void visit(WhileStmt expr, Object arg) {
        increaseCc();
        super.visit(expr, arg);
    }

    public void visit(BinaryExpr expr, Object arg) {
        if ("and".equals(expr.getOperator().name()))
            increaseCc();
        if ("or".equals(expr.getOperator().name()))
            increaseCc();
        super.visit(expr, arg);
    }

    public void visit(SwitchEntryStmt expr, Object arg) {
        if (expr.getLabel() != null)
            increaseCc();
        super.visit(expr, arg);
    }

    public void visit(DoStmt expr, Object arg) {
        increaseCc();
        super.visit(expr, arg);
    }

    public void visit(CatchClause expr, Object arg) {
        increaseCc();
        super.visit(expr, arg);
    }

    public void visit(ConditionalExpr expr, Object arg) {
        increaseCc();
        super.visit(expr, arg);
    }

    private void increaseCc() {
        String currentMethod = methodStack.peek();
        if (!ccPerMethod.containsKey(currentMethod))
            ccPerMethod.put(currentMethod, 0);

        ccPerMethod.put(currentMethod, ccPerMethod.get(currentMethod) + 1);

    }

    public void visit(FieldDeclaration expr, Object arg) {
        methodStack.push("attribute "
                + expr.getVariables().get(expr.getVariables().size() - 1).getId().getName());
        super.visit(expr, arg);
        methodStack.pop();
    }

    public int getCc() {
        int cc = 0;
        for (Entry<String, Integer> method : ccPerMethod.entrySet()) {
            cc += method.getValue();
        }
        return cc;
    }

    public int getCc(String method) {
        return ccPerMethod.get(method);
    }

    public double getAvgCc() {
        double avg = 0;

        for (Entry<String, Integer> method : ccPerMethod.entrySet()) {
            avg += method.getValue();
        }

        return avg / ccPerMethod.size();
    }

    public Map<String, Integer> getCcPerMethod() {
        return ccPerMethod;
    }

}
