package net.andrews.lightbatch.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.andrews.lightbatch.interfaces.LevelPrioritizedQueueInterface;
import net.minecraft.server.world.LevelPrioritizedQueue;

@Mixin(LevelPrioritizedQueue.class)
public class LevelPrioritizedQueueMixin<T> implements LevelPrioritizedQueueInterface {

    @Shadow List<Long2ObjectLinkedOpenHashMap<List<Optional<T>>>> levelToPosToElements;

    @Override
    public int getQueuedCount() {
        int count = 0;
        for (Long2ObjectLinkedOpenHashMap<List<Optional<T>>> map : levelToPosToElements) {
            for (List<Optional<T>> list : map.values()) {
                count += list.size();
            }
        }
        return count;
    }
    
}
