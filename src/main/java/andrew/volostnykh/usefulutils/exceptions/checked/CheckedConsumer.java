package andrew.volostnykh.usefulutils.exceptions.checked;

@FunctionalInterface
public interface CheckedConsumer<T> {

    void accept(T t) throws Exception;
}
