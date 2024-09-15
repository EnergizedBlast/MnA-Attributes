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

    protected LivingEntity owner;

    @Override
    public LivingEntity getOwner() {
        return owner;
    }

    @Override
    public void setOwner(LivingEntity owner) {
        if (this.owner != null) throw new UnsupportedOperationException("Cannot set the owner when it is already set.");
        if (owner == null) throw new UnsupportedOperationException("Cannot set the owner to null.");
        this.owner = owner;
    }

}
