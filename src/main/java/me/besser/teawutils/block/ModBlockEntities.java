//package me.besser.teawutils.block;
//
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//public class ModBlockEntities {
//    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
//            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "teaw_utils");
//
//    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WarMonitorBlockEntity>> WAR_MONITOR_BE =
//            BLOCK_ENTITIES.register("war_monitor_be", () ->
//                    BlockEntityType.Builder.of(WarMonitorBlockEntity::new, ModBlocks.WAR_MONITOR.get()).build(null)
//            );
//}