package ua.kpi.ia33.shellweb.patterns.factorymethod.dto;

import java.nio.file.Path;

public class OperationRequest {

    private OperationType type;
    private Path source;
    private Path target;

    public OperationType getType() { return type; }
    public void setType(OperationType type) { this.type = type; }

    public Path getSource() { return source; }
    public void setSource(Path source) { this.source = source; }

    public Path getTarget() { return target; }
    public void setTarget(Path target) { this.target = target; }
}
