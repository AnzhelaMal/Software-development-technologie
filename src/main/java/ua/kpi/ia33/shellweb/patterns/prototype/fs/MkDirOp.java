package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import java.nio.file.Files;

public class MkDirOp extends AbstractFsOperation {
    @Override
    public void execute(OperationContext ctx) throws Exception {
        if (target == null) throw new IllegalArgumentException("target (dir) is required");
        Files.createDirectories(target);
        ctx.setMessage("Створено теку: " + target);
        ctx.setProgress(100);
    }
}
