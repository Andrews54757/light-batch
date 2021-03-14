package net.andrews.lightbatch;

import net.minecraft.util.thread.TaskExecutor;

public interface ServerLightingProviderInterface {
    public int getTaskBatchSize();
    public TaskExecutor<Runnable> getProcessor();
}
