package ua.kpi.ia33.shellweb.patterns.prototype.fs;

import org.springframework.stereotype.Component;
import ua.kpi.ia33.shellweb.patterns.state.op.OperationType;
import ua.kpi.ia33.shellweb.service.FileOpsService;
import ua.kpi.ia33.shellweb.service.SearchService;

import java.util.EnumMap;
import java.util.Map;

@Component
public class OperationPrototypes {
    private final Map<OperationType, FsOperation> map = new EnumMap<>(OperationType.class);

    public OperationPrototypes(FileOpsService ops, SearchService search) {
        map.put(OperationType.COPY,   new CopyOp(ops));
        map.put(OperationType.MOVE,   new MoveOp(ops));
        map.put(OperationType.DELETE, new DeleteOp(ops));
        map.put(OperationType.SEARCH, new SearchOp(search));

    }


    public FsOperation prototypeOf(OperationType type) { return map.get(type); }
}
