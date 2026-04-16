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
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import me.besser.teawutils.util.TEAWUtilsConfig;

@EventBusSubscriber
public class MiscTweaks {

    private static int enderPearCooldownTicks = 20 * 30;        // 30 seconds
    private static int enchantedAppleCooldownTicks = 20 * 300;  // 300 seconds or 5 minutes
    private static boolean disableInvisibility = true;

    // Ender Pearl Cooldown
    @SubscribeEvent
    public static void onRightClickItem(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ThrownEnderpearl enderPearl) {
            if (enderPearl.getOwner() instanceof Player player) {
                player.getCooldowns().addCooldown(Items.ENDER_PEARL, enderPearCooldownTicks);
            }
        }
    }

    // Enchanted Golden Apple Cooldown
    // TODO: Should be more generic. Use a list of item IDs in the config along with a cooldown time.
    // This is very hard coded to just E-Apples.
    @SubscribeEvent
    public static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getItem().is(Items.ENCHANTED_GOLDEN_APPLE)) {
                player.getCooldowns().addCooldown(Items.ENCHANTED_GOLDEN_APPLE, enchantedAppleCooldownTicks);
            }
        }
    }

    // Invisibility ban
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
        enderPearCooldownTicks = TEAWUtilsConfig.COMMON.enderPearlCooldown.get() * 20;
        enchantedAppleCooldownTicks = TEAWUtilsConfig.COMMON.enchantedAppleCooldown.get() * 20;
        disableInvisibility = TEAWUtilsConfig.COMMON.disableInvisibility.get();
    }
}
