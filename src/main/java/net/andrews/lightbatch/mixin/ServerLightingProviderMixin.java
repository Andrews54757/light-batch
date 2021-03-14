package net.andrews.lightbatch.mixin;

import com.mojang.datafixers.util.Pair;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.andrews.lightbatch.interfaces.ServerLightingProviderInterface;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.util.thread.TaskExecutor;

@Mixin({ ServerLightingProvider.class })
public class ServerLightingProviderMixin implements ServerLightingProviderInterface {
    @Shadow @Final volatile int taskBatchSize;
    @Shadow @Final TaskExecutor<Runnable> processor;
    @Shadow @Final ObjectList pendingTasks;
    @Override
    public int getTaskBatchSize() {
        return taskBatchSize;
    }

    @Override
    public TaskExecutor<Runnable> getProcessor() {
        return processor;
    }

    @Override
    public int getCurrentTasks() {
        return pendingTasks.size();
    }
}