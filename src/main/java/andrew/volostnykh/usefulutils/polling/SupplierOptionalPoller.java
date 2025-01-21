package andrew.volostnykh.usefulutils.polling;

import java.util.Optional;
import java.util.function.Supplier;

// back pressure unsafe
// synchronous
public class SupplierOptionalPoller<T> implements Poller<T> {

    private final Supplier<Optional<T>> supplier;
    // milliseconds
    private final long totalWaitingTime;
    // milliseconds
    private final long pollingDelay;

    public SupplierOptionalPoller(Supplier<Optional<T>> supplier, long totalWaitingTime, long pollingDelay) {
        this.supplier = supplier;
        this.totalWaitingTime = totalWaitingTime;
        this.pollingDelay = pollingDelay;
    }

    public SupplierOptionalPoller(Supplier<Optional<T>> supplier) {
        this(supplier, (long) 1000 * 60 * 5, 500);
    }

    public T poll() {
        for (long waitingTime = 0; waitingTime < totalWaitingTime; waitingTime += pollingDelay) {
            Optional<T> optionalResult = supplier.get();
            if (optionalResult.isPresent()) {
                return optionalResult.get();
            }

            try {
                Thread.sleep(pollingDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException("POLLING_INTERRUPTION_EXCEPTION", e);
            }
        }

        throw new RuntimeException("POLLING_TIME_OUT");
    }

}

