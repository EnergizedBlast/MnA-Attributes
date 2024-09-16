package com.energizedblast.mna_attributes.handlers;

import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.resource.ICastingResource;
import com.mna.api.events.AffinityChangedEvent;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicReference;

public class AttributeHandlers
{

    // TODO: Remove or change to just change the relevant attribute
    public static void updateAttributesFromEvent(Player player)
    {
        updateAttributes(player);
    }

    public static Attribute getAffinityAttribute(Affinity affinity)
    {
        return switch (affinity.getShiftAffinity()) {
            case ARCANE -> AttributeRegistry.AFFINITY_ARCANE.get();
            case EARTH -> AttributeRegistry.AFFINITY_EARTH.get();
            case ENDER -> AttributeRegistry.AFFINITY_ENDER.get();
            case FIRE -> AttributeRegistry.AFFINITY_FIRE.get();
            case WATER -> AttributeRegistry.AFFINITY_WATER.get();
            case WIND -> AttributeRegistry.AFFINITY_WIND.get();
            default -> null;
        };
    }

    public static void updateAffinities(AffinityChangedEvent event)
    {
        Player player = event.getPlayer();

        // Mirroring shiftAffinity calculations
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(pPlayer -> {
            for (Affinity iAffinity : Affinity.CoreSix())
            {
                if (event.getAffinity().getShiftAffinity() == iAffinity) continue;
                setAffinityAttribute(player, iAffinity, -(event.getShift()/2f));
            }
            setAffinityAttribute(player, event.getAffinity().getShiftAffinity(), event.getShift());
        });

        //LogUtils.getLogger().info(MnAAttributes.MODID + ": updateAffinities: event.getCurrentAmount() = " + event.getAffinity().getShiftAffinity() + " " + event.getCurrentAmount() + " shift " + event.getShift());
    }

    private static void setAffinityAttribute(Player player, Affinity affinity, float changeAmount)
    {
        Attribute attr = getAffinityAttribute(affinity.getShiftAffinity());
        player.getAttribute(attr).setBaseValue((Math.max(Math.min(100f, player.getAttributeBaseValue(attr) + changeAmount), 0f)));
    }

    public static ICastingResource getPlayerCastingResource(Player player)
    {
        AtomicReference<ICastingResource> returnResource = new AtomicReference<>();
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(pPlayer -> returnResource.set(pPlayer.getCastingResource()));
        return returnResource.get();
    }

    public static void updateAttributes(Player player)
    {
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(pPlayer ->
        {
            ICastingResource castResource = getPlayerCastingResource(player);

            double attrMaxManaBase = player.getAttributeBaseValue(AttributeRegistry.MAX_MANA.get());
            double attrMaxManaMultiplier = player.getAttributeValue(AttributeRegistry.MAX_MANA_MULTIPLIER.get());
            double attrManaRegenModifier = player.getAttributeValue(AttributeRegistry.MANA_REGEN.get());

            // Max mana modifiers are additive in MnA, getting the resulting bonus mana conferred from max mana multiplier is required
            double maxManaMultiplierAmount = (castResource.getMaxAmountBaseline() * attrMaxManaMultiplier) - castResource.getMaxAmountBaseline();

            castResource.addModifier("mna_attributes:max_mana_base_modifier", (float)attrMaxManaBase);
            castResource.addModifier("mna_attributes:max_mana_multiplier_modifier", (float)maxManaMultiplierAmount);
            castResource.addRegenerationModifier("mna_attributes:mana_regen_modifier", 2 - (float)attrManaRegenModifier);

            for (Affinity iAffinity : Affinity.CoreSix())
            {
                pPlayer.setAffinityDepth(iAffinity, (float)player.getAttributeValue(getAffinityAttribute(iAffinity)));
            }
        });
    }
}
