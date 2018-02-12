package com.khwu.analytics;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code Donor} class represents the donor of a given donation. It is
 * uniquely distinguished by its two fields: {@code name} and {@code zipCode}.
 * Therefore, two fields are used in {@code equals} and {@code hashCode} methods.
 *
 * @author khwu
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(exclude = "otherID")
public class Donor {
    private final String name;
    private final String zipCode;
    private final String otherID;
}
