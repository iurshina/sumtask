package com.company;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class UnsignedIntSum {

    private static final long CHUNK_SIZE = 1024L * 1024L;

    public static long sumUnsignedInts(String path) {
        long total = 0;

        try (FileChannel fc = FileChannel.open(Paths.get(path))) {
            final long size = fc.size();
            final int chunks = (int) ((size + (CHUNK_SIZE - 1)) / CHUNK_SIZE);

            for (int i = 0; i < chunks; i++) {
                final long curChunkSize = Math.min(CHUNK_SIZE, size - CHUNK_SIZE * i);
                final ByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, CHUNK_SIZE * i, curChunkSize)
                        .order(ByteOrder.LITTLE_ENDIAN);

                total += sum(mbb, curChunkSize);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return total;
    }

    public static long multiSumUnsignedInts(String path) {
        long total = 0;

        try (FileChannel fc = FileChannel.open(Paths.get(path))) {
            final long size = fc.size();
            final int chunks = (int) ((size + (CHUNK_SIZE - 1)) / CHUNK_SIZE);

            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
            Set<Future<Long>> futures = new HashSet<>();
            for (int i = 0; i < chunks; i++) {
                final long curChunkSize = Math.min(CHUNK_SIZE, size - CHUNK_SIZE * i);
                final ByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, CHUNK_SIZE * i, curChunkSize)
                        .order(ByteOrder.LITTLE_ENDIAN);

                futures.add(executor.submit(() -> sum(mbb, curChunkSize)));
            }

            for (Future<Long> f : futures) {
                total += f.get();
            }
        } catch (IOException | InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }

        return total;
    }

    private static long sum(final ByteBuffer mbb, final long size) {
        long total = 0;
        for (int i = 0; i < size / 4; i++) {
            total += ((long) mbb.getInt()) & 0xffffffffL;
        }

        return total;
    }
}