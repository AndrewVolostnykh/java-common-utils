package andrew.volostnykh.usefulutils.polling;

@FunctionalInterface
public interface Poller<T> {
    T poll();
}
