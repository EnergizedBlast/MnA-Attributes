package com.energizedblast.mna_attributes.mixin;

import com.energizedblast.mna_attributes.events.AttributeChangedValueEvent;
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
 * For the {@link AttributeChangedValueEvent} to have the necessary entity context, the attribute map must be aware of the owning entity.<br>
 * Once that context is known, firing the event is a hook in AttributeMap#onAttributeModified.
 * <p>
 * The client event is posted from the client packet listener as this method is unreliable on the client due to how attribute sync is done.
 */
@Mixin(AttributeMap.class)
public class AttributeMapMixin implements IEntityOwned, IAttributeManager {

    protected LivingEntity owner;
    private boolean areAttributesUpdating;
    private Map<Attribute, Pair<AttributeInstance, Double>> updatingAttributes = new HashMap<>();

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

    @Override
    public boolean areAttributesUpdating() {
        return this.areAttributesUpdating;
    }

    @Override
    public void setAttributesUpdating(boolean updating) {
        this.areAttributesUpdating = updating;

        // If the attributes are being updated, clear the updating list
        if (this.areAttributesUpdating()) {
            this.updatingAttributes.clear();
        }
        else {
            // Otherwise, cycle through each instance and get the new values, post the results
            if (!this.getOwner().level().isClientSide) {
                this.updatingAttributes.forEach((attr, pair) -> MinecraftForge.EVENT_BUS.post(new AttributeChangedValueEvent(this.getOwner(), pair.getFirst(), pair.getSecond(), pair.getFirst().getValue())));
            }

            // Extra clear in case of weird behavior
            this.updatingAttributes.clear();
        }
    }

    /**
     * Serverside call site for {@link AttributeChangedValueEvent}. Uses the built-in hook AttributeMap#onAttributeModified and the stapled-in entity
     * context.
     * <p>
     * Not used on the client since it would cause it to be fired excessively many times during sync.
     */
    @Inject(at = @At(value = "HEAD"), method = "onAttributeModified(Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;)V", require = 1)
    public void mnaAttributes_attrModifiedEvent(AttributeInstance inst, CallbackInfo ci) {
        if (owner == null) throw new RuntimeException("An AttributeMap object was modified without a set owner!");

        if (!this.areAttributesUpdating() && !owner.level().isClientSide) {
            // This call site is only valid on the server, because the client nukes and reapplies all attribute modifiers when received.
            double oldValue = ((AttributeInstanceAccessor) inst).getCachedValue();
            double newValue = inst.getValue(); // Calling getValue will compute the value once marked dirty.
            if (oldValue != newValue) {
                MinecraftForge.EVENT_BUS.post(new AttributeChangedValueEvent(getOwner(), inst, oldValue, newValue));
            }
        }
        else if (this.areAttributesUpdating()) {
            // If attributes are being updated, store the instance and previous value for exectuion after update
            this.updatingAttributes.putIfAbsent(inst.getAttribute(), Pair.of(inst, ((AttributeInstanceAccessor) inst).getCachedValue()));
        }
    }

}
