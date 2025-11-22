package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import ua.kpi.ia33.shellweb.service.FileOpsService;

public class DeleteOp extends AbstractFsOperation {
    private final FileOpsService ops;
    public DeleteOp(FileOpsService ops) { this.ops = ops; }

    @Override
    public void execute(OperationContext ctx) throws Exception {
        ops.delete(source, p -> ctx.setProgress(p));         // Path + progressCb
        ctx.setMessage("Видалено: " + source);
        ctx.setProgress(100);
    }
}
