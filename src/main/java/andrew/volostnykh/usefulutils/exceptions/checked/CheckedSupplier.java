package andrew.volostnykh.usefulutils.exceptions.checked;

@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Exception;
}
