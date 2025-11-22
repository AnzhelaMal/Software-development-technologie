package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import ua.kpi.ia33.shellweb.patterns.state.op.OperationRequest;
import ua.kpi.ia33.shellweb.patterns.state.op.OperationContext;
import ua.kpi.ia33.shellweb.domain.User;

import java.nio.file.Path;

public abstract class AbstractFsOperation implements FsOperation {
    protected Path source, target;
    protected String nameMask, ext;
    protected User user;

    @Override
    public FsOperation withRequest(OperationRequest r) {
        this.source   = r.getSource();
        this.target   = r.getTarget();
        this.nameMask = r.getNameMask();
        this.ext      = r.getExt();
        this.user     = r.getUser();
        return this;
    }

    @Override
    public FsOperation clone() {
        try { return (FsOperation) super.clone(); }
        catch (CloneNotSupportedException e) { throw new AssertionError(e); }
    }

    @Override
    public abstract void execute(OperationContext ctx) throws Exception;
}
