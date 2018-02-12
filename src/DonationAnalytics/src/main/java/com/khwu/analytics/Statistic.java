package com.khwu.analytics;

import lombok.Getter;

import java.util.Optional;

/**
 * The {@code Statistic} class represents the data type for storing percentile, sum and
 * total number of donors.
 *
 * @author khwu
 */
public class Statistic {
    private Percentile percentile;
    @Getter
    private double sum;
    @Getter
    private int numOfDonor;

    public Statistic(double percentile) {
        this.sum = 0.0;
        this.numOfDonor = 0;
        this.percentile = new Percentile(percentile);
    }

    public void add(double donation) {
        numOfDonor++;
        sum += donation;
        percentile.add(donation);
    }

    public Optional<Double> getPercentile() {
        return percentile.getPerNum();
    }
}
