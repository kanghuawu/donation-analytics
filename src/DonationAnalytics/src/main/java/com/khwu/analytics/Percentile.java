package com.khwu.analytics;

import java.util.*;

/**
 * The {@code Percentile} class represents the data structure for calculating
 * nearest-rank percentile efficiently. The definition of nearest-rank percentile
 * can be found from here: https://en.wikipedia.org/wiki/Percentile.
 *
 * @author khwu
 */
public class Percentile {

    private final PriorityQueue<Double> smallTree;
    private final PriorityQueue<Double> bigTree;
    private final double percentile;
    private int size;

    public Percentile(double percentile) {
        if (percentile < 1 || percentile > 100) {
            throw new IllegalArgumentException("Invalid percentile: " + percentile);
        }
        this.percentile = percentile;
        this.smallTree = new PriorityQueue<>(Comparator.reverseOrder());
        this.bigTree  = new PriorityQueue<>();
        size = 0;
    }

    public void add(double num) {
        int nxtPerIdx = this.getNxtPerIdx();
        if (!smallTree.isEmpty() && smallTree.peek() > num) {
            smallTree.add(num);
            if (smallTree.size() > nxtPerIdx + 1) {
                bigTree.add(smallTree.poll());
            }
        } else {
            bigTree.add(num);
            if (smallTree.size() < nxtPerIdx + 1) {
                smallTree.add(bigTree.poll());
            }
        }
    }

    public int getPerIdx() {
        if (size == 0) {
            return -1;
        }
        return (int) Math.ceil(percentile / (double) 100 * size) - 1;
    }

    private int getNxtPerIdx() {
        size++;
        return this.getPerIdx();
    }

    public Optional<Double> getPerNum() {
        return Optional.of(smallTree.peek());
    }

    public int size() {
        return size;
    }
}
