package ua.kpi.ia33.shellweb.patterns.state.op;

public class CanceledState implements OperationState {
    @Override public void start(OperationContext ctx, OperationRequest req) {
        ctx.setState(new IdleState());
        ctx.start(req);
    }
    @Override public void cancel(OperationContext ctx) {/* no-op */}
    @Override public String name() { return "CANCELED"; }
    @Override public boolean isTerminal() { return true; }
    @Override public int progress(OperationContext ctx) { return ctx.getProgress(); }
    @Override public String statusMessage(OperationContext ctx) { return "Скасовано"; }
}
