package ua.kpi.ia33.shellweb.patterns.state.op;

public class IdleState implements OperationState {
    @Override
    public void start(OperationContext ctx, OperationRequest req) {
        ctx.setState(new PreparingState(req));
        ctx.getState().start(ctx, req);
    }

    @Override public void cancel(OperationContext ctx) {/* no-op */}
    @Override public String name() { return "IDLE"; }
    @Override public boolean isTerminal() { return false; }
    @Override public int progress(OperationContext ctx) { return 0; }
    @Override public String statusMessage(OperationContext ctx) { return "Очікування запуску"; }
}
