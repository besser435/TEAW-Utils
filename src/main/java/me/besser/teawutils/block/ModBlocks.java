//package me.besser.teawutils.block;
//
//import me.besser.teawutils.registry.TUItems;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.material.MapColor;
//import net.neoforged.neoforge.registries.DeferredHolder;
//import net.neoforged.neoforge.registries.DeferredRegister;
//
//public class ModBlocks {
//    public static final DeferredRegister.Blocks BLOCKS =
//            DeferredRegister.createBlocks("teaw_utils");
//
//    public static final DeferredHolder<Block, WarMonitorBlock> WAR_MONITOR =
//            BLOCKS.register("war_monitor", () -> new WarMonitorBlock(
//                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0f)
//            ));
//
//    public static final DeferredHolder<Item, BlockItem> WAR_MONITOR_ITEM =
//            TUItems.ITEMS.register("war_monitor", () -> new BlockItem(WAR_MONITOR.get(), new Item.Properties()));
//}