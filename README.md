# LightBatch

Dumb mod to test different task batch sizes for light engine. Adds /lightbatch command. Higher batch numbers seem to let light engine keep up.

## Suspected Mechanism For Why It Works

Since the light engine runs on another thread, tasks must be sent to it. Here is how it works (simplified):

1. Queue tasks for light engine
2. If number of queued tasks is greater than maxBatchSize, run light engine
3. At every tick, run light engine if there are any tasks (and not busy).

When the light engine is actually run, here is what happens (also simplified):

1. Process light engine tasks, limited by maxBatchSize (won't process over 5 per run).
2. Run the light engine (do stuff with the new information given by the tasks).
3. Send the results back to the game thread.

Every time the light engine is run though, the light engine can only process 5 tasks at a time. To compensate, it can run multiple times per tick. One can observe this happening in-game using the `/light log command` with the LightYeet mod when piston machines are running - you will be inundated with numbers indicating the number of tasks scheduled per run, but they will never be above 5.

Light suppression happens when the light engine is overloaded, and can't handle the tasks as fast as they are given. Theoretically, this is not dependent on batch size, and the LightBatch mod shouldn't work because the sum of tasks is the same with/without batch limits.

However, it does work. How? Because of optimization/culling.

The light engine can somehow take the tasks given to it per run, and cull it.
IE, if you are moving lots of blocks, instead of propagating from every block to every block adjacent (flood fill algorithm), it can cull the extra unecessary updates.

However, not much is known exactly how this works. It probably has something to do with the `DynamicGraphMinFixedPoint.java` (yarn `LevelPropagator.java` code in the game.
