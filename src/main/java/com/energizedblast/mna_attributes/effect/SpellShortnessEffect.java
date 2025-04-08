package com.energizedblast.mna_attributes.effect;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class SpellShortnessEffect extends MobEffect
{
    public SpellShortnessEffect() {
        super(MobEffectCategory.HARMFUL, ChatFormatting.GRAY.getColor());
        this.addAttributeModifier(AttributeRegistry.SPELL_DURATION_MULTIPLIER.get(), "c2046a8c-8be5-461d-bb4e-898dd7bca715", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }
}
