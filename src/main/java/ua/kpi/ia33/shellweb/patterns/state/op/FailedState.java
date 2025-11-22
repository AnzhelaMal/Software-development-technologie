package ua.kpi.ia33.shellweb.patterns.state.op;

public class FailedState implements OperationState {
    @Override public void start(OperationContext ctx, OperationRequest req) {
        // повторити
        ctx.setState(new IdleState());
        ctx.start(req);
    }
    @Override public void cancel(OperationContext ctx) {/* no-op */}
    @Override public String name() { return "FAILED"; }
    @Override public boolean isTerminal() { return true; }
    @Override public int progress(OperationContext ctx) { return ctx.getProgress(); }
    @Override public String statusMessage(OperationContext ctx) {
        return "Помилка: " + ctx.getError();
    }
}
