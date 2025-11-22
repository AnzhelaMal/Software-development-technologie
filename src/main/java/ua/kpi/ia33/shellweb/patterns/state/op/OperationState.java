package ua.kpi.ia33.shellweb.patterns.state.op;

public interface OperationState {
    void start(OperationContext ctx, OperationRequest req);
    void cancel(OperationContext ctx);

    String name();
    boolean isTerminal();

    int progress(OperationContext ctx);
    String statusMessage(OperationContext ctx);
}
