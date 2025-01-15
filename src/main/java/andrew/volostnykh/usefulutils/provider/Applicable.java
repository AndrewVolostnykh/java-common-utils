package andrew.volostnykh.usefulutils.provider;

public interface Applicable<T> {

    boolean isApplicableTo(T arg);

}
