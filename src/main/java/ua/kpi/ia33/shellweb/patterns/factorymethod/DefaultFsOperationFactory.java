package ua.kpi.ia33.shellweb.patterns.factorymethod;

import org.springframework.stereotype.Component;
import ua.kpi.ia33.shellweb.patterns.prototype.fs.FsOperation;
import ua.kpi.ia33.shellweb.patterns.prototype.fs.OperationPrototypes;
import ua.kpi.ia33.shellweb.patterns.state.op.OperationRequest;
import ua.kpi.ia33.shellweb.patterns.state.op.OperationType;

@Component
public class DefaultFsOperationFactory {

    private final OperationPrototypes prototypes;

    public DefaultFsOperationFactory(OperationPrototypes prototypes) {
        this.prototypes = prototypes;
    }

    public FsOperation create(OperationType type, OperationRequest req) {
        return prototypes
                .prototypeOf(type)
                .clone()
                .withRequest(req);
    }
}
