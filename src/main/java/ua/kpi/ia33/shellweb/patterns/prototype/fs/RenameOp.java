package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class RenameOp extends AbstractFsOperation {
    @Override
    public void execute(OperationContext ctx) throws Exception {
        if (source == null || target == null)
            throw new IllegalArgumentException("source/target are required");
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        ctx.setMessage("Перейменовано: " + source + " → " + target);
        ctx.setProgress(100);
    }
}
