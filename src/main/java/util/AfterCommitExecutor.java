package util;

import jakarta.transaction.Synchronization;

public class AfterCommitExecutor implements Synchronization {

    private final Runnable action;

    public AfterCommitExecutor(Runnable action) {
        this.action = action;
    }

    @Override
    public void beforeCompletion() {
        // No action required before completion
    }

    @Override
    public void afterCompletion(int status) {
        if (status == jakarta.transaction.Status.STATUS_COMMITTED) {
            action.run();
        }
    }
}

