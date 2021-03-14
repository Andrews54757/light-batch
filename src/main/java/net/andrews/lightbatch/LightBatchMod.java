package net.andrews.lightbatch;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;

import java.util.Iterator;
import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.andrews.lightbatch.interfaces.ChunkTaskPrioritySystemInterface;
import net.andrews.lightbatch.interfaces.LevelPrioritizedQueueInterface;
import net.andrews.lightbatch.interfaces.ServerLightingProviderInterface;
import net.andrews.lightbatch.interfaces.SimpleTaskQueueInterface;
import net.andrews.lightbatch.interfaces.ThreadedAnvilChunkStorageInterface;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.LevelPrioritizedQueue;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Unit;
import net.minecraft.util.thread.MessageListener;

public class LightBatchMod {

  public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("light")
        .then(CommandManager.literal("batch")
            .then(CommandManager.literal("get").then(
                argument("dimension", DimensionArgumentType.dimension()).executes(LightBatchMod::getBatchSizeCommand)))
            .then(CommandManager.literal("set")
                .then(argument("dimension", DimensionArgumentType.dimension())
                    .then(argument("batch size", integer(1, 1000000)).executes(LightBatchMod::setBatchSizeCommand)))))
        .then(CommandManager.literal("status").executes(LightBatchMod::statusCommand)));
  }

  private static int statusCommand(CommandContext<ServerCommandSource> ctx) {

    MinecraftServer server = ctx.getSource().getMinecraftServer();
    Iterator<ServerWorld> iterator = server.getWorlds().iterator();
    sendMessage(ctx.getSource(), "Queued Tasks:");
    while (iterator.hasNext()) {
      ServerWorld serverWorld = iterator.next();
      ServerLightingProviderInterface lightProvider = (ServerLightingProviderInterface) serverWorld
          .getLightingProvider();
      SimpleTaskQueueInterface queue = (SimpleTaskQueueInterface) lightProvider.getProcessor().queue;
      int backed = 0;

      ThreadedAnvilChunkStorageInterface storage = (ThreadedAnvilChunkStorageInterface) serverWorld
          .getChunkManager().threadedAnvilChunkStorage;
      ChunkTaskPrioritySystemInterface system = (ChunkTaskPrioritySystemInterface) storage.getTaskPrioritySystem();
      LevelPrioritizedQueue<? extends Function<MessageListener<Unit>, ?>> lvlqueue = null;
      for (LevelPrioritizedQueue<? extends Function<MessageListener<Unit>, ?>> lvlq : system.getQueues().values()) {
        if (lvlq.toString().indexOf("light_queue") != -1) {
          lvlqueue = lvlq;
          break;
        }
      }

      if (lvlqueue != null) {
        backed = ((LevelPrioritizedQueueInterface) lvlqueue).getQueuedCount();
      }

      sendMessage(ctx.getSource(), serverWorld.getRegistryKey().getValue().getPath() + ": "
          + lightProvider.getCurrentTasks() + "/" + queue.getQueue().size() + "/" + backed);
    }
    return 1;
  }

  private static void sendMessage(ServerCommandSource source, String str) {
    source.sendFeedback(new LiteralText(str), false);
  }

  private static int getBatchSizeCommand(CommandContext<ServerCommandSource> ctx) {

    try {
      ServerWorld dimension = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
      ServerLightingProvider lightProvider = dimension.getChunkManager().getLightingProvider();
      int batchSize = ((ServerLightingProviderInterface) lightProvider).getTaskBatchSize();
      sendMessage(ctx.getSource(),
          "Batch Size for " + dimension.getRegistryKey().getValue().getPath() + " is " + batchSize);
    } catch (Exception e) {
      sendMessage(ctx.getSource(), "An error has occured");
    }

    return 1;
  }

  private static int setBatchSizeCommand(CommandContext<ServerCommandSource> ctx) {
    try {
      ServerWorld dimension = DimensionArgumentType.getDimensionArgument(ctx, "dimension");
      int batchSize = getInteger(ctx, "batch size");
      dimension.getChunkManager().getLightingProvider().setTaskBatchSize(batchSize);
      sendMessage(ctx.getSource(),
          "Batch Size for " + dimension.getRegistryKey().getValue().getPath() + " set to " + batchSize);
    } catch (Exception e) {
      sendMessage(ctx.getSource(), "An error has occured");
    }

    return 1;
  }

  public static void init() {
    CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
      registerCommands(dispatcher);
    });
  }

}
