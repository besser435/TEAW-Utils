package me.besser.teawutils;

import com.mojang.logging.LogUtils;
import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import me.besser.teawutils.opac.PartyClaimListener;
import me.besser.teawutils.registry.TUCreativeTabs;
import me.besser.teawutils.registry.TUItems;
import me.besser.teawutils.tweaks.MiscTweaks;
import me.besser.teawutils.util.TEAWUtilsConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;
import xaero.pac.common.server.api.OpenPACServerAPI;

@Mod(TEAWUtils.MODID)
public class TEAWUtils {
    public static final String MODID = "teawutils";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static String VERSION;

    public TEAWUtils(IEventBus modEventBus, ModContainer modContainer) {
        VERSION = modContainer.getModInfo().getVersion().toString();

        modContainer.registerConfig(ModConfig.Type.COMMON, TEAWUtilsConfig.SPEC);
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(this::onServerStarted);



        TUItems.register(modEventBus);
        TUCreativeTabs.register(modEventBus);
//        ModBlocks.BLOCKS.register(modEventBus);
//        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
//        modEventBus.addListener(this::registerCapabilities);


        // Initial Config Load
        modEventBus.addListener((ModConfigEvent.Loading event) -> {
            if (event.getConfig().getSpec() == TEAWUtilsConfig.SPEC) {
                ContraptionLimiter.reloadConfig();
                MiscTweaks.reloadConfig();
                PartyClaimListener.reloadConfig();
            }
        });

        // Config Reload
        modEventBus.addListener((ModConfigEvent.Reloading event) -> {
            if (event.getConfig().getSpec() == TEAWUtilsConfig.SPEC) {
                ContraptionLimiter.reloadConfig();
                MiscTweaks.reloadConfig();
                PartyClaimListener.reloadConfig();
            }
        });


        // Gameplay tweaks listeners
        NeoForge.EVENT_BUS.register(MiscTweaks.class);


        LOGGER.info("TEAW Utils v{} started!", VERSION);
    }

//    private void registerCapabilities(RegisterCapabilitiesEvent event) {
//        event.registerBlockEntity(
//                Capabilities.ItemHandler.BLOCK,
//                ModBlockEntities.WAR_MONITOR_BE.get(),
//                (be, side) -> be // This works because your BE implements IItemHandler
//        );
//    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        OpenPACServerAPI.get(event.getServer())
                .getServerClaimsManager()
                .getActionListenerManager()
                .register(new PartyClaimListener());
    }
}
