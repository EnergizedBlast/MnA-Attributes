package com.energizedblast.mna_attributes.events;

import com.energizedblast.mna_attributes.Config;
import com.energizedblast.mna_attributes.handlers.AttributeHandlers;
import com.energizedblast.mna_attributes.registry.AttributeRegistry;
import com.mna.api.affinity.Affinity;
import com.mna.api.events.AffinityChangedEvent;
import com.mna.api.events.SpellCooldownCalculatingEvent;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import dev.shadowsoffire.attributeslib.api.AttributeChangedValueEvent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class RuntimeEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void AffinityChangedEvent(AffinityChangedEvent event) {
        AttributeHandlers.updateAffinities(event);
        event.setCanceled(true);
    }

    // Required to have retroactive affinity-related attribute assigning for players
    @SubscribeEvent
    public static void onEntityJoinUpdate(EntityJoinLevelEvent event)
    {
        if (event.getEntity() instanceof Player player)
        {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(pPlayer -> {
                HashMap<Affinity, Float> cachedValues = new HashMap<>();
                for (Affinity iAffinity : Affinity.CoreSix())
                {
                    cachedValues.put(iAffinity, pPlayer.getAffinityDepth(iAffinity));
                }
                // Make sure all affinity values are captured before attribute setting causes attribute change events to fire and clear them
                for (Affinity iAffinity : Affinity.CoreSix())
                {
                    player.getAttribute(AttributeHandlers.getAffinityAttribute(iAffinity)).setBaseValue(cachedValues.get(iAffinity));
                }
            });

            // Apply config default values to the player, may need to change if other mods do not use attribute modifiers when applying attribute changes
            for (RegistryObject<Attribute> attr : AttributeRegistry.ATTRIBUTES.getEntries())
            {
                if (attr.get().getDescriptionId().contains("affinity")) continue;
                ((Player) event.getEntity()).getAttribute(attr.get()).setBaseValue(Config.getAttributeConfigDefaultValue(attr));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttributeChangeUpdate(AttributeChangedValueEvent event)
    {
        if (event.getEntity() instanceof Player player)
        {
            if (event.getAttributeInstance() == null) return;

            Attribute attr = event.getAttributeInstance().getAttribute();
            if (AttributeRegistry.isMnaAttribute(attr))
            {
                player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(pPlayer -> {
                    AttributeHandlers.updateAttributesFromEvent(player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onCooldownCalculate(SpellCooldownCalculatingEvent event)
    {
        Player player = event.getCaster();
        event.setCooldown((int)(event.getCooldown()/player.getAttributeBaseValue(AttributeRegistry.CAST_SPEED.get())));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onPlayerCloned(PlayerEvent.Clone event)
    {
        if (event.isWasDeath())
        {
            for (RegistryObject<Attribute> attr : AttributeRegistry.ATTRIBUTES.getEntries())
            {
                // Permanent modifiers SHOULD carry over across death, leaving note in case that ends up being messy for some reason
                event.getEntity().getAttribute(attr.get()).setBaseValue(event.getOriginal().getAttributeBaseValue(attr.get()));
            }
        }
    }
}
