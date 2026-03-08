package me.besser.teawutils.mixin;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.piston.PistonContraption;
import me.besser.teawutils.TEAWUtils;
import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: add other TranslatingContraption types such as gantry shafts, elevators, rope pulleys.
// Rope pulleys should maybe be exempt from certain things like drill limits.
@Mixin(value = PistonContraption.class, remap = false)
public class PistonContraptionMixin {
    @Inject(method = "assemble", at = @At("RETURN"))
    private void onAssemble(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) throws AssemblyException {

        TEAWUtils.LOGGER.info("Assemble piston contraption");

        if (cir.getReturnValue()) {

            TEAWUtils.LOGGER.info("Validating piston contraption...");

            // Treat these as generic kinetic contraptions
            ContraptionLimiter.validate((Contraption)(Object)this, pos, "kinetic");
        }
    }
}