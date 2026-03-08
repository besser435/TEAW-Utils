package me.besser.teawutils.contraptionTweaks;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
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
    private static final Map<String, Set<ResourceLocation>> BANNED_BLOCKS = new HashMap<>();
    private static final Map<String, Set<String>> BANNED_MODS = new HashMap<>();
    private static final Map<String, Map<ResourceLocation, Integer>> BLOCK_LIMITS = new HashMap<>();

    public static final String TYPE_ANY = "any";
    public static final String TYPE_CART = "cart";
    public static final String TYPE_TRAIN = "train";

    public static void init() {
        //  TODO us config values
        // Universal limit: Drills are limited everywhere
        addLimit(TYPE_ANY, "create:mechanical_drill", 12);

        // Specific limit: Ban CBC on Carts, but not necessarily on Trains
        addBannedMod(TYPE_CART, "createbigcannons");

        // Example: Only 4 chests on Trains, but unlimited on others
        addLimit(TYPE_TRAIN, "minecraft:chest", 4);

    }

    public static void validate(Contraption contraption, BlockPos anchorPos, String currentType) throws AssemblyException {
        Map<ResourceLocation, Integer> currentCounts = new HashMap<>();

        for (StructureBlockInfo info : contraption.getBlocks().values()) {
            Block block = info.state().getBlock();
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

            // 1. Check Bans
            if (isBanned(id, TYPE_ANY) || isBanned(id, currentType)) {
                throw AssemblyException.unmovableBlock(anchorPos.offset(info.pos()), info.state());
            }

            // 2. Increment the count ONCE per block
            int count = currentCounts.getOrDefault(id, 0) + 1;
            currentCounts.put(id, count);

            // 3. Check Limits for "any" and the specific type using the updated count
            checkLimit(id, info, count, TYPE_ANY);
            checkLimit(id, info, count, currentType);
        }
    }

    private static void checkLimit(ResourceLocation id, StructureBlockInfo info, int currentCount, String type) throws AssemblyException {
        Map<ResourceLocation, Integer> typeLimits = BLOCK_LIMITS.get(type);

        if (typeLimits != null && typeLimits.containsKey(id)) {
            int limit = typeLimits.get(id);

            if (currentCount > limit) {
                throw new AssemblyException(info.state().getBlock().getName().copy()
                        .append(" limit exceeded.")
                        .append(" The limit is: " + limit));
            }
        }
    }

    private static boolean isBanned(ResourceLocation id, String type) {
        if (BANNED_BLOCKS.getOrDefault(type, Set.of()).contains(id)) return true;
        if (BANNED_MODS.getOrDefault(type, Set.of()).contains(id.getNamespace())) return true;

        return false;
    }

    public static void addBannedMod(String type, String modId) {
        BANNED_MODS.computeIfAbsent(type, k -> new HashSet<>()).add(modId);
    }

    public static void addLimit(String type, String id, int limit) {
        BLOCK_LIMITS.computeIfAbsent(type, k -> new HashMap<>()).put(ResourceLocation.parse(id), limit);
    }
}