package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.BromaxExtras;
import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;

public final class GoetyCompat {

    // Dark Alloy ("Dark Steel") — ModTiers.DARK: 166 uses, 8.0 speed, +3 dmg, ench 20, netherite mining
    // damage = 1 + 3 + 3 = 7 at speed 1.6 → baseDps=11.2; uses 166×3 = 498 (low, but it never breaks)
    // Bonuses mirror Goety's dark tools:
    //   - Persist: stops at 1 durability instead of breaking (DarkSwordItem.damageItem)
    //   - Soul Repair: slowly repairs itself with Soul Energy (ItemEvents → ItemHelper.repairTick)
    static final ModdedTier DARK_ALLOY = new ModdedTier(
            "dark_alloy_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            498, 8.0f, 20, 11.2f,
            "goety", "dark_ingot")
            .persistent()
            .inventoryEffect(GoetyCompat::soulRepair)
            .bonusLines(2);

    public static void register() {
        if (!ModList.get().isLoaded("goety")) return;
        ExtrasItems.addTiers(DARK_ALLOY);
    }

    // Goety's own repair routine (config-gated, costs Soul Energy, 1 durability / second).
    // Called reflectively so BMM Extras doesn't need Goety at compile time.
    private static Method repairTick;
    private static boolean lookedUp;

    private static void soulRepair(ItemStack stack, Player player, boolean selected) {
        if (!lookedUp) {
            lookedUp = true;
            try {
                repairTick = Class.forName("com.Polarice3.Goety.utils.ItemHelper")
                        .getMethod("repairTick", ItemStack.class, Entity.class, boolean.class);
            } catch (ReflectiveOperationException e) {
                BromaxExtras.LOGGER.warn("Goety ItemHelper.repairTick not found — Dark Alloy soul repair disabled", e);
            }
        }
        if (repairTick == null) return;
        try {
            repairTick.invoke(null, stack, player, selected);
        } catch (ReflectiveOperationException e) {
            BromaxExtras.LOGGER.warn("Goety soul repair failed — disabling", e);
            repairTick = null;
        }
    }

    private GoetyCompat() {}
}
