package andrew.volostnykh.usefulutils.exceptions.checked;

public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}
