package me.besser.teawutils.registry;

import me.besser.teawutils.TEAWUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TEAWUtils.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TEAW_TAB =
            TABS.register("teaw_tab", () -> CreativeModeTab.builder()
                    .title(Component.literal("TEAW Utils")) // TODO: use lang
                    .icon(() -> new ItemStack(ModItems.NETHER_STAR_FRAME.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.NETHER_STAR_FRAME.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
