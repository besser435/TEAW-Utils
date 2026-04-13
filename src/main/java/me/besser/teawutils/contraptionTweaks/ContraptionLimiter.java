package me.besser.teawutils.contraptionTweaks;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import me.besser.teawutils.TEAWUtils;
import me.besser.teawutils.util.TEAWUtilsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import java.util.*;

public class ContraptionLimiter {
    private static final Map<Category, Set<ResourceLocation>> BANNED_BLOCKS = new HashMap<>();
    private static final Map<Category, Set<String>> BANNED_MODS = new HashMap<>();
    private static final Map<Category, Map<ResourceLocation, Integer>> BLOCK_LIMITS = new HashMap<>();

    // Kind of goofy we set config defaults here, but whatever.
    public enum Category {
        ALL("All contraption types",
                List.of(), List.of(), List.of("create:mechanical_drill=320")),

        CART("Minecart Contraptions",
                List.of("createbigcannons"), List.of(), List.of()),

        TRAIN("Train Contraptions",
                List.of(), List.of(), List.of()),

        LINEAR("Gantry Carriage and Mechanical Piston Contraptions",
                List.of(), List.of(), List.of()),

        PULLEY("Rope Pulley and Elevator Contraptions",
                List.of(), List.of(), List.of()),

        ROTATIONAL("Mechanical, Windmill, and Clockwork Bearing Contraptions",
                List.of(), List.of(), List.of());

        private final String description;
        private final List<String> defaultMods;
        private final List<String> defaultBlocks;
        private final List<String> defaultLimits;

        Category(String description,
                 List<String> defaultMods, List<String> defaultBlocks, List<String> defaultLimits) {
            this.description = description;
            this.defaultMods = defaultMods;
            this.defaultBlocks = defaultBlocks;
            this.defaultLimits = defaultLimits;
        }

        // Getters
        public String getDescription() { return description; }
        public List<String> getDefaultMods() { return defaultMods; }
        public List<String> getDefaultBlocks() { return defaultBlocks; }
        public List<String> getDefaultLimits() { return defaultLimits; }
    }

    public static void reloadConfig() {
        BANNED_BLOCKS.clear();
        BANNED_MODS.clear();
        BLOCK_LIMITS.clear();

        for (Category category : Category.values()) {
            TEAWUtilsConfig.Common.CategoryConfig catConfig = TEAWUtilsConfig.COMMON.categories.get(category);
            if (catConfig == null) continue;

            catConfig.bannedMods.get().forEach(modId -> addBannedMod(category, modId));

            catConfig.bannedBlocks.get().forEach(blockId -> {
                if (ResourceLocation.tryParse(blockId) != null) {
                    addBannedBlock(category, blockId);
                }
            });

            catConfig.blockLimits.get().forEach(entry -> {
                String[] parts = entry.split("=");
                if (parts.length == 2) {
                    String id = parts[0].trim();
                    if (ResourceLocation.tryParse(id) != null) {
                        try {
                            int limit = Integer.parseInt(parts[1].trim());
                            addLimit(category, id, limit);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            });
        }

        TEAWUtils.LOGGER.info("Loaded configuration");
    }

    public static void validate(Contraption contraption, BlockPos anchorPos, Category category) throws AssemblyException {
        Map<ResourceLocation, Integer> currentCounts = new HashMap<>();

        // Probably slow, but the max contraption size is only 2048 blocks.
        for (StructureBlockInfo info : contraption.getBlocks().values()) {
            Block block = info.state().getBlock();
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

            // Check Bans
            if (isBanned(id, Category.ALL) || isBanned(id, category)) {
                throw AssemblyException.unmovableBlock(anchorPos.offset(info.pos()), info.state());
            }

            // Increment count for block limits
            int count = currentCounts.getOrDefault(id, 0) + 1;
            currentCounts.put(id, count);

            // Check limits
            checkLimit(id, info, count, Category.ALL);
            checkLimit(id, info, count, category);
        }
    }

    private static void checkLimit(ResourceLocation id, StructureBlockInfo info, int currentCount, Category category)
            throws AssemblyException {

        Map<ResourceLocation, Integer> typeLimits = BLOCK_LIMITS.get(category);

        if (typeLimits != null && typeLimits.containsKey(id)) {
            int limit = typeLimits.get(id);

            if (currentCount > limit) {
                String categoryName = category.name().toLowerCase();

                throw new AssemblyException(
                        info.state().getBlock().getName().copy()
                        // categoryName is import for UX, as some things are allowed on other kinds of contraptions.
                        .append(" limit exceeded for " + categoryName + " contraptions.")
                        .append(" The limit is: " + limit));
            }
        }
    }

    private static boolean isBanned(ResourceLocation id, Category category) {
        if (BANNED_BLOCKS.getOrDefault(category, Set.of()).contains(id)) return true;
        if (BANNED_MODS.getOrDefault(category, Set.of()).contains(id.getNamespace())) return true;

        return false;
    }

    public static void addBannedMod(Category category, String modId) {
        BANNED_MODS.computeIfAbsent(category, k -> new HashSet<>()).add(modId);
    }

    public static void addBannedBlock(Category category, String id) {
        BANNED_BLOCKS.computeIfAbsent(category, k -> new HashSet<>()).add(ResourceLocation.parse(id));
    }

    public static void addLimit(Category category, String id, int limit) {
        BLOCK_LIMITS.computeIfAbsent(category, k -> new HashMap<>()).put(ResourceLocation.parse(id), limit);
    }
}