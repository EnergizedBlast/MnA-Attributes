package com.energizedblast.mna_attributes.mixin;

import com.energizedblast.mna_attributes.events.MnAAttributeChangedValueEvent;
import com.energizedblast.mna_attributes.util.IAttributeManager;
import com.energizedblast.mna_attributes.util.IEntityOwned;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
 *
 * For the {@link MnAAttributeChangedValueEvent} to have the necessary entity context, the attribute map must be aware of the owning entity.<br>
 * Once that context is known, firing the event is a hook in AttributeMap#onAttributeModified.
 * <p>
 * The client event is posted from the client packet listener as this method is unreliable on the client due to how attribute sync is done.
 */
@Mixin(AttributeMap.class)
public class AttributeMapMixin implements IEntityOwned, IAttributeManager {

    protected LivingEntity mnaAttributesOwner;
    private boolean mnaAttributesAreAttributesUpdating;
    private Map<Attribute, Pair<AttributeInstance, Double>> mnaAttributesUpdatingAttributes = new HashMap<>();

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

    @Override
    public boolean areAttributesUpdating() {
        return this.mnaAttributesAreAttributesUpdating;
    }

    @Override
    public void setAttributesUpdating(boolean updating) {
        this.mnaAttributesAreAttributesUpdating = updating;

        // If the attributes are being updated, clear the updating list
        if (this.areAttributesUpdating()) {
            this.mnaAttributesUpdatingAttributes.clear();
        }
        else {
            // Otherwise, cycle through each instance and get the new values, post the results
            if (!this.getMnaAttributesOwner().level().isClientSide) {
                this.mnaAttributesUpdatingAttributes.forEach((attr, pair) -> MinecraftForge.EVENT_BUS.post(new MnAAttributeChangedValueEvent(this.getMnaAttributesOwner(), pair.getFirst(), pair.getSecond(), pair.getFirst().getValue())));
            }

            // Extra clear in case of weird behavior
            this.mnaAttributesUpdatingAttributes.clear();
        }
    }

    /**
     * Serverside call site for {@link MnAAttributeChangedValueEvent}. Uses the built-in hook AttributeMap#onAttributeModified and the stapled-in entity
     * context.
     * <p>
     * Not used on the client since it would cause it to be fired excessively many times during sync.
     */
    @Inject(at = @At(value = "HEAD"), method = "onAttributeModified(Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;)V", require = 1)
    public void mnaAttributes_attrModifiedEvent(AttributeInstance inst, CallbackInfo ci) {
        if (mnaAttributesOwner == null) throw new RuntimeException("An AttributeMap object was modified without a set owner!");

        if (!this.areAttributesUpdating() && !mnaAttributesOwner.level().isClientSide) {
            // This call site is only valid on the server, because the client nukes and reapplies all attribute modifiers when received.
            double oldValue = ((AttributeInstanceAccessor) inst).getCachedValue();
            double newValue = inst.getValue(); // Calling getValue will compute the value once marked dirty.
            if (oldValue != newValue) {
                MinecraftForge.EVENT_BUS.post(new MnAAttributeChangedValueEvent(getMnaAttributesOwner(), inst, oldValue, newValue));
            }
        }
        else if (this.areAttributesUpdating()) {
            // If attributes are being updated, store the instance and previous value for exectuion after update
            this.mnaAttributesUpdatingAttributes.putIfAbsent(inst.getAttribute(), Pair.of(inst, ((AttributeInstanceAccessor) inst).getCachedValue()));
        }
    }

}
