package ua.kpi.ia33.shellweb.service;

import java.nio.file.Path;
import java.util.function.IntConsumer;

public interface FileOpsService {
    void copy(Path src, Path dst, IntConsumer progress) throws Exception;
    void move(Path src, Path dst, IntConsumer progress) throws Exception;
    void delete(Path src, IntConsumer progress) throws Exception;
}
