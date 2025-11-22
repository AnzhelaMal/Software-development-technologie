package ua.kpi.ia33.shellweb.patterns.prototype.fs.traversal;

import java.io.IOException;
import java.nio.file.Path;

public class TraversalDemo {

    public static void main(String[] args) throws IOException {
        Path root = Path.of("C:/temp"); // або будь-яка тестова папка

        AbstractTraversal bfs = new BfsTraversal(root);
        bfs.traverse();

        System.out.println("----");

        AbstractTraversal dfs = new DfsTraversal(root);
        dfs.traverse();
    }
}
