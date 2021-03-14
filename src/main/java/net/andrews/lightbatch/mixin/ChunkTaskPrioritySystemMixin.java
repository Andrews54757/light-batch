package net.andrews.lightbatch.mixin;

import java.util.Map;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.andrews.lightbatch.interfaces.ChunkTaskPrioritySystemInterface;
import net.minecraft.server.world.ChunkTaskPrioritySystem;
import net.minecraft.server.world.LevelPrioritizedQueue;
import net.minecraft.util.Unit;
import net.minecraft.util.thread.MessageListener;

@Mixin(ChunkTaskPrioritySystem.class)
public class ChunkTaskPrioritySystemMixin implements ChunkTaskPrioritySystemInterface {
    @Shadow Map<MessageListener<?>, LevelPrioritizedQueue<? extends Function<MessageListener<Unit>, ?>>> queues;
    @Override
    public Map<MessageListener<?>, LevelPrioritizedQueue<? extends Function<MessageListener<Unit>, ?>>> getQueues() {
     
        return queues;
    }
    
}
