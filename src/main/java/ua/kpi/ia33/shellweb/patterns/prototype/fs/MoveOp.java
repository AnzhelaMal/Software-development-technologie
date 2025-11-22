package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import ua.kpi.ia33.shellweb.service.FileOpsService;

public class MoveOp extends AbstractFsOperation {
    private final FileOpsService ops;
    public MoveOp(FileOpsService ops) { this.ops = ops; }

    @Override
    public void execute(OperationContext ctx) throws Exception {
        ops.move(source, target, p -> ctx.setProgress(p));   // Path + progressCb
        ctx.setMessage("Переміщено: " + source + " → " + target);
    }
}
