package andrew.volostnykh.usefulutils.provider;


import java.util.List;

public abstract class AbstractBeansProvider<T, R extends Applicable<T>> {

    protected abstract List<R> getApplicableBeans();

    public R findBy(T arg) {
        return getApplicableBeans().stream()
                .filter(validator -> validator.isApplicableTo(arg))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Could not find required provider %s", arg)
                        )
                );
    }
}
