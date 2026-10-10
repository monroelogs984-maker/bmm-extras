package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

public final class TwilightForestCompat {

    // Knockback I on hit — mirrors TF ironwood_sword recipe (enchantments: {knockback:1})
    // damage=6.0 at speed 1.6 → baseDps=9.6
    static final ModdedTier IRONWOOD = new ModdedTier(
            "ironwood_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            1536, 6.5f, 25, 9.6f,
            "twilightforest", "ironwood_ingot")
            .onHit(TwilightForestCompat::ironwoodHit)
            .bonusLines(1);

    // Looting II — mirrors TF steeleaf_sword recipe (enchantments: {looting:2})
    // damage=7.0 at speed 1.6 → baseDps=11.2; durability=131 (fragile by design)
    static final ModdedTier STEELEAF = new ModdedTier(
            "steeleaf_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            393, 8.0f, 9, 11.2f,
            "twilightforest", "steeleaf_ingot")
            .looting(2)
            .bonusLines(1);

    // +2 damage to armored targets (scaled by armor cover) — mirrors TF ToolEvents.doKnightmetalToolLogic,
    // which only checks TF's own sword item, so BMM has to apply it itself.
    // damage=7.0 at speed 1.6 → baseDps=11.2
    static final ModdedTier KNIGHTMETAL = new ModdedTier(
            "knightmetal_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            1536, 8.0f, 8, 11.2f,
            "twilightforest", "knightmetal_ingot")
            .damageModifier(TwilightForestCompat::knightmetalBonus)
            .bonusLines(1);

    // 15-second fire on hit — mirrors FierySwordItem.hurtEnemy
    // damage=8.0 at speed 1.6 → baseDps=12.8
    static final ModdedTier FIERY = new ModdedTier(
            "fiery_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3072, 9.0f, 10, 12.8f,
            "twilightforest", "fiery_ingot")
            .onHit(TwilightForestCompat::fieryHit)
            .bonusLines(1);

    public static void register() {
        if (!ModList.get().isLoaded("twilightforest")) return;
        ExtrasItems.addTiers(IRONWOOD, STEELEAF, KNIGHTMETAL, FIERY);
    }

    private static void ironwoodHit(LivingEntity target, LivingEntity attacker, float dealt) {
        target.knockback(0.5, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }

    private static void fieryHit(LivingEntity target, LivingEntity attacker, float dealt) {
        if (!target.fireImmune()) target.igniteForSeconds(15.0f);
    }

    private static float knightmetalBonus(LivingEntity target, LivingEntity attacker, float amount) {
        if (target.getArmorValue() <= 0) return amount;
        float cover = target.getArmorCoverPercentage();
        return amount + (cover > 0.0f ? (int) (2.0f * cover) : 2.0f);
    }

    private TwilightForestCompat() {}
}
