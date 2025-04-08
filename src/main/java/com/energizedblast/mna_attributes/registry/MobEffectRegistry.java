package com.energizedblast.mna_attributes.registry;

import com.energizedblast.mna_attributes.effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.energizedblast.mna_attributes.MnAAttributes.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);

    public static final RegistryObject<MobEffect> SPELL_MIGHT = EFFECTS.register("spell_might", SpellMightEffect::new);
    public static final RegistryObject<MobEffect> SPELL_FRAILTY = EFFECTS.register("spell_frailty", SpellFrailtyEffect::new);

    public static final RegistryObject<MobEffect> SPELL_LONGEVITY = EFFECTS.register("spell_longevity", SpellLongevityEffect::new);
    public static final RegistryObject<MobEffect> SPELL_SHORTNESS = EFFECTS.register("spell_shortness", SpellShortnessEffect::new);

    public static final RegistryObject<MobEffect> SPELL_HASTE = EFFECTS.register("spell_haste", SpellHasteEffect::new);
    public static final RegistryObject<MobEffect> SPELL_FATIGUE = EFFECTS.register("spell_fatigue", SpellFatigueEffect::new);

    public static final RegistryObject<MobEffect> SPELL_PRESERVING = EFFECTS.register("spell_preserving", SpellPreservingEffect::new);
    public static final RegistryObject<MobEffect> SPELL_WASTING = EFFECTS.register("spell_wasting", SpellWastingEffect::new);

    @SubscribeEvent
    public static void addPotionRecipes(FMLCommonSetupEvent event) {
        // Might add potion recipes for these later, mostly up to modpack devs to add that though.
        //PotionBrewing.addMix(Potions.WATER, ItemRegistry.ROTTEN_FLESH.get(), Potions.MUNDANE);
    }
}
