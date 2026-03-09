package me.besser.teawutils.util;

import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TUConfig {

    public static final ModConfigSpec SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final Map<ContraptionLimiter.Category, CategoryConfig> categories = new HashMap<>();

        public Common(ModConfigSpec.Builder builder) {
            builder.push("contraption_limits");

            for (ContraptionLimiter.Category cat : ContraptionLimiter.Category.values()) {
                builder.comment(cat.getDescription())
                        .push(cat.name().toLowerCase());

                categories.put(cat, new CategoryConfig(builder, cat));

                builder.pop();
            }

            builder.pop();
        }

        public static class CategoryConfig {
            public final ModConfigSpec.ConfigValue<List<? extends String>> bannedMods;
            public final ModConfigSpec.ConfigValue<List<? extends String>> bannedBlocks;
            public final ModConfigSpec.ConfigValue<List<? extends String>> blockLimits;

            public CategoryConfig(ModConfigSpec.Builder builder, ContraptionLimiter.Category category) {
                bannedMods = builder
                        .comment("Banned mods. Format: 'modid'")
                        // TODO: fix deprecated defineList method
                        .defineListAllowEmpty("bannedMods", category.getDefaultMods(), obj -> obj instanceof String);

                bannedBlocks = builder
                        .comment("Banned blocks. Format: 'modid:block'")
                        .defineListAllowEmpty("bannedBlocks", category.getDefaultBlocks(), obj -> obj instanceof String);

                blockLimits = builder
                        .comment("Limited blocks. Format: 'modid:block=limit'")
                        .defineListAllowEmpty("blockLimits", category.getDefaultLimits(), obj -> obj instanceof String);
            }
        }
    }
}