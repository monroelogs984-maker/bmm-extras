package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

public final class AbyssborneCompat {

    // AbyssToolMaterials: 1506 uses, 10.0 speed, +4 dmg, ench 21. No weapon bonus in Abyssborne.
    // Sword damage = 1 + 5 + 4 = 10 at speed 1.6 → baseDps=16.0 (above netherite's 12.8).
    // Abyssborne passes NEEDS_DIAMOND_TOOL as its incorrect-blocks tag (a bug that blocks diamond-level
    // drops); diamond-level mining is used here as the evident intent.
    static final ModdedTier CRIMSONITE = new ModdedTier(
            "crimsonite_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            4518, 10.0f, 21, 16.0f,
            "abyssborne", "crimsonite_ingot");

    static final ModdedTier AMARYLLIUM = new ModdedTier(
            "amaryllium_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            4518, 10.0f, 21, 16.0f,
            "abyssborne", "amaryllium_ingot");

    public static void register() {
        if (!ModList.get().isLoaded("abyssborne")) return;
        ExtrasItems.addTiers(CRIMSONITE, AMARYLLIUM);
    }

    private AbyssborneCompat() {}
}
