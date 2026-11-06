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

    // Benitoite: Gaia sword 6.0 -> 7 dmg -> 11.2
    static final ModdedTier BENITOITE = new ModdedTier(
            "benitoite_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            10500, 5.0f, 10, 11.2f,
            "gaiadimension", "benitoite");

    // Sugilite: Gaia sword 4.5 -> 5.5 dmg -> 8.8
    static final ModdedTier SUGILITE = new ModdedTier(
            "sugilite_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            2400, 4.0f, 10, 8.8f,
            "gaiadimension", "sugilite");

    // Albite / Proustite / Diopside are armour-only gems in Gaia; ranked from their armour
    // (Albite 4 + 1 toughness, Proustite 4 -> iron level; Diopside 5 + 1 -> diamond level).
    static final ModdedTier ALBITE = new ModdedTier(
            "albite_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            3000, 4.0f, 10, 9.6f,
            "gaiadimension", "albite");

    static final ModdedTier PROUSTITE = new ModdedTier(
            "proustite_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            3600, 4.0f, 10, 9.6f,
            "gaiadimension", "proustite");

    static final ModdedTier DIOPSIDE = new ModdedTier(
            "diopside_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            5400, 5.0f, 10, 11.2f,
            "gaiadimension", "diopside");

    public static void register() {
        if (!ModList.get().isLoaded("gaiadimension")) return;
        ExtrasItems.addTiers(AGATE, STIBNITE, EUCLASE, CARNELIAN, GOSHENITE,
                BENITOITE, SUGILITE, ALBITE, PROUSTITE, DIOPSIDE);
    }

    private GaiaCompat() {}
}
