package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

public final class RootsCompat {

    // RootsAPI.RUNED_TIER: 2031 uses, 9.0 speed, +4 dmg, ench 15, netherite mining. No bonus.
    // damage = 1 + 3 + 4 = 8 at speed 1.6 → baseDps=12.8
    static final ModdedTier RUNED = new ModdedTier(
            "runed_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            6093, 9.0f, 15, 12.8f)
            .repairTag("roots", "runed_obsidian");

    // RootsAPI.LIVING_TOOL_TIER: 300 uses, 6.0 speed, +2 dmg, ench 19, iron mining, repaired with logs
    // damage = 1 + 3 + 2 = 6 at speed 1.6 → baseDps=9.6
    // Bonus mirrors ILivingRepair: 1-in-40 chance per tick to regain 1 durability while in the inventory.
    static final ModdedTier LIVING = new ModdedTier(
            "living_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            900, 6.0f, 19, 9.6f)
            .repairTag("minecraft", "logs")
            .inventoryEffect(RootsCompat::livingRepair)
            .bonusLines(1);

    public static void register() {
        if (!ModList.get().isLoaded("roots")) return;
        ExtrasItems.addTiers(RUNED, LIVING);
    }

    private static void livingRepair(ItemStack stack, Player player, boolean selected) {
        if (stack.isDamaged() && player.getRandom().nextInt(40) == 0) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    private RootsCompat() {}
}
