package ua.kpi.ia33.shellweb.patterns.state.op;

public class CompletedState implements OperationState {
    @Override public void start(OperationContext ctx, OperationRequest req) {
        // новий цикл тієї ж операції
        ctx.setState(new IdleState());
        ctx.start(req);
    }

    @Override public void cancel(OperationContext ctx) {/* no-op */}
    @Override public String name() { return "COMPLETED"; }
    @Override public boolean isTerminal() { return true; }
    @Override public int progress(OperationContext ctx) { return 100; }
    @Override public String statusMessage(OperationContext ctx) { return "Завершено успішно"; }
}
