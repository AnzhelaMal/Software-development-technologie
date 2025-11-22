package ua.kpi.ia33.shellweb.patterns.prototype.fs.traversal;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

public class DfsTraversal extends AbstractTraversal {

    private final Deque<Path> stack = new ArrayDeque<>();

    public DfsTraversal(Path root) {
        super(root);
    }

    @Override
    protected void prepareRoot() throws IOException {
        super.prepareRoot();
        stack.clear();
        stack.push(root);
    }

    @Override
    protected void iterate() throws IOException {
        while (!stack.isEmpty()) {
            Path current = stack.pop();

            if (!shouldVisit(current)) {
                continue;
            }

            visit(current);

            // додаємо дітей у стек — вийде обхід у глибину
            var children = listChildren(current);
            for (Path child : children) {
                stack.push(child);
            }
        }
    }

    @Override
    protected void emitResult() {
        System.out.println("DFS traversal from " + root);
        super.emitResult();
    }
}
