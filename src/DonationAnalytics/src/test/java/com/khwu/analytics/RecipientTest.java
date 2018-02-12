package com.khwu.analytics;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecipientTest {

    @Test
    public void testStatistic() {
        Recipient r = new Recipient("ABC", 30);
        Donor d = new Donor("d", "12345", "");
        YearZipCode yearZipCode1 = new YearZipCode(2017, "12345");
        Donation donation1 = new Donation(d, r, yearZipCode1, 313);

        YearZipCode yearZipCode2 = new YearZipCode(2017, "12345");
        Donation donation2 = new Donation(d, r, yearZipCode2, 384);

        r.add(donation1);
        r.add(donation2);
        assertEquals(313, r.getStatistic(yearZipCode1).get().getPercentile().get(), 0);
        assertEquals(2, r.getStatistic(yearZipCode1).get().getNumOfDonor(), 0);
        assertEquals(313 + 384, r.getStatistic(yearZipCode1).get().getSum(), 0);
    }
}