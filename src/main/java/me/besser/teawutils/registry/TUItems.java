package me.besser.teawutils.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import me.besser.teawutils.TEAWUtils;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TUItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TEAWUtils.MODID);

    public static final DeferredHolder<Item, Item> NETHER_STAR_FRAME =
            ITEMS.register("nether_star_frame",
                    () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> UNCOMPRESSED_NETHER_STAR =
            ITEMS.register("uncompressed_nether_star",
                    () -> new SequencedAssemblyItem(new Item.Properties()));    // A Create class that just extends Item

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
