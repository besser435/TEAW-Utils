package me.besser.teawutils.util;

import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TEAWUtilsConfig {

    public static final ModConfigSpec SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        // Contraption limits
        public final Map<ContraptionLimiter.Category, CategoryConfig> categories = new HashMap<>();

        // Utility Tweaks
        public final ModConfigSpec.IntValue enderPearlCooldown;
        public final ModConfigSpec.IntValue enchantedAppleCooldown;
        public final ModConfigSpec.ConfigValue<List<? extends String>> disabledPotions;
        public final ModConfigSpec.BooleanValue requirePartyToClaim;

        // Build config
        public Common(ModConfigSpec.Builder builder) {
            builder.comment("Contraption limits").push("contraption_limits");

            for (ContraptionLimiter.Category cat : ContraptionLimiter.Category.values()) {
                builder.comment(cat.getDescription()).push(cat.name().toLowerCase());

                categories.put(cat, new CategoryConfig(builder, cat));

                builder.pop();
            }

            builder.pop();



            builder.comment("General utility tweaks").push("tweaks");

            enderPearlCooldown = builder
                    .comment("Cooldown for Ender Pearls in seconds. Minecraft's default is 1 second.")
                    .defineInRange("enderPearlCooldownSeconds", 30, 0, 3600);

            enchantedAppleCooldown = builder
                    .comment("Cooldown for Enchanted Golden Apples in seconds.")
                    .comment("Players can still get this effect by being shot with a Potato Cannon loaded with an E-Apple.")
                    .defineInRange("enchantedAppleCooldownSeconds", 300, 0, 3600);

            disabledPotions = builder
                    .comment("List of potion/mob effect IDs to prevent players from receiving. Format: 'namespace:effect_id'")
                    .defineListAllowEmpty("disabledEffects", List.of("minecraft:invisibility"), obj -> obj instanceof String);

            requirePartyToClaim = builder
                    .comment("Whether or not to require a party in order to be able to claim for Open Parties and Claims.")
                    .define("requirePartyToClaim", true);

            builder.pop();
        }

        public static class CategoryConfig {
            public final ModConfigSpec.ConfigValue<List<? extends String>> bannedMods;
            public final ModConfigSpec.ConfigValue<List<? extends String>> bannedBlocks;
            public final ModConfigSpec.ConfigValue<List<? extends String>> blockLimits;

            public CategoryConfig(ModConfigSpec.Builder builder, ContraptionLimiter.Category category) {
                bannedMods = builder
                        .comment("Banned mods. Format: 'namespace'")
                        // TODO: fix deprecated defineList method
                        .defineListAllowEmpty("bannedMods", category.getDefaultMods(), obj -> obj instanceof String);

                bannedBlocks = builder
                        .comment("Banned blocks. Format: 'namespace:block'")
                        .defineListAllowEmpty("bannedBlocks", category.getDefaultBlocks(), obj -> obj instanceof String);

                blockLimits = builder
                        .comment("Limited blocks. Format: 'namespace:block=limit'")
                        .defineListAllowEmpty("blockLimits", category.getDefaultLimits(), obj -> obj instanceof String);
            }
        }
    }
}