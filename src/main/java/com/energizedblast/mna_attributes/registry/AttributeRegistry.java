package com.energizedblast.mna_attributes.registry;

import com.energizedblast.mna_attributes.Config;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

import static com.energizedblast.mna_attributes.MnAAttributes.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);
    public static final HashMap<RegistryObject<Attribute>, UUID> UUIDS = new HashMap<>();
    public static final RegistryObject<Attribute> MANA_REGEN = registerAttribute(ATTRIBUTES, MODID, "mana_regen", (id) -> new RangedAttribute(id, Config.startingManaRegenMultiplier, -2048.0D, 2.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MAX_MANA = registerAttribute(ATTRIBUTES, MODID, "max_mana", (id) -> new RangedAttribute(id, Config.startingMaxManaModifier, -Double.MAX_VALUE, Double.MAX_VALUE).setSyncable(true));
    // Separate multiplier attribute is required to allow for keeping track of multiplicative modifiers since the API currently supports only additive modifiers or set operations.
    public static final RegistryObject<Attribute> MAX_MANA_MULTIPLIER = registerAttribute(ATTRIBUTES, MODID, "max_mana_multiplier", (id) -> new RangedAttribute(id, Config.startingMaxManaMultiplier, 0.0D, 2048.0D).setSyncable(true));
    public static final RegistryObject<Attribute> CAST_SPEED = registerAttribute(ATTRIBUTES, MODID, "cast_speed", (id) -> new RangedAttribute(id, Config.startingCastSpeedMultiplier, 0.001D, 2048.0D).setSyncable(true));

    // Spell Adjuster Attributes
    public static final RegistryObject<Attribute> SPELL_EFFICIENCY = registerAttribute(ATTRIBUTES, MODID, "spell_efficiency", (id) -> new RangedAttribute(id, Config.startingSpellEfficiencyMultiplier, -2048.0D, 2048.0D).setSyncable(true));
    public static final RegistryObject<Attribute> SPELL_DAMAGE = registerAttribute(ATTRIBUTES, MODID, "spell_damage", (id) -> new RangedAttribute(id, Config.startingSpellDamageModifier, -2048.0D, 2048.0D).setSyncable(true));
    public static final RegistryObject<Attribute> SPELL_DAMAGE_MULTIPLIER = registerAttribute(ATTRIBUTES, MODID, "spell_damage_multiplier", (id) -> new RangedAttribute(id, Config.startingSpellDamageMultiplier, 0.0D, 2048.0D).setSyncable(true));

    // Affinity Attributes
    public static final RegistryObject<Attribute> AFFINITY_ARCANE = registerAttribute(ATTRIBUTES, MODID, "affinity_arcane", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 100.0D).setSyncable(true));
    public static final RegistryObject<Attribute> AFFINITY_EARTH = registerAttribute(ATTRIBUTES, MODID, "affinity_earth", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 100.0D).setSyncable(true));
    public static final RegistryObject<Attribute> AFFINITY_ENDER = registerAttribute(ATTRIBUTES, MODID, "affinity_ender", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 100.0D).setSyncable(true));
    public static final RegistryObject<Attribute> AFFINITY_FIRE = registerAttribute(ATTRIBUTES, MODID, "affinity_fire", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 100.0D).setSyncable(true));
    public static final RegistryObject<Attribute> AFFINITY_WATER = registerAttribute(ATTRIBUTES, MODID, "affinity_water", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 100.0D).setSyncable(true));
    public static final RegistryObject<Attribute> AFFINITY_WIND = registerAttribute(ATTRIBUTES, MODID, "affinity_wind", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 100.0D).setSyncable(true));


    public static RegistryObject<Attribute> registerAttribute(DeferredRegister<Attribute> registry, String modId, String name, Function<String, Attribute> attribute) {
        RegistryObject<Attribute> registryObject = registry.register(name, () -> attribute.apply("attribute.name." + modId + "." + name));
        UUIDS.put(registryObject, UUID.randomUUID());
        return registryObject;
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(e -> {
            event.add(e, MANA_REGEN.get());
            event.add(e, MAX_MANA.get());
            event.add(e, MAX_MANA_MULTIPLIER.get());
            event.add(e, CAST_SPEED.get());
            event.add(e, SPELL_EFFICIENCY.get());
            event.add(e, SPELL_DAMAGE.get());
            event.add(e, SPELL_DAMAGE_MULTIPLIER.get());

            event.add(e, AFFINITY_ARCANE.get());
            event.add(e, AFFINITY_EARTH.get());
            event.add(e, AFFINITY_ENDER.get());
            event.add(e, AFFINITY_FIRE.get());
            event.add(e, AFFINITY_WATER.get());
            event.add(e, AFFINITY_WIND.get());
        });
    }

    // Feels like this can already be solved with a builtin method, please help if there is already
    public static boolean isMnaAttribute(Attribute attr)
    {
        for (RegistryObject<Attribute> iAttr : ATTRIBUTES.getEntries())
        {
            if (attr == iAttr.get()) return true;
        }
        return false;
    }
}