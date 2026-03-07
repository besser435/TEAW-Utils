package me.besser.teawutils.mixin;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import me.besser.teawutils.TEAWUtils;
import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: This only affects Minecart Contraptions
// TODO: There should be a note in the config that specifies what kinds of contraptions are affected.
// TODO: We want to limit all drills, but only limit the CBC stuff to cart contraptions. They can be allowed on trains.
// TODO: I think trains are a special kind of contraption, so we can just disable them on all contraptions and trains should be unaffected.
@Mixin(value = MountedContraption.class, remap = false)
public class ContraptionMixin {
    @Inject(
            method = "assemble",
            at = @At("RETURN")
    )
    private void onAssemble(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) throws AssemblyException {
        TEAWUtils.LOGGER.debug("onAssemble mixin\n\n\n");

        if (cir.getReturnValue()) {
            TEAWUtils.LOGGER.debug("running contraption validation\n\n\n");

            Contraption contraption = (Contraption) (Object) this;  // Casts MountedContraption to general Contraption
            ContraptionLimiter.validate(contraption, pos);
        }
    }
}