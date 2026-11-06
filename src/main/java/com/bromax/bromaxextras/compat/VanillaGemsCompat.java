package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;

/**
 * Emerald and Lapis, using Opposing Force's tool tiers (always registered: vanilla materials).
 * Emerald = diamond stats (1561 uses x3). Lapis = weak but highly enchantable (OF: 200 uses, +0 damage,
 * enchantability 25), set at BMM's wood-equivalent floor like Agate.
 */
public final class VanillaGemsCompat {

    static final ModdedTier EMERALD = new ModdedTier("emerald_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            4683, 8.0f, 15, 11.2f, "minecraft", "emerald");
    static final ModdedTier LAPIS = new ModdedTier("lapis_", BlockTags.INCORRECT_FOR_STONE_TOOL,
            600, 7.0f, 25, 8.0f, "minecraft", "lapis_lazuli");

    public static void register() {
        ExtrasItems.addTiers(EMERALD, LAPIS);
    }

    private VanillaGemsCompat() {}
}
