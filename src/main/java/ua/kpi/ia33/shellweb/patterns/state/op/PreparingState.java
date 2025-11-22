package ua.kpi.ia33.shellweb.patterns.state.op;

public class PreparingState implements OperationState {
    private final OperationRequest req;
    public PreparingState(OperationRequest req) { this.req = req; }

    @Override
    public void start(OperationContext ctx, OperationRequest r) {
        ctx.setMessage("Підготовка...");
        ctx.setProgress(5);
        // тут можна додати перевірки прав, існування шляхів, оцінку обсягу тощо
        ctx.setState(new InProgressState(req));
        ctx.getState().start(ctx, req);
    }

    @Override public void cancel(OperationContext ctx) {
        ctx.setMessage("Скасовано на етапі підготовки");
        ctx.setState(new CanceledState());
    }

    @Override public String name() { return "PREPARING"; }
    @Override public boolean isTerminal() { return false; }
    @Override public int progress(OperationContext ctx) { return Math.max(5, ctx.getProgress()); }
    @Override public String statusMessage(OperationContext ctx) { return ctx.getMessage(); }
}
