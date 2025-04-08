package com.energizedblast.mna_attributes.effect;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.awt.*;

public class SpellLongevityEffect extends MobEffect
{
    public SpellLongevityEffect() {
        super(MobEffectCategory.BENEFICIAL, ChatFormatting.YELLOW.getColor());
        this.addAttributeModifier(AttributeRegistry.SPELL_DURATION_MULTIPLIER.get(), "c93cc2c2-4534-49a0-9bbe-7afe32760592", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }
}
