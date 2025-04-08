package com.energizedblast.mna_attributes.effect;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class SpellFatigueEffect extends MobEffect
{
    public SpellFatigueEffect() {
        super(MobEffectCategory.HARMFUL, ChatFormatting.DARK_RED.getColor());
        this.addAttributeModifier(AttributeRegistry.CAST_SPEED.get(), "4e6446f8-27eb-4a3d-9f7c-d755fac73d7d", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }
}
