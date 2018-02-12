package com.khwu.analytics;

import lombok.*;

/**
 * j
 * @author khwu
 */
@Setter
@Getter
@AllArgsConstructor
public class Donation {
    private Donor donor;
    private Recipient recipient;
    private YearZipCode yearZipCode;
    private double transactionAMT;
}
