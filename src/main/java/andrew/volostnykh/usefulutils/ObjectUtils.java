package andrew.volostnykh.usefulutils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectUtils {

    public static <T, R> R getOfNullable(T object, Function<T, R> getter, R orElse) {
        if (object == null) {
            return orElse;
        }

        R got = getter.apply(object);

        if (got == null) {
            return orElse;
        }

        return got;
    }

}
