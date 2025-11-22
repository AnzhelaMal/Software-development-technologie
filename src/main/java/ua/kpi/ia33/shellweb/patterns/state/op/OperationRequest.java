package ua.kpi.ia33.shellweb.patterns.state.op;

import ua.kpi.ia33.shellweb.domain.User;
import java.nio.file.Path;

public class OperationRequest {
    private final OperationType type;

    // SEARCH
    private String nameMask;
    private String ext;
    private User user;

    // FILE OPS
    private Path source;
    private Path target;


    public OperationRequest(OperationType type) {
        this.type = type;
    }


    public OperationRequest nameMask(String nameMask) {
        this.nameMask = nameMask;
        return this;
    }

    public OperationRequest ext(String ext) {
        this.ext = ext;
        return this;
    }

    public OperationRequest user(User user) {
        this.user = user;
        return this;
    }

    public OperationRequest source(Path source) {
        this.source = source;
        return this;
    }

    public OperationRequest target(Path target) {
        this.target = target;
        return this;
    }

    // --- Геттери ---
    public OperationType getType() { return type; }
    public String getNameMask() { return nameMask; }
    public String getExt() { return ext; }
    public User getUser() { return user; }
    public Path getSource() { return source; }
    public Path getTarget() { return target; }
}
