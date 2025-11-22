package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import ua.kpi.ia33.shellweb.service.FileOpsService;



public class CopyOp extends AbstractFsOperation {
    private final FileOpsService ops;

    public CopyOp(FileOpsService ops) { this.ops = ops; }

    @Override
    public void execute(OperationContext ctx) throws Exception {
        ops.copy(source, target, p -> ctx.setProgress(p));   // Path + progressCb
        ctx.setMessage("Скопійовано: " + source + " → " + target);
    }
}
