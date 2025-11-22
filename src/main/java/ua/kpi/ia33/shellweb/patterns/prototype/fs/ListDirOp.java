package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import java.nio.file.Files;

public class ListDirOp extends AbstractFsOperation {
    @Override
    public void execute(OperationContext ctx) throws Exception {
        if (source == null) throw new IllegalArgumentException("source (dir) is required");
        long count = Files.isDirectory(source) ? Files.list(source).count() : 0;
        ctx.setMessage("Вміст \"" + source + "\": " + count + " елемент(ів)");
        ctx.setProgress(100);
    }
}
