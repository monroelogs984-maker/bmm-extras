package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import com.bromax.bromaxmelees.item.BMMWeapon;
import com.bromax.bromaxmelees.weapon.WeaponCategory;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

public final class TwilightForestCompat {

    // Knockback I on hit — mirrors TF ironwood_sword recipe (enchantments: {knockback:1})
    // damage=6.0 at speed 1.6 → baseDps=9.6
    static final ModdedTier IRONWOOD = new ModdedTier(
            "ironwood_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            1536, 6.5f, 25, 9.6f,
            "twilightforest", "ironwood_ingot",
            TwilightForestCompat::ironwoodHit, 0);

    // Looting II — mirrors TF steeleaf_sword recipe (enchantments: {looting:2})
    // damage=7.0 at speed 1.6 → baseDps=11.2; durability=131 (fragile by design)
    static final ModdedTier STEELEAF = new ModdedTier(
            "steeleaf_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            393, 8.0f, 9, 11.2f,
            "twilightforest", "steeleaf_ingot",
            null, 2);

    // No special hit effect — knightmetal sword only adds a tooltip in TF
    // damage=7.0 at speed 1.6 → baseDps=11.2
    static final ModdedTier KNIGHTMETAL = new ModdedTier(
            "knightmetal_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1536, 8.0f, 8, 11.2f,
            "twilightforest", "knightmetal_ingot");

    // 15-second fire on hit — mirrors FierySwordItem.hurtEnemy
    // damage=8.0 at speed 1.6 → baseDps=12.8
    static final ModdedTier FIERY = new ModdedTier(
            "fiery_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3072, 9.0f, 10, 12.8f,
            "twilightforest", "fiery_ingot",
            TwilightForestCompat::fieryHit, 0);

    private static final ModdedTier[] TIERS = { IRONWOOD, STEELEAF, KNIGHTMETAL, FIERY };

    public static void register() {
        if (!ModList.get().isLoaded("twilightforest")) return;
        for (ModdedTier tier : TIERS) {
            for (WeaponCategory cat : WeaponCategory.values()) {
                final ModdedTier t = tier;
                final WeaponCategory c = cat;
                ExtrasItems.add(t.prefix() + c.id(), () -> new BMMWeapon(c, t));
            }
        }
    }

    private static void ironwoodHit(LivingEntity target, LivingEntity attacker) {
        target.knockback(0.5, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }

    private static void fieryHit(LivingEntity target, LivingEntity attacker) {
        if (!target.fireImmune()) target.setRemainingFireTicks(300);
    }

    private TwilightForestCompat() {}
}
