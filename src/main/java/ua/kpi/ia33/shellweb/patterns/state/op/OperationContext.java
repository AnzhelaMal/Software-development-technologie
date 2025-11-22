package ua.kpi.ia33.shellweb.patterns.state.op;

import ua.kpi.ia33.shellweb.service.SearchService;
import ua.kpi.ia33.shellweb.service.FileOpsService;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import ua.kpi.ia33.shellweb.patterns.prototype.fs.FsOperation;


public class OperationContext {
    private final String id = UUID.randomUUID().toString();
    private final AtomicReference<OperationState> state = new AtomicReference<>();
    private final AtomicInteger progress = new AtomicInteger(0);
    private volatile String message;
    private volatile String error;

    private final SearchService searchService;
    private final FileOpsService fileOpsService;

    public OperationContext(SearchService searchService, FileOpsService fileOpsService) {
        this.searchService = searchService;
        this.fileOpsService = fileOpsService;
        this.state.set(new IdleState());
    }

    public String id() { return id; }
    public OperationState getState() { return state.get(); }
    public void setState(OperationState newState) { state.set(newState); }

    public void start(OperationRequest req) { state.get().start(this, req); }
    public void cancel() { state.get().cancel(this); }

    public int getProgress() { return progress.get(); }
    public void setProgress(int p) { progress.set(Math.max(0, Math.min(100, p))); }

    public String getMessage() { return message; }
    public void setMessage(String msg) { this.message = msg; }

    public String getError() { return error; }
    public void setError(String err) { this.error = err; }

    public SearchService searchService() { return searchService; }
    public FileOpsService fileOpsService() { return fileOpsService; }

    private FsOperation operation;
    public FsOperation getOperation() { return operation; }
    public void setOperation(FsOperation op) { this.operation = op; }
}
