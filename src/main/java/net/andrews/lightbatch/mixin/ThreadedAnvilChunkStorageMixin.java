package net.andrews.lightbatch.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.andrews.lightbatch.interfaces.ThreadedAnvilChunkStorageInterface;
import net.minecraft.server.world.ChunkTaskPrioritySystem;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin implements ThreadedAnvilChunkStorageInterface {
    @Shadow @Final ChunkTaskPrioritySystem chunkTaskPrioritySystem;

    @Override
    public ChunkTaskPrioritySystem getTaskPrioritySystem() {
        return chunkTaskPrioritySystem;
    }
}
