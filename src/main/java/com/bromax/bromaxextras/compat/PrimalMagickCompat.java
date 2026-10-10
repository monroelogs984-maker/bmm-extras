package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

public final class PrimalMagickCompat {

    // damage=6.5 at speed 1.6 → baseDps=10.4
    static final ModdedTier PRIMALITE = new ModdedTier(
            "primalite_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            2400, 7.5f, 18, 10.4f,
            "primalmagick", "primalite_ingot");

    // damage=8.0 at speed 1.6 → baseDps=12.8
    static final ModdedTier HEXIUM = new ModdedTier(
            "hexium_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            4800, 9.5f, 23, 12.8f,
            "primalmagick", "hexium_ingot");

    // damage=9.5 at speed 1.6 → baseDps=15.2
    static final ModdedTier HALLOWSTEEL = new ModdedTier(
            "hallowsteel_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            7200, 11.5f, 28, 15.2f,
            "primalmagick", "hallowsteel_ingot");


    public static void register() {
        if (!ModList.get().isLoaded("primalmagick")) return;
        ExtrasItems.addTiers(PRIMALITE, HEXIUM, HALLOWSTEEL);
    }

    private PrimalMagickCompat() {}
}
