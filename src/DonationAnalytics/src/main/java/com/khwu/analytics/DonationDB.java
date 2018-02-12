package com.khwu.analytics;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * The {@code DonationDB} class act as data storage which maintains two data structure
 * {@code Map<Integer, Set<Donor>> donors} and {@code Map<String, Recipient> recipients}.
 * Repeated {@code Donor} objects can be searched from {@code donors} with given year.
 * Whereas, {@code Recipient} objects can be searched from {@code recipients} with its
 * {@code cmteID}.
 *
 * @author khwu
 */
public class DonationDB {

    private final Map<Integer, Set<Donor>> donors;
    private final Map<String, Recipient> recipients;


    public DonationDB() {
        this.donors = new HashMap<>();
        this.recipients = new HashMap<>();
    }

    public Optional<String> getSummary(Donation donation) {
        String cmteID = donation.getRecipient().getCmteID();

        YearZipCode yearZipCode = donation.getYearZipCode();

        if (!recipients.containsKey(cmteID)) return Optional.empty();

        Optional<Statistic> statisticOptional = recipients.get(cmteID).getStatistic(donation.getYearZipCode());

        if (statisticOptional.isPresent()) {
            Statistic statistic = statisticOptional.get();
            if (statistic.getPercentile().isPresent()) {
                /*
                 C00384516|02895  |2018|333    |333|1
                 cmteID   |zipCode|year|per Num|sum|num of donor
                 */
                String res = String.format("%s|%s|%d|%.0f|%.0f|%d%n",
                        cmteID, yearZipCode.getZipCode(), yearZipCode.getYear(),
                        statistic.getPercentile().get(), statistic.getSum(), statistic.getNumOfDonor());
                return Optional.of(res);
            }
        }
        return Optional.empty();
    }

    public void saveDonationToDB(Donation donation) {
        if (donation == null) return;
        Recipient currRecipient = donation.getRecipient();
        recipients.putIfAbsent(currRecipient.getCmteID(), currRecipient);
        Recipient recipient = recipients.get(currRecipient.getCmteID());
        recipient.add(donation);

        int year = donation.getYearZipCode().getYear();
        donors.putIfAbsent(year, new HashSet<>());
        donors.get(year).add(donation.getDonor());
    }

    public boolean isRepeatedDonor(Donation donation) {
        if (donation == null) return false;
        Donor donor = donation.getDonor();
        int currYear = donation.getYearZipCode().getYear();
        return donors.get(currYear - 1) != null && donors.get(currYear - 1).contains(donor);
    }

    public void addDonorToDB(Donation donation) {
        if (donation == null) return ;
        Donor donor = donation.getDonor();
        int currYear = donation.getYearZipCode().getYear();
        donors.putIfAbsent(currYear, new HashSet<>());
        donors.get(currYear).add(donor);
    }
}
