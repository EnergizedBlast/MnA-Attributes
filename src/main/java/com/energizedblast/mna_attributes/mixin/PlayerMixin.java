package com.energizedblast.mna_attributes.mixin;

import com.energizedblast.mna_attributes.util.IEntityOwned;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
 */

@Mixin(Player.class)
public class PlayerMixin {

    @Shadow
    private Abilities abilities;

    /**
     * Constructor mixin to call {@link IEntityOwned#setMnaAttributesOwner(LivingEntity)} on {@link #abilities}.<br>
     */
    @Inject(at = @At(value = "TAIL"), method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLcom/mojang/authlib/GameProfile;)V", require = 1, remap = false)
    public void mnaAttributes_ownedAbilities(Level level, BlockPos pos, float yRot, GameProfile profile, CallbackInfo ci) {
        ((IEntityOwned) abilities).setMnaAttributesOwner((LivingEntity) (Object) this);
    }

}
