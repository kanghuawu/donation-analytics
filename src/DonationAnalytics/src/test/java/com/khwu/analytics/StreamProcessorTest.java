package com.khwu.analytics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class StreamProcessorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    public void testStartAnalysisWithWrongInputFile() {
        String inputFile = "../../input/itcont.";
        String percentileFile = "../../input/percentile.txt";
        String outputFile = "../../output/repeat_donors.txt";
        StreamProcessor streamProcessor = new StreamProcessor();
        streamProcessor.startAnalysis(inputFile, percentileFile, outputFile);
        assertEquals(String.format("Problem handling files: %s%n", inputFile), outContent.toString());
    }

    @Test
    public void testStartAnalysisWithWrongPercentileFile() {
        String inputFile = "../../input/itcont.txt";
        String percentileFile = "../../input/percent.txt";
        String outputFile = "../../output/repeat_donors.txt";
        StreamProcessor streamProcessor = new StreamProcessor();
        streamProcessor.startAnalysis(inputFile, percentileFile, outputFile);
        assertEquals(String.format("Problem handling files: %s%n", percentileFile), outContent.toString());
    }

    @Test
    public void testParseDonationCorrectDonation() {
        String donation = "C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JAMES|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312018|384||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339";
        String cmteID = "C00384516";
        double percentile = 30.0;
        int year = 2018;
        String zipCode = "02895";
        double amt = 384;
        String donor = "SABOURIN, JAMES";

        Donation actual = testParseDonation(donation, percentile);
        assertEquals(cmteID, actual.getRecipient().getCmteID());
        assertEquals(percentile, actual.getRecipient().getPercentile(), 0);
        assertEquals(year, actual.getYearZipCode().getYear());
        assertEquals(zipCode, actual.getYearZipCode().getZipCode());
        assertEquals(amt, actual.getTransactionAMT(), 0);
        assertEquals(donor, actual.getDonor().getName());
        assertEquals(zipCode, actual.getDonor().getZipCode());
        assertEquals(Optional.empty(), actual.getRecipient().getStatistic(actual.getYearZipCode()));
    }

    @Test
    public void testParseDonationWrongDate() {
        String donation = "C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JAMES|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|013118|384||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339";
        String cmteID = "C00384516";
        double percentile = 30.0;
        int year = 2018;
        String zipCode = "02895";
        double amt = 384;
        String donor = "SABOURIN, JAMES";

        Donation actual = testParseDonation(donation, percentile);
        assertEquals(null, actual);
    }

    @Test
    public void testParseDonationEmptyName() {
        String donation = "C00384516|N|M2|P|201702039042410894|15|IND||LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312018|384||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339";
        String cmteID = "C00384516";
        double percentile = 30.0;
        int year = 2018;
        String zipCode = "02895";
        double amt = 384;
        String donor = "SABOURIN, JAMES";

        Donation actual = testParseDonation(donation, percentile);
        assertEquals(null, actual);
    }

    @Test
    public void testParseDonationEmptyCmteID() {
        String donation = "|N|M2|P|201702039042410894|15|IND|SABOURIN, JAMES|LOOKOUT MOUNTAIN|GA|028956146|UNUM|SVP, CORPORATE COMMUNICATIONS|01312018|384||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339";
        String cmteID = "C00384516";
        double percentile = 30.0;
        int year = 2018;
        String zipCode = "02895";
        double amt = 384;
        String donor = "SABOURIN, JAMES";

        Donation actual = testParseDonation(donation, percentile);
        assertEquals(null, actual);
    }

    @Test
    public void testParseDonationWrongZipCode() {
        String donation = "C00384516|N|M2|P|201702039042410894|15|IND|SABOURIN, JAMES|LOOKOUT MOUNTAIN|GA|0289|UNUM|SVP, CORPORATE COMMUNICATIONS|01312018|384||PR2283904845050|1147350||P/R DEDUCTION ($192.00 BI-WEEKLY)|4020820171370029339";
        String cmteID = "C00384516";
        double percentile = 30.0;
        int year = 2018;
        String zipCode = "02895";
        double amt = 384;
        String donor = "SABOURIN, JAMES";

        Donation actual = testParseDonation(donation, percentile);
        assertEquals(null, actual);
    }

    private Donation testParseDonation(String input, double percentile) {
        StreamProcessor streamProcessor = new StreamProcessor();
        try {
            Method method = streamProcessor.getClass().getDeclaredMethod("parseDonation", String.class, double.class);
            method.setAccessible(true);
            Optional<Donation> res = (Optional<Donation>) method.invoke(streamProcessor, input, percentile);
            if (res.isPresent()) {
                return res.get();
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}