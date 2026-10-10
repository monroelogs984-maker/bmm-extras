package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

public final class DruidcraftCompat {

    // Bone uses vanilla minecraft:bone as repair material (druidcraftrg uses mc bones directly)
    // damage=5.0 at speed 1.6 → baseDps=8.0
    static final ModdedTier BONE = new ModdedTier(
            "bone_", BlockTags.INCORRECT_FOR_STONE_TOOL,
            750, 5.0f, 18, 8.0f,
            "minecraft", "bone");

    // Moonstone is druidcraftrg:moonstone
    // damage=8.0 at speed 1.6 → baseDps=12.8
    static final ModdedTier MOONSTONE = new ModdedTier(
            "moonstone_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            5763, 9.0f, 15, 12.8f,
            "druidcraftrg", "moonstone");


    public static void register() {
        if (!ModList.get().isLoaded("druidcraftrg")) return;
        ExtrasItems.addTiers(BONE, MOONSTONE);
    }

    private DruidcraftCompat() {}
}
