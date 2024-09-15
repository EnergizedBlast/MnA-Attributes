package com.energizedblast.mna_attributes.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
 * The only reason the mod does not use AttributesLib as a dependency is due to
 * other changes the mod makes under the hood that some players may not wish to
 * have imposed on them (i.e. armor calculation changes).
 *
 *
 * This event is fired whenever the value of an attribute changes values.<br>
 * It is fired on both sides at different points:
 * <ul>
 * <li>On the Server, it is fired from AttributeMap#onAttributeModified which is the builtin callback hook for values changing.</li>
 * <li>On the Client, it is fired from ClientPacketListener#handleUpdateAttributes after all changes have been processed.</li>
 * </ul>
 * It is fired on {@link MinecraftForge#EVENT_BUS}.
 */
public class AttributeChangedValueEvent extends Event {

    protected LivingEntity entity;
    protected AttributeInstance attrInst;
    protected double oldValue, newValue;

    public AttributeChangedValueEvent(LivingEntity entity, AttributeInstance attrInst, double oldValue, double newValue) {
        this.entity = entity;
        this.attrInst = attrInst;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public AttributeInstance getAttributeInstance() {
        return this.attrInst;
    }

    public double getOldValue() {
        return this.oldValue;
    }

    public double getNewValue() {
        return this.newValue;
    }
}
