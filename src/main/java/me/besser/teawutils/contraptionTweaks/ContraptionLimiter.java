package me.besser.teawutils.contraptionTweaks;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import me.besser.teawutils.util.TUConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContraptionLimiter {
    private static final Set<String> BANNED_NAMESPACES = new HashSet<>();
    private static final Set<ResourceLocation> BANNED_BLOCKS = new HashSet<>();
    private static final Map<ResourceLocation, Integer> BLOCK_LIMITS = new HashMap<>();

    public static void init() {
        // For hot reloading (the values are final, maybe fix that)
        BANNED_NAMESPACES.clear();
        BANNED_BLOCKS.clear();
        BLOCK_LIMITS.clear();

        // Namespaces
        for (String namespace : TUConfig.COMMON.bannedNamespaces.get()) {
            addBannedNamespace(namespace);
        }

        // Blocks
        for (String block : TUConfig.COMMON.bannedBlocks.get()) {
            addBannedBlock(block);
        }

        // Limited Blocks
        for (String entry : TUConfig.COMMON.blockLimits.get()) {
            try {
                String[] parts = entry.split("=");
                String id = parts[0];
                int limit = Integer.parseInt(parts[1]);

                addLimit(id, limit);
            } catch (Exception ignored) {}
        }
    }

    public static void validate(Contraption contraption, BlockPos anchorPos) throws AssemblyException {
        Map<ResourceLocation, Integer> currentCounts = new HashMap<>();

        for (StructureBlockInfo info : contraption.getBlocks().values()) {
            Block block = info.state().getBlock();
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

            // Check Banned Blocks
            if (BANNED_BLOCKS.contains(id) || BANNED_NAMESPACES.contains(id.getNamespace())) {
                BlockPos worldPos = anchorPos.offset(info.pos());
                throw AssemblyException.unmovableBlock(worldPos, info.state());
            }

            // Check Limited Blocks
            if (BLOCK_LIMITS.containsKey(id)) {
                int currentCount = currentCounts.getOrDefault(id, 0) + 1;
                if (currentCount > BLOCK_LIMITS.get(id)) {
                    throw new AssemblyException(
                            info.state().getBlock().getName()
                                    .copy()
                                    .append(" limit exceeded. The limit is ")
                                    .append(String.valueOf(BLOCK_LIMITS.get(id)))
                                    .append(".")
                    );
                }
                currentCounts.put(id, currentCount);
            }
        }
    }

    public static void addBannedNamespace(String namespace) {
        BANNED_NAMESPACES.add(namespace.toLowerCase());
    }

    public static void addBannedBlock(String id) {
        BANNED_BLOCKS.add(ResourceLocation.parse(id));
    }

    public static void addLimit(String id, int limit) {
        BLOCK_LIMITS.put(ResourceLocation.parse(id), limit);
    }
}