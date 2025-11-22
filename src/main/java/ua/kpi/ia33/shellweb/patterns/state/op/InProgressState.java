package ua.kpi.ia33.shellweb.patterns.state.op;

import ua.kpi.ia33.shellweb.domain.User;
import ua.kpi.ia33.shellweb.domain.SearchQuery;
import ua.kpi.ia33.shellweb.patterns.prototype.fs.FsOperation;

import java.util.concurrent.atomic.AtomicBoolean;

public class InProgressState implements OperationState {
    private final OperationRequest req;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    public InProgressState(OperationRequest req) { this.req = req; }

    @Override
    public void start(OperationContext ctx, OperationRequest r) {
        try {
            ctx.setMessage("Виконується...");

            FsOperation op = ctx.getOperation();
            if (op != null) {
                try {
                    op.execute(ctx);
                    ctx.setMessage("Завершено (через прототип)");
                    ctx.setState(new CompletedState());
                    return;
                } catch (Exception ex) {
                    ctx.setError(ex.getMessage());
                    ctx.setMessage("Помилка виконання прототипу");
                    ctx.setState(new FailedState());
                    return;
                }
            }

            switch (req.getType()) {
                case SEARCH -> {
                    User user = req.getUser();
                    if (user == null) throw new IllegalStateException("User is required for SEARCH");

                    SearchQuery q = ctx.searchService().createQuery(user, req.getNameMask(), req.getExt());
                    ctx.setProgress(100);
                    ctx.setMessage("Пошук завершено: запит #" + q.getId());
                }

                case COPY -> ctx.fileOpsService().copy(req.getSource(), req.getTarget(), p -> {
                    if (cancelled.get()) throw new CancelledOperation();
                    ctx.setProgress(p);
                });

                case MOVE -> ctx.fileOpsService().move(req.getSource(), req.getTarget(), p -> {
                    if (cancelled.get()) throw new CancelledOperation();
                    ctx.setProgress(p);
                });

                case DELETE -> ctx.fileOpsService().delete(req.getSource(), p -> {
                    if (cancelled.get()) throw new CancelledOperation();
                    ctx.setProgress(p);
                });
            }

            ctx.setMessage("Завершено");
            ctx.setState(new CompletedState());

        } catch (CancelledOperation co) {
            ctx.setMessage("Скасовано користувачем");
            ctx.setState(new CanceledState());
        } catch (Exception ex) {
            ctx.setError(ex.getMessage());
            ctx.setMessage("Помилка");
            ctx.setState(new FailedState());
        }
    }

    @Override
    public void cancel(OperationContext ctx) { cancelled.set(true); }

    @Override public String name() { return "IN_PROGRESS"; }
    @Override public boolean isTerminal() { return false; }
    @Override public int progress(OperationContext ctx) { return ctx.getProgress(); }
    @Override public String statusMessage(OperationContext ctx) { return ctx.getMessage(); }
}
