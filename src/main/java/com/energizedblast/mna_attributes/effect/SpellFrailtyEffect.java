package com.energizedblast.mna_attributes.effect;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class SpellFrailtyEffect extends MobEffect
{
    public SpellFrailtyEffect() {
        super(MobEffectCategory.HARMFUL, ChatFormatting.DARK_BLUE.getColor());
        this.addAttributeModifier(AttributeRegistry.SPELL_DAMAGE.get(), "5bbf3256-3830-4bf3-b77d-2c80858251c7", -2, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }
}
