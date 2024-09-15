package com.energizedblast.mna_attributes.mixin;

import com.energizedblast.mna_attributes.events.AttributeChangedValueEvent;
import com.energizedblast.mna_attributes.util.IAttributeManager;
import com.energizedblast.mna_attributes.util.IEntityOwned;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
 */

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    private AttributeMap attributes;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * Constructor mixin to call {@link IEntityOwned#setOwner(LivingEntity)} on {@link #attributes}.<br>
     * Supports {@link AttributeChangedValueEvent}.
     */
    @Inject(at = @At(value = "TAIL"), method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", require = 1, remap = false)
    public void mnaAttributes_ownedAttrMap(EntityType<?> type, Level level, CallbackInfo ci) {
        ((IEntityOwned) attributes).setOwner((LivingEntity) (Object) this);
    }

    @Shadow
    public abstract boolean hasEffect(MobEffect ef);

    @Shadow
    public abstract MobEffectInstance getEffect(MobEffect ef);

    /**
     * @author ChampionAsh5357
     * @reason Lock attribute updates for event until after new modifiers are added
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V"), method = "onEffectUpdated", require = 1)
    public void mnaAttributes_onEffectUpdateRemoveAttribute(MobEffectInstance pEffectInstance, boolean pForced, Entity pEntity, CallbackInfo ci) {
        ((IAttributeManager) attributes).setAttributesUpdating(true);
    }

    /**
     * @author ChampionAsh5357
     * @reason Unlock attribute updates for event until after new modifiers are added
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V", shift = At.Shift.AFTER), method = "onEffectUpdated", require = 1)
    public void mnaAttributes_onEffectUpdateAddAttribute(MobEffectInstance pEffectInstance, boolean pForced, Entity pEntity, CallbackInfo ci) {
        ((IAttributeManager) attributes).setAttributesUpdating(false);
    }
}
