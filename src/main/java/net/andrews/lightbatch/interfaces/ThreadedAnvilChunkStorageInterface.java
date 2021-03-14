package net.andrews.lightbatch.interfaces;

import net.minecraft.server.world.ChunkTaskPrioritySystem;

public interface ThreadedAnvilChunkStorageInterface {
    public ChunkTaskPrioritySystem getTaskPrioritySystem();
}