package net.andrews.lightbatch.interfaces;

import net.minecraft.util.thread.TaskExecutor;

public interface ServerLightingProviderInterface {
    public int getTaskBatchSize();
    public TaskExecutor<Runnable> getProcessor();
    public int getCurrentTasks();
}
