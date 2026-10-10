package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;

public final class BetterNetherCompat {

    // nether_ruby is a gem — item ID is betternether:nether_ruby (no suffix)
    // No special hit effect — plain stat sword. (Fire only applies to the Flaming Ruby smithing upgrade.)
    // damage=7.1 at speed 1.6 → baseDps=11.36
    static final ModdedTier NETHER_RUBY = new ModdedTier(
            "nether_ruby_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            7683, 7.1f, 18, 11.36f,
            "betternether", "nether_ruby");

    // damage=6.5 at speed 1.6 → baseDps=10.4
    static final ModdedTier CINCINNASITE = new ModdedTier(
            "cincinnasite_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            1536, 6.2f, 16, 10.4f,
            "betternether", "cincinnasite_ingot");

    // Smithing upgrade of nether_ruby_sword via sculk_catalyst + template — netherite-equivalent tier
    // Effects mirror the ruby_fire enchantment:
    //   - Knockback II equivalent on every hit
    //   - 25% chance: attacker takes indirect magic damage (2–7) + ignites for 5 seconds (self-burn risk)
    // damage=10.0 at speed 1.6 → baseDps=16.0
    static final ModdedTier FLAMING_RUBY = new ModdedTier(
            "flaming_ruby_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            8583, 10.4f, 32, 16.0f,
            "minecraft", "sculk_catalyst")
            .onHit(BetterNetherCompat::flamingRubyHit)
            .bonusLines(2);


    public static void register() {
        if (!ModList.get().isLoaded("betternether")) return;
        ExtrasItems.addTiers(NETHER_RUBY, CINCINNASITE, FLAMING_RUBY);
    }

    private static void flamingRubyHit(LivingEntity target, LivingEntity attacker, float dealt) {
        // Knockback II equivalent (ruby_fire enchant: knockback 1.0 base at level 1)
        target.knockback(1.0, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        // 25% chance: attacker takes indirect magic damage + self-ignite (matches ruby_fire post_attack JSON)
        if (attacker.getRandom().nextFloat() < 0.25f) {
            float selfDmg = 2.0f + attacker.getRandom().nextFloat() * 5.0f;
            attacker.hurt(attacker.level().damageSources().magic(), selfDmg);
            attacker.setRemainingFireTicks(100);
        }
    }

    private BetterNetherCompat() {}
}
