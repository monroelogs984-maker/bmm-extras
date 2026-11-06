package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

/**
 * Eternal Starlight tiers. Sword damage = ES sword attack + 1 (player base), baseDps = damage x 1.6.
 * Uses are 3x ES's tool durability. Flowglaze and Starfire are smithing upgrades, as in ES itself
 * (Glacite -> Flowglaze, Thermal Springstone -> Starfire).
 */
public final class EternalStarlightCompat {

    private static final String ES = "eternal_starlight";

    // ES sword 5.0 -> 6 dmg -> 9.6 (iron level)
    static final ModdedTier AMARAMBER = new ModdedTier("amaramber_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            750, 6.0f, 14, 9.6f).repairTag("c", "ingots/amaramber");
    static final ModdedTier DEEPSILVER = new ModdedTier("deepsilver_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            2400, 7.0f, 14, 9.6f).repairTag("c", "ingots/deepsilver");
    static final ModdedTier MALARITE = new ModdedTier("malarite_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            1200, 6.5f, 14, 9.6f).repairTag("c", "gems/malarite");
    static final ModdedTier THERMAL_SPRINGSTONE = new ModdedTier("thermal_springstone_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            1200, 6.5f, 12, 9.6f).repairTag("c", "ingots/thermal_springstone");

    // ES sword 5.5 -> 6.5 dmg -> 10.4
    static final ModdedTier GLACITE = new ModdedTier("glacite_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1500, 7.0f, 15, 10.4f).repairTag("c", "gems/glacite");
    static final ModdedTier FLOWGLAZE = new ModdedTier("flowglaze_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3000, 8.0f, 15, 10.4f, ES, "flowglaze");
    static final ModdedTier STARFIRE = new ModdedTier("starfire_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3000, 8.0f, 15, 10.4f, ES, "starfire");

    // ES sword 7.0 -> 8 dmg -> 12.8 (netherite level)
    static final ModdedTier UNREALIUM = new ModdedTier("unrealium_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            3600, 9.0f, 16, 12.8f).repairTag("c", "ingots/unrealium");
    static final ModdedTier STARLIT_DIAMOND = new ModdedTier("starlit_diamond_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            7500, 9.0f, 18, 12.8f).repairTag("c", "gems/starlit_diamond");

    public static void register() {
        if (!ModList.get().isLoaded(ES)) return;
        ExtrasItems.addTiers(AMARAMBER, DEEPSILVER, MALARITE, THERMAL_SPRINGSTONE, GLACITE, FLOWGLAZE, STARFIRE,
                UNREALIUM, STARLIT_DIAMOND);
    }

    private EternalStarlightCompat() {}
}
