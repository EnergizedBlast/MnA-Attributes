package com.energizedblast.mna_attributes.effect;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class SpellWastingEffect extends MobEffect
{
    public SpellWastingEffect() {
        super(MobEffectCategory.HARMFUL, ChatFormatting.DARK_GREEN.getColor());
        this.addAttributeModifier(AttributeRegistry.SPELL_EFFICIENCY.get(), "e86108d7-ec33-475e-89f1-2d310651d12b", -0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }
}
