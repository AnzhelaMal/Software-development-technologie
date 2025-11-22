package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;

public class SwitchRootOp extends AbstractFsOperation {
    @Override
    public void execute(OperationContext ctx) {
        if (target == null) throw new IllegalArgumentException("target (new root) is required");
        // Тут можна просто зберігати вибраний “корінь” у повідомленні/сесії,
        // бо реального перемикання диска в ОС не потрібно для ЛР.
        ctx.setMessage("Перемкнено корінь на: " + target);
        ctx.setProgress(100);
    }
}
