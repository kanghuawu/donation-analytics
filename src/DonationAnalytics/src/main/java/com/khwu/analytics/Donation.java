package com.khwu.analytics;

import lombok.*;

/**
 * The {@code Donation} class represents data type for incoming donation
 * streaming data. All arguments {@code donor}, {@code recipient}, {@code yearZipCode}
 * and {@code transactionAMT} are required for initializing an object.
 *
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
