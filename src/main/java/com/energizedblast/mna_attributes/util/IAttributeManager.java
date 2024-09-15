package com.energizedblast.mna_attributes.util;

import org.jetbrains.annotations.ApiStatus;

/**
 * Credit goes to Shadows-of-Fire for this implementation from AttributesLib (Apothic Attributes).
 *
 * A manager for handling weird attribute logic within Minecraft.
 */
@ApiStatus.Internal
public interface IAttributeManager {

    /**
     * {@return whether the attributes are being updated, instead of added or removed}
     */
    boolean areAttributesUpdating();

    /**
     * Sets whether the attributes are being updated, instead of added or removed.
     *
     * @param updating whether the attributes are being updated, instead of added or removed
     */
    void setAttributesUpdating(boolean updating);
}
