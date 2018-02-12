package com.khwu.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * The {@code YearZipCode} are bundled together so that two fields {@code year} and
 * {@code zipCode} can act as one lookup field.
 *
 * @author khwu
 */
@Data
@AllArgsConstructor
public class YearZipCode {
    private int year;
    private String zipCode;
}
