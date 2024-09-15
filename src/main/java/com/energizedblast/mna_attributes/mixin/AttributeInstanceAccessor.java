package com.energizedblast.mna_attributes.mixin;

import com.energizedblast.mna_attributes.events.AttributeChangedValueEvent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeInstance.class)
public interface AttributeInstanceAccessor {

    /**
     * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
     *
     * Exposes AttributeInstance#cachedValue so we can read it while posting {@link AttributeChangedValueEvent}.<br>
     * Due to the hook location, this value will always be the value that was computed prior to application of the latest
     * modifier (the one causing the change).
     */
    @Accessor
    public double getCachedValue();

}
