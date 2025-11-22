package ua.kpi.ia33.shellweb.patterns.prototype.fs.traversal;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

public class BfsTraversal extends AbstractTraversal {

    private final Queue<Path> queue = new ArrayDeque<>();

    public BfsTraversal(Path root) {
        super(root);
    }

    @Override
    protected void prepareRoot() throws IOException {
        super.prepareRoot();
        queue.clear();
        queue.add(root);
    }

    @Override
    protected void iterate() throws IOException {
        while (!queue.isEmpty()) {
            Path current = queue.remove();

            if (!shouldVisit(current)) {
                continue;
            }

            visit(current);

            for (Path child : listChildren(current)) {
                queue.add(child);
            }
        }
    }

    @Override
    protected void emitResult() {
        System.out.println("BFS traversal from " + root);
        super.emitResult();
    }
}
