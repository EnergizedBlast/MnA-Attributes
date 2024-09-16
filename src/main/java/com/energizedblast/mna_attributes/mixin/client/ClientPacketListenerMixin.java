package com.energizedblast.mna_attributes.mixin.client;

import java.util.Iterator;

import com.energizedblast.mna_attributes.events.MnAAttributeChangedValueEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket.AttributeSnapshot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.common.MinecraftForge;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
 */

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    /**
     * Records the old value of the attribute before the attribute packet begins applying new clientside modifiers
     * in {@link ClientPacketListener#handleUpdateAttributes(ClientboundUpdateAttributesPacket)}.
     */
    private double mnaAttributes_lastValue;

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/ai/attributes/AttributeInstance.setBaseValue(D)V"), method = "handleUpdateAttributes(Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket;)V", require = 1, locals = LocalCapture.CAPTURE_FAILHARD)
    public void mnaAttributes_recordOldAttrValue(ClientboundUpdateAttributesPacket packet, CallbackInfo ci, Entity entity, AttributeMap map, Iterator<AttributeSnapshot> it, AttributeSnapshot snapshot, AttributeInstance inst) {
        this.mnaAttributes_lastValue = inst.getValue();
    }

    /**
     * Injected after the for loop iterating {@link AttributeSnapshot#getModifiers()}, which is when after all client attribute modifiers have been cleared and
     * reapplied.
     * <p>
     * Responsible for comparing {@link #mnaAttributes_lastValue} to the new value, and posting {@link MnAAttributeChangedValueEvent} if necessary.<br>
     * This is required due to how attributes are synced, since all modifiers are cleared and reapplied instead of only adding/removing modifiers.
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;addTransientModifier(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;)V", shift = At.Shift.BY, by = 5), method = "handleUpdateAttributes(Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket;)V", require = 1, locals = LocalCapture.CAPTURE_FAILHARD)
    public void mnaAttributes_postAttrChangedEvent(ClientboundUpdateAttributesPacket packet, CallbackInfo ci, Entity entity, AttributeMap map, Iterator<AttributeSnapshot> it, AttributeSnapshot snapshot, @Nullable AttributeInstance inst) {
        if (inst != null) { // Due to the loop semantics, the injection point is also the point where the nullcheck will jump to, so we can receive null.
            double newValue = inst.getValue();
            if (newValue != mnaAttributes_lastValue) {
                MinecraftForge.EVENT_BUS.post(new MnAAttributeChangedValueEvent((LivingEntity) entity, inst, mnaAttributes_lastValue, newValue));
            }
        }
    }
}
