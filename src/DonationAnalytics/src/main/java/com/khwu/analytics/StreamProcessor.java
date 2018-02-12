package com.khwu.analytics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * The {@code StreamProcessor} class act as processor for processing
 * incoming streaming data.
 *
 * @author khwu
 */
public class StreamProcessor {

    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");
    private final String DELIMITER = "\\|";
    private final String ZIP_CODE_REGEX = "^\\d{5,}$";
    private final DonationDB donationDB;

    public StreamProcessor() {
        donationDB = new DonationDB();
    }

    public void startAnalysis(String inputDir, String percentileDir, String outputDir) {
        try (Stream<String> streamFromInput = Files.lines(Paths.get(inputDir));
             Scanner percentileScanner = new Scanner(Paths.get(percentileDir));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir))){

            final double percentile = percentileScanner.nextDouble();
            if (percentile > 100 || percentile <= 0) {
                throw new IllegalArgumentException("Incorrect percentile.");
            }
            // System.out.printf("Percentile is: %d%n", percentile);
            streamFromInput.map(line -> parseDonation(line, percentile))   // parse incoming donation data
                    .filter(Optional::isPresent)                           // filter out empty data
                    .map(Optional::get)                                    // transform back to Donation object
                    .peek(donationDB::addDonorToDB)                        // add donor to virtual database
                    .filter(donationDB::isRepeatedDonor)                   // include repeated donors
                    .peek(donationDB::saveDonationToDB)                    // save donation to virtual database
                    .forEach(donation -> donationDB.getSummary(donation)   // retrieve summary of donation
                            .ifPresent(summary -> writeToFile(writer, summary))   // write to file
                    );

        } catch (IOException e) {
            System.out.println("Problem handling files: " + e.getMessage());
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.println("Problem handling percentile file: " + e.getMessage());
        }
    }

    private void writeToFile(BufferedWriter writer, String summary) {
        try {
            writer.write(summary);
        } catch (IOException e) {
            System.out.println("Problem writing: " + summary);
        }
    }

    private Optional<Donation> parseDonation(String input, double percentile) {
        String[] inputSplits = input.split(DELIMITER);

        String cmteID = inputSplits[0];
        String name = inputSplits[7];
        String zipCode = inputSplits[10];
        String transactionDate = inputSplits[13];
        String transactionAMT = inputSplits[14];
        String otherId = inputSplits[15];

        // check if every input array has 21 fields
        // check if CMTE_ID, NAME, ZIP_CODE, OTHER_ID are empty or malformed
        if (inputSplits.length != 21 || cmteID.isEmpty() || name.isEmpty() ||
                !zipCode.matches(ZIP_CODE_REGEX) || !otherId.isEmpty()) {
            return Optional.empty();
        }

        // check if TRANSACTION_DT empty or malformed
        int year;
        try {
            LocalDate date = LocalDate.parse(transactionDate, DATE_FORMATTER);
            year = date.getYear();
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }

        // check if TRANSACTION_AMT is empty or not able to parse
        double amt;
        try {
            amt = Double.parseDouble(transactionAMT);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        Donor donor = new Donor(name, zipCode, otherId);
        Recipient recipient = new Recipient(cmteID, percentile);
        YearZipCode yearZipCode = new YearZipCode(year, zipCode.substring(0, 5));
        Donation donation = new Donation(donor, recipient, yearZipCode, amt);

        return Optional.of(donation);
    }
}
