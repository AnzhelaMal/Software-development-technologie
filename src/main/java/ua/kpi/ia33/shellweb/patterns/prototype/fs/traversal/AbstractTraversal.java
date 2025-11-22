package ua.kpi.ia33.shellweb.patterns.prototype.fs.traversal;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTraversal {

    protected final Path root;
    protected final List<Path> visited = new ArrayList<>();

    protected AbstractTraversal(Path root) {
        this.root = root;
    }

    /**
     * Template Method — фіксує скелет алгоритму обходу.
     */
    public final void traverse() throws IOException {
        prepareRoot();
        iterate();
        emitResult();
        finalizeTraversal();
    }

    // --- кроки алгоритму, які можуть перевизначати підкласи ---

    protected void prepareRoot() throws IOException {
        if (!Files.exists(root)) {
            throw new NoSuchFileException(root.toString());
        }
    }


    protected abstract void iterate() throws IOException;


    protected boolean shouldVisit(Path path) {
        return true;
    }


    protected void visit(Path path) {
        visited.add(path);
    }


    protected void emitResult() {
        System.out.println("Visited " + visited.size() + " items:");
        visited.forEach(System.out::println);
    }

    protected void finalizeTraversal() {

    }

    protected List<Path> listChildren(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) return List.of();
        try (var stream = Files.list(dir)) {
            return stream.toList();
        }
    }
}
