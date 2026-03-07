package me.besser.teawutils.util;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class TUConfig {

    public static final ModConfigSpec SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {

        public final ModConfigSpec.ConfigValue<List<? extends String>> bannedNamespaces;
        public final ModConfigSpec.ConfigValue<List<? extends String>> bannedBlocks;
        public final ModConfigSpec.ConfigValue<List<? extends String>> blockLimits;

        public Common(ModConfigSpec.Builder builder) {

            builder.push("contraption_limits");

            builder.comment(
                    "========================================",
                    "        Contraption Limiter",
                    "========================================",

                    "TODO: Currently only minecart contraptions are affected, not all contraptions.",
                    "See the note in the mixin class to fix this."
            ).push("contraption_limits");

            bannedNamespaces = builder
                    .comment(
                            "Namespaces that cannot be assembled into contraptions.",
                            "Format: modid",
                            "Example: createbigcannons"
                    )
                    .defineListAllowEmpty(
                            "bannedNamespaces",
                            List.of("createbigcannons"),
                            () -> "",
                            obj -> obj instanceof String
                    );

            bannedBlocks = builder
                    .comment(
                            "Specific blocks that cannot be assembled into contraptions.",
                            "Format: modid:block",
                            "Example: minecraft:chest"
                    )
                    .defineListAllowEmpty(
                            "bannedBlocks",
                            List.of(),
                            () -> "",
                            obj -> obj instanceof String
                    );

            blockLimits = builder
                    .comment(
                            "The max number of specific blocks that can be assembled into contraption.",
                            "Format: modid:block=limit",
                            "Example: create:mechanical_drill=320"
                    )
                    .defineListAllowEmpty(
                            "blockLimits",
                            List.of("create:mechanical_drill=320"),
                            () -> "",
                            obj -> obj instanceof String
                    );

            builder.pop();
        }
    }
}