package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

public final class WyrmroostCompat {

    // Platinum is iron-mining-level with 0 attack bonus — a utility/crafting metal in WR, not a combat material
    // damage=6.0 at speed 1.6 → baseDps=9.6
    static final ModdedTier PLATINUM = new ModdedTier(
            "platinum_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            1275, 6.5f, 18, 9.6f,
            "wyrmroost", "platinum_ingot");

    // damage=6.5 at speed 1.6 → baseDps=10.4
    static final ModdedTier BLUE_GEODE = new ModdedTier(
            "blue_geode_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            5196, 7.5f, 20, 10.4f,
            "wyrmroost", "blue_geode");

    // damage=7.5 at speed 1.6 → baseDps=12.0
    static final ModdedTier RED_GEODE = new ModdedTier(
            "red_geode_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            6093, 8.5f, 20, 12.0f,
            "wyrmroost", "red_geode");

    // damage=9.0 at speed 1.6 → baseDps=14.4
    static final ModdedTier PURPLE_GEODE = new ModdedTier(
            "purple_geode_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            12642, 10.0f, 25, 14.4f,
            "wyrmroost", "purple_geode");


    public static void register() {
        if (!ModList.get().isLoaded("wyrmroost")) return;
        ExtrasItems.addTiers(PLATINUM, BLUE_GEODE, RED_GEODE, PURPLE_GEODE);
    }

    private WyrmroostCompat() {}
}
