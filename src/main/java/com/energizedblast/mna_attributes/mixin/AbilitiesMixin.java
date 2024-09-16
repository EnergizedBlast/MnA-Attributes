package com.energizedblast.mna_attributes.mixin;

import com.energizedblast.mna_attributes.util.IEntityOwned;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
 */

@Mixin(Abilities.class)
public class AbilitiesMixin implements IEntityOwned {

    protected LivingEntity mnaAttributesOwner;

    @Override
    public LivingEntity getMnaAttributesOwner() {
        return mnaAttributesOwner;
    }

    @Override
    public void setMnaAttributesOwner(LivingEntity mnaAttributesOwner) {
        if (this.mnaAttributesOwner != null) throw new UnsupportedOperationException("Cannot set the owner when it is already set.");
        if (mnaAttributesOwner == null) throw new UnsupportedOperationException("Cannot set the owner to null.");
        this.mnaAttributesOwner = mnaAttributesOwner;
    }

}
