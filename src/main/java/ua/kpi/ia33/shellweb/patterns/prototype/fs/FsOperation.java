package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationRequest;
import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;



public interface FsOperation extends Cloneable {
    FsOperation withRequest(OperationRequest req); // заповнюємо source/target/nameMask/ext
    void execute(OperationContext ctx) throws Exception; // реальна дія
    FsOperation clone();
}
