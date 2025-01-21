package andrew.volostnykh.usefulutils.exceptions;

import andrew.volostnykh.usefulutils.exceptions.checked.CheckedConsumer;
import andrew.volostnykh.usefulutils.exceptions.checked.CheckedFunction;
import andrew.volostnykh.usefulutils.exceptions.checked.CheckedRunnable;
import andrew.volostnykh.usefulutils.exceptions.checked.CheckedSupplier;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SilentExceptions {

    // TODO: isThrown; callbacks; log modificator

    public void ignore(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error("Exception ignored", e);
        }
    }

    public <T> Optional<T> ignore(CheckedSupplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (Exception e) {
            log.error("Exception ignored", e);
        }

        return Optional.empty();
    }

    public <T, R> Optional<R> ignore(T arg, CheckedFunction<T, R> function) {
        try {
            return Optional.ofNullable(function.apply(arg));
        } catch (Exception e) {
            log.error("Exception ignored", e);
        }

        return Optional.empty();
    }

    public <T> void ignore(T arg, CheckedConsumer<T> consumer) {
        try {
            consumer.accept(arg);
        } catch (Exception e) {
            log.error("Exception ignored", e);
        }
    }
}
