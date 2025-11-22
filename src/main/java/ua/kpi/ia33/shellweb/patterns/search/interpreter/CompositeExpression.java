package ua.kpi.ia33.shellweb.patterns.search.interpreter;

import ua.kpi.ia33.shellweb.domain.SearchResult;

public class CompositeExpression implements FilterExpression {

    private final FilterExpression left;
    private final FilterExpression right;
    private final LogicalOp op;

    public CompositeExpression(FilterExpression left, LogicalOp op, FilterExpression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public boolean matches(SearchResult file) {
        return switch (op) {
            case AND -> left.matches(file) && right.matches(file);
            case OR  -> left.matches(file) || right.matches(file);
        };
    }
}
