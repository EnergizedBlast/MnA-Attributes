package com.energizedblast.mna_attributes.spells;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public final class AttributeSpellAdjusters
{
    public AttributeSpellAdjusters() {
    }

    public static final boolean checkSpellEfficiencyAttribute(SpellAdjustingContext context) {
        if (context.caster != null && context.caster instanceof Player && (context.stage == SpellCastStage.CASTING || context.stage == SpellCastStage.SPELL_TOOLTIP))
        {
            return ((context.caster).getAttribute(AttributeRegistry.SPELL_EFFICIENCY.get()).getValue() != 1f);
        } else {
            return false;
        }
    }

    public static final void modifySpellEfficiencyAttribute(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null)
        {
            double attrSpellEfficiency = caster.getAttribute(AttributeRegistry.SPELL_EFFICIENCY.get()).getValue();
            recipe.setManaCost(Math.max(1, recipe.getManaCost() * (2f - (float) attrSpellEfficiency)));
        }
    }

    public static final boolean checkSpellDamageAttribute(SpellAdjustingContext context) {
        if (context.caster != null && context.caster instanceof Player && (context.stage == SpellCastStage.CASTING || context.stage == SpellCastStage.SPELL_TOOLTIP))
        {
            if ((context.caster).getAttributeValue(AttributeRegistry.SPELL_DAMAGE_MULTIPLIER.get()) == 1f) return false;
            return ((context.caster).getAttributeValue(AttributeRegistry.SPELL_DAMAGE.get()) != 1f);
        } else {
            return false;
        }
    }

    public static final void modifySpellDamageAttribute(ISpellDefinition recipe, @Nullable LivingEntity caster) {
        if (caster != null)
        {
            float attrSpellPower = (float)caster.getAttributeValue(AttributeRegistry.SPELL_DAMAGE.get());
            float attrSpellPowerMultiplier = (float)caster.getAttributeValue(AttributeRegistry.SPELL_DAMAGE_MULTIPLIER.get());

            recipe.iterateComponents((c) -> {
                for (Attribute attr : c.getContainedAttributes()) {
                    if (attr == Attribute.DAMAGE) {
                        c.setValue(attr, Math.max(0, (c.getValue(attr) + attrSpellPower) * attrSpellPowerMultiplier));
                    }
                }
            });
        }
    }
}
