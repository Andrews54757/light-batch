package net.andrews.lightbatch.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.andrews.lightbatch.ServerLightingProviderInterface;
import net.minecraft.server.world.ServerLightingProvider;

@Mixin({ ServerLightingProvider.class })
public class ServerLightingProviderMixin implements ServerLightingProviderInterface {
    @Shadow @Final volatile int taskBatchSize;

    @Override
    public int getTaskBatchSize() {
        return taskBatchSize;
    }
}