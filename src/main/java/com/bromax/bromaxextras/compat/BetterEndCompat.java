package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

public final class BetterEndCompat {

    // damage=5.5 at speed 1.6 → baseDps=8.8
    static final ModdedTier THALLASIUM = new ModdedTier(
            "thallasium_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            960, 7.0f, 12, 8.8f,
            "betterend", "thallasium_ingot");

    // damage=7.0 at speed 1.6 → baseDps=11.2
    static final ModdedTier TERMINITE = new ModdedTier(
            "terminite_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3690, 8.5f, 14, 11.2f,
            "betterend", "terminite_ingot");

    // Above-netherite tier — BE uses a custom INCORRECT_FOR_AETERNIUM_TOOL tag; approximated as netherite-level
    // damage=8.5 at speed 1.6 → baseDps=13.6
    static final ModdedTier AETERNIUM = new ModdedTier(
            "aeternium_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            6588, 10.0f, 18, 13.6f,
            "betterend", "aeternium_ingot");


    public static void register() {
        if (!ModList.get().isLoaded("betterend")) return;
        ExtrasItems.addTiers(THALLASIUM, TERMINITE, AETERNIUM);
    }

    private BetterEndCompat() {}
}
