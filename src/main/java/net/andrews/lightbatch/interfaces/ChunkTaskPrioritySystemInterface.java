package net.andrews.lightbatch.interfaces;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.server.world.LevelPrioritizedQueue;
import net.minecraft.util.Unit;
import net.minecraft.util.thread.MessageListener;

public interface ChunkTaskPrioritySystemInterface {
    Map<MessageListener<?>, LevelPrioritizedQueue<? extends Function<MessageListener<Unit>, ?>>> getQueues();
}
