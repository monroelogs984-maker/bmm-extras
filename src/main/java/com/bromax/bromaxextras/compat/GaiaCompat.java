package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

public final class GaiaCompat {

    // GaiaToolMaterials — plain BasicGaiaSwordItem swords, no bonuses. Sword damage = 1 + 3 + tier bonus.
    // Gaia's incorrect_for_* tags resolve to these vanilla tags.

    // Agate (wood-equivalent): 150 uses, 2.5 speed, +1 → 5 dmg → baseDps=8.0
    static final ModdedTier AGATE = new ModdedTier(
            "agate_", BlockTags.INCORRECT_FOR_STONE_TOOL,
            450, 2.5f, 5, 8.0f)
            .repairTag("gaiadimension", "agate_tiles");

    // Stibnite: 1500 uses, 4.0 speed, +2 → 6 dmg → baseDps=9.6
    static final ModdedTier STIBNITE = new ModdedTier(
            "stibnite_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            4500, 4.0f, 10, 9.6f,
            "gaiadimension", "stibnite");

    // Euclase: 3000 uses, 4.0 speed, +2 → 6 dmg → baseDps=9.6
    static final ModdedTier EUCLASE = new ModdedTier(
            "euclase_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            9000, 4.0f, 10, 9.6f,
            "gaiadimension", "euclase");

    // Carnelian: 2500 uses, 5.0 speed, +3 → 7 dmg → baseDps=11.2
    static final ModdedTier CARNELIAN = new ModdedTier(
            "carnelian_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            7500, 5.0f, 10, 11.2f,
            "gaiadimension", "carnelian");

    // Goshenite: 4000 uses, 6.0 speed, +4 → 8 dmg → baseDps=12.8
    static final ModdedTier GOSHENITE = new ModdedTier(
            "goshenite_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            12000, 6.0f, 10, 12.8f,
            "gaiadimension", "goshenite");

    public static void register() {
        if (!ModList.get().isLoaded("gaiadimension")) return;
        ExtrasItems.addTiers(AGATE, STIBNITE, EUCLASE, CARNELIAN, GOSHENITE);
    }

    private GaiaCompat() {}
}
