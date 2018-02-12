package com.khwu.analytics;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DonationDBTest {

    private Donor donor1;
    private Donor donor2;
    private YearZipCode yearZipCode1;
    private YearZipCode yearZipCode2;
    private Recipient recipient1;
    private Recipient recipient2;
    private Donation donation1;
    private Donation donation2;
    private Donation donation3;
    private final DonationDB donationDB = new DonationDB();

    @Before
    public void setUpDB() {
        String name = "abc";
        String zipCode = "abcde";
        int year1 = 2018;
        int year2 = 2019;
        String cmteID = "def";

        donor1 = new Donor(name, zipCode, "");
        donor2 = new Donor(name, zipCode, "");
        yearZipCode1 = new YearZipCode(year1, zipCode);
        yearZipCode2 = new YearZipCode(year2, zipCode);
        recipient1 = new Recipient(cmteID, 30);
        recipient2 = new Recipient(cmteID, 30);
        donation1 = new Donation(donor1, recipient1, yearZipCode1, 313);
        donation2 = new Donation(donor1, recipient1, yearZipCode2, 384);
        donation3 = new Donation(donor1, recipient1, yearZipCode1, 384);
    }

    @Test
    public void testAddAndCheckIsInLastYearOnce() {
        assertFalse(donationDB.isRepeatedDonor(donation1));
    }

    @Test
    public void testAddAndCheckIsInLastYearTwice() {
        assertFalse(donationDB.isRepeatedDonor(donation1));
        donationDB.addDonorToDB(donation1);
        assertTrue(donationDB.isRepeatedDonor(donation2));
    }

    @Test
    public void testSubmitDonationOnce() {
        donationDB.addDonorToDB(donation1);
        donationDB.addDonorToDB(donation1);
        donationDB.saveDonationToDB(donation1);
        String res = "def|abcde|2018|313|313|1\n";
        assertEquals(res, donationDB.getSummary(donation1).get());
    }

    @Test
    public void testSubmitDonationTwice() {
        donationDB.addDonorToDB(donation1);
        donationDB.saveDonationToDB(donation1);
        donationDB.saveDonationToDB(donation3);
        String res = "def|abcde|2018|313|697|2\n";
        assertEquals(res, donationDB.getSummary(donation3).get());
    }
}