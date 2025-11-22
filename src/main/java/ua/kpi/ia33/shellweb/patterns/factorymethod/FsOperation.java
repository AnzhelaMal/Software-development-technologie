package ua.kpi.ia33.shellweb.patterns.factorymethod;

import ua.kpi.ia33.shellweb.patterns.factorymethod.dto.OperationRequest;

@FunctionalInterface
public interface FsOperation {
    void execute(OperationRequest req) throws Exception;
}
