package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

/**
 * Mystical Agriculture tiers. MA's own swords run far past netherite (Supremium ~24 damage), so these are
 * fitted into BMM's scale instead: Inferium at diamond, climbing to Awakened Supremium at the top tier (16 DPS,
 * level with Crimsonite). Uses are 3x MA's durability; Supremium/Awakened (unbreakable in MA) get 24000/30000.
 * Recipes upgrade the previous tier's weapon (Inferium upgrades BMM diamond) and keep its enchantments.
 */
public final class MysticalAgricultureCompat {

    private static final String MA = "mysticalagriculture";

    static final ModdedTier INFERIUM = tier("inferium", 6000, 9.0f, 12, 11.2f);
    static final ModdedTier PRUDENTIUM = tier("prudentium", 8400, 11.0f, 14, 12.0f);
    static final ModdedTier TERTIUM = tier("tertium", 12000, 14.0f, 16, 12.8f);
    static final ModdedTier IMPERIUM = tier("imperium", 18000, 19.0f, 18, 13.6f);
    static final ModdedTier SUPREMIUM = tier("supremium", 24000, 25.0f, 20, 15.2f);
    static final ModdedTier AWAKENED_SUPREMIUM = tier("awakened_supremium", 30000, 30.0f, 22, 16.0f);

    private static ModdedTier tier(String name, int uses, float speed, int enchant, float dps) {
        return new ModdedTier(name + "_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL, uses, speed, enchant, dps,
                MA, name + "_ingot");
    }

    public static void register() {
        if (!ModList.get().isLoaded(MA)) return;
        ExtrasItems.addTiers(INFERIUM, PRUDENTIUM, TERTIUM, IMPERIUM, SUPREMIUM, AWAKENED_SUPREMIUM);
    }

    private MysticalAgricultureCompat() {}
}
