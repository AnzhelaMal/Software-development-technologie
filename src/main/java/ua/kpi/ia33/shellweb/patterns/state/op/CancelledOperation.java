package ua.kpi.ia33.shellweb.patterns.state.op;

public class CancelledOperation extends RuntimeException {
    public CancelledOperation() { super("cancel"); }
}
