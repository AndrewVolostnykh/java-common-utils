package andrew.volostnykh.usefulutils;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class Exceptions {

    public interface CheckedConsumer<T> {
        void accept(T arg) throws Exception;
    }

    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws Exception;
    }

    public static <T> Optional<T> ignore(CheckedSupplier<T> throwable) {
        try {
            return Optional.of(throwable.get());
        } catch (Exception e) {
            log.error("[EXCEPTIONS] Exception occurred", e);
            return Optional.empty();
        }
    }

    public static void ignore(CheckedRunnable throwable) {
        try {
            throwable.run();
        } catch (Exception e) {
            log.error("[EXCEPTIONS] Exception occurred", e);
        }
    }

    public static <T> Optional<T> ignoreNoLog(CheckedSupplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
