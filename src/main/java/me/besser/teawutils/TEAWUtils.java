package me.besser.teawutils;

import com.mojang.logging.LogUtils;
import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import me.besser.teawutils.registry.ModCreativeTabs;
import me.besser.teawutils.registry.ModItems;
import me.besser.teawutils.util.TUConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(TEAWUtils.MODID)
public class TEAWUtils {
    public static final String MODID = "teawutils";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TEAWUtils(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, TUConfig.SPEC);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);


        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);


        modEventBus.addListener((ModConfigEvent.Loading event) -> {
            if (event.getConfig().getSpec() == TUConfig.SPEC) {
                ContraptionLimiter.reloadConfig();
            }
        });

        modEventBus.addListener((ModConfigEvent.Reloading event) -> {
            if (event.getConfig().getSpec() == TUConfig.SPEC) {
                ContraptionLimiter.reloadConfig();
            }
        });


        LOGGER.info("TEAW Utils started!");
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
