package net.andrews.lightbatch;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Messenger;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;

import static net.minecraft.server.command.CommandManager.argument;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

public class LightBatchExtension implements CarpetExtension {

  static {
    CarpetServer.manageExtension(new LightBatchExtension());
  }

  public void onGameStarted() {
  }

  public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("lightbatch")
        .then(CommandManager.literal("get").then(argument("dimension", DimensionArgumentType.dimension()).executes(LightBatchExtension::getBatchSizeCommand)))
        .then(CommandManager.literal("set").then(argument("dimension", DimensionArgumentType.dimension()).then(argument("batch size", integer(1,1000000)).executes(LightBatchExtension::setBatchSizeCommand)))));
  }

  private static int getBatchSizeCommand(CommandContext<ServerCommandSource> ctx) {
    
    try {
    ServerWorld dimension = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
    ServerLightingProvider lightProvider = dimension.getChunkManager().getLightingProvider();
    int batchSize = ((ServerLightingProviderInterface) lightProvider).getTaskBatchSize();
    Messenger.m(ctx.getSource(), new Object[] { "w Batch Size for " + dimension.getDimension().toString() + " is " + batchSize});
    } catch (Exception e) {
      Messenger.m(ctx.getSource(), new Object[] { "w An error has occured"});
    }
    
    return 1;
  }

  private static int setBatchSizeCommand(CommandContext<ServerCommandSource> ctx) {
    try {
    ServerWorld dimension = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
    int batchSize = getInteger(ctx, "batch size");
    dimension.getChunkManager().getLightingProvider().setTaskBatchSize(batchSize);
    Messenger.m(ctx.getSource(), new Object[] { "w Batch Size for " + dimension.getDimension().toString() + " set to " + batchSize});
    } catch (Exception e) {
      Messenger.m(ctx.getSource(), new Object[] { "w An error has occured"});
    }
    
    return 1;
  }

  public static void init() {

  }

}
