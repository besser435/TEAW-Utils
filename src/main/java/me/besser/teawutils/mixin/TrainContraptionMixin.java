package me.besser.teawutils.mixin;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import me.besser.teawutils.TEAWUtils;
import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = CarriageContraption.class, remap = false)
public class TrainContraptionMixin {
    @Inject(method = "assemble", at = @At("RETURN"))
    private void onAssemble(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) throws AssemblyException {

        TEAWUtils.LOGGER.info("Assemble train contraption");

        if (cir.getReturnValue()) {

            TEAWUtils.LOGGER.info("Validating train contraption...");

            // We tell the limiter this is a TRAIN
            ContraptionLimiter.validate((Contraption)(Object)this, pos, ContraptionLimiter.TYPE_TRAIN);
        }
    }
}