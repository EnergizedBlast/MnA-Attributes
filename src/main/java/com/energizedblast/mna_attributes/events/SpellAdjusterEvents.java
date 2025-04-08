package com.energizedblast.mna_attributes.events;

import com.energizedblast.mna_attributes.spells.AttributeSpellAdjusters;
import com.energizedblast.mna_attributes.MnAAttributes;
import com.mna.api.spells.adjusters.DefaultAdjusters;
import com.mna.spells.SpellCaster;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(
        modid = MnAAttributes.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)

public class SpellAdjusterEvents {
    public SpellAdjusterEvents() {
    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event)
    {
        SpellCaster.registerAdjuster(DefaultAdjusters::adjustAlways, AttributeSpellAdjusters::modifySpellEfficiencyAttribute);
        SpellCaster.registerAdjuster(AttributeSpellAdjusters::checkSpellDamageAttribute, AttributeSpellAdjusters::modifySpellDamageAttribute);
        SpellCaster.registerAdjuster(AttributeSpellAdjusters::checkSpellDurationAttribute, AttributeSpellAdjusters::modifySpellDurationAttribute);
    }
}
