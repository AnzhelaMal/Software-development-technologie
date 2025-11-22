package ua.kpi.ia33.shellweb.service;

import org.springframework.stereotype.Service;
import ua.kpi.ia33.shellweb.patterns.state.op.CancelledOperation;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntConsumer;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service // <-- робить це біном для автопідстановки в OperationController
public class DefaultFileOpsService implements FileOpsService {

    // ---------- COPY ----------
    @Override
    public void copy(Path src, Path dst, IntConsumer progress) throws Exception {
        if (Files.isDirectory(src)) {
            copyDirectory(src, dst, progress);
        } else {
            Files.createDirectories(dst.getParent() != null ? dst.getParent() : dst.toAbsolutePath().getParent());
            copyFileWithProgress(src, dst, progress);
        }
    }

    // ---------- MOVE ----------
    @Override
    public void move(Path src, Path dst, IntConsumer progress) throws Exception {
        // Проста стратегія: copy -> delete (дає реальний прогрес для різних дисків)
        copy(src, dst, progress);
        delete(src, p -> { /* нічого, або можна віддати прогрес окремо */ });
        progress.accept(100);
    }

    // ---------- DELETE ----------
    @Override
    public void delete(Path src, IntConsumer progress) throws Exception {
        if (!Files.exists(src)) { progress.accept(100); return; }

        // рахуємо кількість елементів для грубого відсотка
        final long total = countNodes(src);
        final AtomicLong done = new AtomicLong(0);

        Files.walkFileTree(src, new SimpleFileVisitor<>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws java.io.IOException {
                Files.deleteIfExists(file);
                tick(progress, done.incrementAndGet(), total);
                return FileVisitResult.CONTINUE;
            }
            @Override public FileVisitResult postVisitDirectory(Path dir, java.io.IOException exc) throws java.io.IOException {
                Files.deleteIfExists(dir);
                tick(progress, done.incrementAndGet(), total);
                return FileVisitResult.CONTINUE;
            }
        });
        progress.accept(100);
    }

    // ---------- helpers ----------
    private void copyDirectory(Path src, Path dst, IntConsumer progress) throws Exception {
        final long totalBytes = dirSize(src);
        final AtomicLong copied = new AtomicLong(0);

        Files.walkFileTree(src, new SimpleFileVisitor<>() {
            @Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws java.io.IOException {
                Path rel = src.relativize(dir);
                Path targetDir = dst.resolve(rel);
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws java.io.IOException {
                Path rel = src.relativize(file);
                Path out = dst.resolve(rel);
                try {
                    long n = copyFileWithProgress(file, out, p -> {
                        // пер-file прогрес; агрегуємо у bytes
                        // тут нічого не робимо, бо нижче рахуємо за фактом записаних байтів
                    });
                    long cur = copied.addAndGet(n);
                    tick(progress, cur, totalBytes);
                    return FileVisitResult.CONTINUE;
                } catch (CancelledOperation co) {
                    throw co;
                }
            }
        });
        progress.accept(100);
    }

    /** Копіює один файл і повертає кількість скопійованих байтів */
    private long copyFileWithProgress(Path src, Path dst, IntConsumer perChunkProgress) throws java.io.IOException {
        long total = Files.size(src);
        long written = 0;

        try (InputStream in = Files.newInputStream(src);
             OutputStream out = Files.newOutputStream(dst, CREATE, TRUNCATE_EXISTING)) {

            byte[] buf = new byte[1024 * 64];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
                written += n;
                int percent = percent(written, Math.max(1, total));
                // Дозволяємо зверху зупиняти через unchecked виняток
                perChunkProgress.accept(percent);
            }
        }
        return written;
        // перший рівень прогресу (агрегований) оновлюємо у copyDirectory вище
    }

    private long dirSize(Path root) throws java.io.IOException {
        final AtomicLong sum = new AtomicLong(0);
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!attrs.isDirectory()) sum.addAndGet(attrs.size());
                return FileVisitResult.CONTINUE;
            }
        });
        return Math.max(1, sum.get());
    }

    private long countNodes(Path root) throws java.io.IOException {
        final AtomicLong cnt = new AtomicLong(0);
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) { cnt.incrementAndGet(); return FileVisitResult.CONTINUE; }
            @Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) { cnt.incrementAndGet(); return FileVisitResult.CONTINUE; }
        });
        return Math.max(1, cnt.get());
    }

    private static void tick(IntConsumer progress, long done, long total) {
        int p = percent(done, total);
        progress.accept(p);
    }

    private static int percent(long done, long total) {
        if (total <= 0) return 0;
        long v = (done * 100L) / total;
        if (v < 0) v = 0;
        if (v > 100) v = 100;
        return (int) v;
    }
}
