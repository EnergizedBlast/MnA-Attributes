package com.energizedblast.mna_attributes;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.energizedblast.mna_attributes.registry.AttributeRegistry.*;

@Mod.EventBusSubscriber(modid = MnAAttributes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue STARTING_MAX_MANA = BUILDER
            .comment("The following attributes settings are ONLY set when a player first joins a world with this mod loaded. Manual resetting of attributes is required for config changes to apply.")
            .defineInRange("startingMaxManaModifier", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE);
    public static final ForgeConfigSpec.DoubleValue STARTING_MAX_MANA_MULTIPLIER = BUILDER
            .defineInRange("startingMaxManaMultiplier", 1.0D, -2048.0D, 2048.0D);
    public static final ForgeConfigSpec.DoubleValue STARTING_MANA_REGEN = BUILDER
            .defineInRange("startingManaRegenMultiplier", 1.0D, -2048.0D, 2.0D);
    public static final ForgeConfigSpec.DoubleValue STARTING_CAST_SPEED = BUILDER
            .defineInRange("startingCastSpeedMultiplier", 1.0D, 0.001D, 2048.0D);
    public static final ForgeConfigSpec.DoubleValue STARTING_SPELL_DAMAGE = BUILDER
            .defineInRange("startingSpellDamageModifier", 0.0D, -2048.0D, 2048.0D);
    public static final ForgeConfigSpec.DoubleValue STARTING_SPELL_DAMAGE_MULTIPLIER = BUILDER
            .defineInRange("startingSpellDamageMultiplier", 1.0D, 0.0D, 2048.0D);
    public static final ForgeConfigSpec.DoubleValue STARTING_SPELL_EFFICIENCY = BUILDER
            .defineInRange("startingSpellEfficiencyMultiplier", 1.0D, -2048.0D, 2048.0D);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double startingMaxManaModifier = 0.0D;
    public static double startingMaxManaMultiplier = 1.0D;
    public static double startingCastSpeedMultiplier = 1.0D;
    public static double startingSpellDamageModifier = 0.0D;
    public static double startingSpellDamageMultiplier = 1.0D;
    public static double startingSpellEfficiencyMultiplier = 1.0D;
    public static double startingManaRegenMultiplier = 1.0D;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

    }

    public static double getAttributeConfigDefaultValue(RegistryObject<Attribute> attribute)
    {
        if (attribute == null) return 0;
        else
        {
            if (attribute == MAX_MANA) return STARTING_MAX_MANA.get();
            if (attribute == MAX_MANA_MULTIPLIER) return STARTING_MAX_MANA_MULTIPLIER.get();
            if (attribute == MANA_REGEN) return STARTING_MANA_REGEN.get();
            if (attribute == CAST_SPEED) return STARTING_CAST_SPEED.get();
            if (attribute == SPELL_DAMAGE) return STARTING_SPELL_DAMAGE.get();
            if (attribute == SPELL_DAMAGE_MULTIPLIER) return STARTING_SPELL_DAMAGE_MULTIPLIER.get();
            if (attribute == SPELL_EFFICIENCY) return STARTING_SPELL_EFFICIENCY.get();
        }
        return 0;
    }
}
