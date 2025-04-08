package com.energizedblast.mna_attributes.effect;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class SpellPreservingEffect extends MobEffect
{
    public SpellPreservingEffect() {
        super(MobEffectCategory.BENEFICIAL, ChatFormatting.GREEN.getColor());
        this.addAttributeModifier(AttributeRegistry.SPELL_EFFICIENCY.get(), "821da13a-4702-41c8-91ff-029539d00916", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }
}
