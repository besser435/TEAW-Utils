package me.besser.teawutils.mixin;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.bearing.BearingContraption;
import com.simibubi.create.content.contraptions.bearing.ClockworkContraption;
import com.simibubi.create.content.contraptions.bearing.StabilizedContraption;
import com.simibubi.create.content.contraptions.elevator.ElevatorContraption;
import com.simibubi.create.content.contraptions.gantry.GantryContraption;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import com.simibubi.create.content.contraptions.piston.PistonContraption;
import com.simibubi.create.content.contraptions.pulley.PulleyContraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import me.besser.teawutils.contraptionTweaks.ContraptionLimiter;
import me.besser.teawutils.contraptionTweaks.ContraptionLimiter.Category;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {
        MountedContraption.class,
        CarriageContraption.class,

        GantryContraption.class,
        PistonContraption.class,

        BearingContraption.class,
        StabilizedContraption.class,
        ClockworkContraption.class,

        PulleyContraption.class,
        ElevatorContraption.class     // Needed despite subclass of PulleyContraption
}, remap = false)
public class UniversalContraptionMixin {

    @Inject(method = "assemble", at = @At("RETURN"))
    private void onAssemble(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) throws AssemblyException {
        // Only run if the assembly was successful
        if (!cir.getReturnValue()) return;

        Category category = tEAW_Utils$resolveCategory();

        ContraptionLimiter.validate((Contraption) (Object) this, pos, category);
    }

    @Unique
    private Category tEAW_Utils$resolveCategory() {
        Object self = this;

        // I got compiler errors when using switch statements, so this will have to do.
        if (self instanceof MountedContraption) return Category.CART;
        if (self instanceof CarriageContraption) return Category.TRAIN;

        if (self instanceof GantryContraption)   return Category.LINEAR;
        if (self instanceof PistonContraption)   return Category.LINEAR;

        if (self instanceof BearingContraption)  return Category.ROTATIONAL;
        if (self instanceof StabilizedContraption)  return Category.ROTATIONAL;
        if (self instanceof ClockworkContraption)  return Category.ROTATIONAL;

        if (self instanceof PulleyContraption)   return Category.PULLEY;    // Catches elevator too

        return Category.ALL;
    }
}