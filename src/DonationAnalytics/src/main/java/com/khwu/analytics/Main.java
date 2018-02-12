package com.khwu.analytics;

public class Main {

    public static void main(String[] args) {
        if (args == null || args.length != 3) {
            System.out.println("Incorrect input. Expecting: java -cp donation-analytics.jar <input file> <percentile file> <output file>");
            return;
        }
//        long beforeTimestamp = start();
        StreamProcessor streamProcessor = new StreamProcessor();
        streamProcessor.startAnalysis(args[0], args[1], args[2]);
//        end(beforeTimestamp);
    }

    private static long start() {
        long beforeTimestamp = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("Used Memory before: %d MB%n", usedMemoryBefore/1_000_000);
        System.out.println("=== Start parsing file ===");
        return beforeTimestamp;
    }

    private static void end(long beforeTimestamp) {
        System.out.println("=== Complete parsing file ===");
        Runtime runtime = Runtime.getRuntime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.printf("Used Memory after: %d MB%n", usedMemoryAfter/1_000_000);
        long afterTimestamp = System.currentTimeMillis();
        System.out.printf("Total time used: %d ms%n", afterTimestamp - beforeTimestamp);
    }
}
