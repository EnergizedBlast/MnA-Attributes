package com.energizedblast.mna_attributes.spells;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.Shape;
import com.mna.spells.shapes.ShapeBoundAxe;
import com.mna.spells.shapes.ShapeBoundSword;
import com.mna.spells.shapes.ShapeSummon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class AttributeSpellAdjusters
{
    public AttributeSpellAdjusters() {
    }

    public static final boolean checkSpellEfficiencyAttribute(SpellAdjustingContext context) {
        if (context.caster instanceof Player && (context.stage == SpellCastStage.CASTING || context.stage == SpellCastStage.SPELL_TOOLTIP || context.stage == SpellCastStage.CALCULATING_MANA_COST || context.stage == SpellCastStage.SPELLCRAFTING_MANA_COST_ESTIMATE))
        {
            return ((context.caster).getAttributeValue(AttributeRegistry.SPELL_EFFICIENCY.get()) != 1f);
        } else {
            return false;
        }
    }

    public static final void modifySpellEfficiencyAttribute(SpellAdjustingContext context) {
        LivingEntity caster = context.caster;
        if (caster != null)
        {
            double attrSpellEfficiency = caster.getAttributeValue(AttributeRegistry.SPELL_EFFICIENCY.get());
            context.spell.setManaCost(Math.max(1, context.spell.getManaCost() * (2f - (float)attrSpellEfficiency)));
        }
    }

    public static final boolean checkSpellDamageAttribute(SpellAdjustingContext context) {
        if (context.caster instanceof Player && (context.stage == SpellCastStage.CASTING || context.stage == SpellCastStage.SPELL_TOOLTIP))
        {
            return (((context.caster).getAttributeValue(AttributeRegistry.SPELL_DAMAGE_MULTIPLIER.get()) != 1f) || ((context.caster).getAttributeValue(AttributeRegistry.SPELL_DAMAGE.get()) != 0f));
        } else {
            return false;
        }
    }

    public static final void modifySpellDamageAttribute(SpellAdjustingContext context) {
        LivingEntity caster = context.caster;
        if (caster != null)
        {
            float attrSpellPower = (float)caster.getAttributeValue(AttributeRegistry.SPELL_DAMAGE.get());
            float attrSpellPowerMultiplier = (float)caster.getAttributeValue(AttributeRegistry.SPELL_DAMAGE_MULTIPLIER.get());

            IModifiedSpellPart<Shape> shape = context.spell.getShape();
            if (shape.getContainedAttributes() != null) {
                if (shape.getPart() instanceof ShapeBoundSword
                        || shape.getPart() instanceof ShapeBoundAxe
                )
                {
                    for (Attribute attr : context.spell.getShape().getContainedAttributes()) {
                        if (attr == Attribute.DAMAGE) {
                           shape.setValue(attr, Math.max(0, (shape.getValue(attr) + attrSpellPower) * attrSpellPowerMultiplier));
                         }
                    }
                }
            }

            context.spell.iterateComponents((c) -> {
                for (Attribute attr : c.getContainedAttributes()) {
                    if (attr == Attribute.DAMAGE) {
                        c.setValue(attr, Math.max(0, (c.getValue(attr) + attrSpellPower) * attrSpellPowerMultiplier));
                    }
                }
            });
        }
    }

    public static final boolean checkSpellDurationAttribute(SpellAdjustingContext context) {
        if (context.caster instanceof Player && (context.stage == SpellCastStage.CASTING || context.stage == SpellCastStage.SPELL_TOOLTIP))
        {
            return (((context.caster).getAttributeValue(AttributeRegistry.SPELL_DURATION_MULTIPLIER.get()) != 1f));
        } else {
            return false;
        }
    }

    public static final void modifySpellDurationAttribute(SpellAdjustingContext context) {
        LivingEntity caster = context.caster;
        if (caster != null)
        {
            float attrSpellDurationMultiplier = (float)caster.getAttributeValue(AttributeRegistry.SPELL_DURATION_MULTIPLIER.get());

            IModifiedSpellPart<Shape> shape = context.spell.getShape();
            if (shape.getContainedAttributes() != null)
            {
                if (shape.getPart() instanceof ShapeSummon)
                {
                    for (Attribute attr : context.spell.getShape().getContainedAttributes()) {
                        if (attr == Attribute.DURATION)
                        {
                            shape.setValue(attr, Math.max(0, (shape.getValue(attr) * attrSpellDurationMultiplier)));
                        }
                    }
                }
            }

            context.spell.iterateComponents((c) -> {
                for (Attribute attr : c.getContainedAttributes()) {
                    if (attr == Attribute.DURATION) {
                        c.setValue(attr, Math.max(0, (c.getValue(attr) * attrSpellDurationMultiplier)));
                    }
                }
            });
        }
    }
}
