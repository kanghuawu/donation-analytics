package com.khwu.analytics;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The {@code Recipient} class represents the data type for the recipient of donations.
 * The donations statistics grouped by year and zip code, and are stored by
 * {@code Map<YearZipCode, Statistic> groupedStatistics}.
 *
 * @author khwu
 */
public class Recipient {

    private final Map<YearZipCode, Statistic> groupedStatistics;
    private final double percentile;

    @Getter
    private final String cmteID;

    public Recipient(String cmteID, double percentile) {
        this.cmteID = cmteID;
        this.percentile = percentile;
        this.groupedStatistics = new HashMap<>();
    }

    public void add(Donation donation) {
        if (donation == null) return;

        double amt = donation.getTransactionAMT();
        YearZipCode yearZipCode = donation.getYearZipCode();

        groupedStatistics.putIfAbsent(yearZipCode, new Statistic(percentile));
        Statistic statistics = groupedStatistics.get(yearZipCode);
        statistics.add(amt);
    }

    public Optional<Statistic> getStatistic(YearZipCode yearZipCode) {
        return Optional.of(groupedStatistics.get(yearZipCode));
    }
}
