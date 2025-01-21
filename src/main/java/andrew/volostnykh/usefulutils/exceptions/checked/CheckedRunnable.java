package andrew.volostnykh.usefulutils.exceptions.checked;

@FunctionalInterface
public interface CheckedRunnable {

    void run() throws Exception;
}
