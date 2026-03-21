//package me.besser.teawutils.registry;
//
//import me.besser.teawutils.TEAWUtils;
//import me.besser.teawutils.block.ModBlockEntities;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.capabilities.Capabilities;
//import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
//
//// TODO: fix deprecated objects
//@EventBusSubscriber(modid = TEAWUtils.MODID, bus = EventBusSubscriber.Bus.MOD)
//public class ModCapabilities {
//    @SubscribeEvent
//    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
//        event.registerBlockEntity(
//                Capabilities.ItemHandler.BLOCK,
//                ModBlockEntities.WAR_MONITOR_BE.get(),
//                (be, side) -> be
//        );
//    }
//}