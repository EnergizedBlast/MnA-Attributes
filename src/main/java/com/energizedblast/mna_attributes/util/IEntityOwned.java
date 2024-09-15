package com.energizedblast.mna_attributes.util;

import net.minecraft.world.entity.LivingEntity;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
*/

public interface IEntityOwned {

    public LivingEntity getOwner();

    public void setOwner(LivingEntity owner);

}
