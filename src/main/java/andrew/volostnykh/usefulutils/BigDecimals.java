package andrew.volostnykh.usefulutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BigDecimals {

    public static boolean isNegative(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean moreThan(BigDecimal value, BigDecimal than) {
        if (value == null || than == null) {
            throw new IllegalArgumentException("Comparable value cannot be null");
        }

        return value.compareTo(than) > 0;
    }

    public static boolean equalsOrMoreThan(BigDecimal value, BigDecimal than) {
        if (value == null || than == null) {
            throw new IllegalArgumentException("Comparable value cannot be null");
        }

        return value.compareTo(than) >= 0;
    }

    public static boolean equalsOrLessThan(BigDecimal value, BigDecimal than) {
        if (value == null || than == null) {
            throw new IllegalArgumentException("Comparable value cannot be null");
        }

        return value.compareTo(than) <= 0;
    }

    public static boolean lessThan(BigDecimal value, BigDecimal than) {
        if (value == null || than == null) {
            throw new IllegalArgumentException("Comparable value cannot be null");
        }

        return value.compareTo(than) < 0;
    }

}
