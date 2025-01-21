package andrew.volostnykh.usefulutils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Wraps code safely in transactions and adds commit hooks for running
 * code after a successful commit. Do not create this object directly.
 * It must be autowired to function properly.
 */

@Slf4j
@Component
public class TransactionHandler {
    private final ThreadLocal<TransactionState> transactionState =
            ThreadLocal.withInitial(TransactionState::new);

    /**
     * Internal class for storing transaction state on the current thread.
     */
    protected static class TransactionState {
        protected Collection<Runnable> afterCommit = new ArrayList<>();
    }

    /**
     * Adds a runnable to the post transaction commit execution list.
     * All Runnable instances in this list will get executed after a
     * successful transaction commit.
     *
     * @param runnable the runnable to add
     */
    public void onCommit(Runnable runnable) {
        TransactionState state = transactionState.get();
        if (state.afterCommit.isEmpty() && isActive()) {
            TransactionSynchronizationManager
                    .registerSynchronization(new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            doRun();
                        }
                    });
        }

        if (isActive()) {
            state.afterCommit.add(runnable);
        } else {
            throw new IllegalStateException("onCommit used outside of a transaction.");
        }
    }

    /**
     * Runs all the on-commit actions. You should not normally call this
     * directly as it will be automatically called on commit. However,
     * sometimes it can be useful in tests to directly trigger this.
     */
    public void doRun() {
        TransactionState state = transactionState.get();
        state.afterCommit.forEach(r -> {
            try {
                r.run();
            } catch (Throwable t) {
                log.error("Post transaction commit failure: {}", t.getMessage(), t);
            }
        });
        state.afterCommit.clear();
    }

    protected boolean isActive() {
        return TransactionSynchronizationManager.isSynchronizationActive();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doInNewTransaction(Runnable runnable) {
        runnable.run();
    }

    @SneakyThrows
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T doInNewTransaction(Callable<T> callable) {
        return callable.call();
    }
}
