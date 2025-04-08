package com.energizedblast.mna_attributes.effect;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.awt.*;

public class SpellMightEffect extends MobEffect
{
    public SpellMightEffect() {
        super(MobEffectCategory.BENEFICIAL, ChatFormatting.BLUE.getColor());
        this.addAttributeModifier(AttributeRegistry.SPELL_DAMAGE.get(), "eb3e136a-f6f8-460f-8a1a-d22ab96eff9b", 2, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
    }
}
