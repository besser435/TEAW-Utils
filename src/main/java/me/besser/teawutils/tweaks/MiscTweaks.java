package me.besser.teawutils.tweaks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import me.besser.teawutils.util.TUConfig;

@EventBusSubscriber
public class MiscTweaks {

    private static int cooldownTicks = 600;
    private static boolean disableInvisibility = true;

    @SubscribeEvent
    public static void onRightClickItem(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ThrownEnderpearl enderPearl) {
            if (enderPearl.getOwner() instanceof Player player) {
                player.getCooldowns().addCooldown(Items.ENDER_PEARL, cooldownTicks);
            }
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!disableInvisibility) return;

        if (event.getEffectInstance().getEffect() == MobEffects.INVISIBILITY) {
            player.displayClientMessage(Component.translatable("teawutils.notifications.invisibility_disabled")
                    .withStyle(ChatFormatting.RED), true);
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    public static void reloadConfig() {
        cooldownTicks = TUConfig.COMMON.enderPearlCooldown.get() * 20;
        disableInvisibility = TUConfig.COMMON.disableInvisibility.get();
    }
}
