package net.andrews.lightbatch.mixin;

import java.util.Queue;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.andrews.lightbatch.SimpleTaskQueueInterface;
import net.minecraft.util.thread.TaskQueue;

@Mixin(TaskQueue.Simple.class)
public class SimpleTaskQueueMixin<T> implements SimpleTaskQueueInterface {
    @Shadow Queue<T> queue;

    @Override
    public Queue<T> getQueue() {
        return queue;
    }
}
