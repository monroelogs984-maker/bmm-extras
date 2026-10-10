package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.fml.ModList;

public final class UnusualFishCompat {

    // "Ancient" — the Depth Scythe line (Ancient Weapon template + weapon parts + Depth Claw).
    // Tiers.DIAMOND with +3 → 7 dmg at speed 1.6 → baseDps=11.2; 600 uses ×3; enchantability 1.
    // Bonus mirrors DepthScytheItem.onCraftedBy: comes out of the smithing table with Sweeping Edge V.
    // (Fast cobweb cutting is already on every BMM weapon via SwordItem.)
    static final ModdedTier ANCIENT = new ModdedTier(
            "ancient_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1800, 8.0f, 1, 11.2f,
            "unusualfishmod", "depth_claw")
            .craftEnchant(Enchantments.SWEEPING_EDGE, 5)
            .bonusLines(1);

    public static void register() {
        if (!ModList.get().isLoaded("unusualfishmod")) return;
        ExtrasItems.addTiers(ANCIENT);
    }

    private UnusualFishCompat() {}
}
