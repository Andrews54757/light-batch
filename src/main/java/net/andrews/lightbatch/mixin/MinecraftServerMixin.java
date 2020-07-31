package net.andrews.lightbatch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.andrews.lightbatch.LightBatchExtension;
import net.minecraft.server.MinecraftServer;

@Mixin({ MinecraftServer.class })
public class MinecraftServerMixin {
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void loadMe(CallbackInfo ci) {
        LightBatchExtension.init();
    }
}