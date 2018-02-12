package com.khwu.analytics;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * s
 * @author khwu
 */
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(exclude = "otherID")
public class Donor {
    private final String name;
    private final String zipCode;
    private final String otherID;
}
