// All `uses` values are 3× the source mod's tier — BMM weapons are expensive to craft.
// Any new tiers added in future compat files must follow the same ×3 rule.
package com.bromax.bromaxextras.tier;

import com.bromax.bromaxmelees.weapon.IBMMTier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ModdedTier implements IBMMTier {

    private final String prefix;
    private final TagKey<Block> incorrectBlocks;
    private final int uses;
    private final float miningSpeed;
    private final int enchantmentValue;
    private final float baseDps;
    private final String repairNamespace;
    private final String repairPath;
    private final BiConsumer<LivingEntity, LivingEntity> hitEffect;
    private final int lootingBonus;
    private final Function<LivingEntity, Float> damageMultiplier;

    public ModdedTier(String prefix, TagKey<Block> incorrectBlocks,
                      int uses, float miningSpeed, int enchantmentValue, float baseDps,
                      String repairNamespace, String repairPath) {
        this(prefix, incorrectBlocks, uses, miningSpeed, enchantmentValue, baseDps,
                repairNamespace, repairPath, null, 0, null);
    }

    public ModdedTier(String prefix, TagKey<Block> incorrectBlocks,
                      int uses, float miningSpeed, int enchantmentValue, float baseDps,
                      String repairNamespace, String repairPath,
                      BiConsumer<LivingEntity, LivingEntity> hitEffect, int lootingBonus) {
        this(prefix, incorrectBlocks, uses, miningSpeed, enchantmentValue, baseDps,
                repairNamespace, repairPath, hitEffect, lootingBonus, null);
    }

    public ModdedTier(String prefix, TagKey<Block> incorrectBlocks,
                      int uses, float miningSpeed, int enchantmentValue, float baseDps,
                      String repairNamespace, String repairPath,
                      BiConsumer<LivingEntity, LivingEntity> hitEffect, int lootingBonus,
                      Function<LivingEntity, Float> damageMultiplier) {
        this.prefix = prefix;
        this.incorrectBlocks = incorrectBlocks;
        this.uses = uses;
        this.miningSpeed = miningSpeed;
        this.enchantmentValue = enchantmentValue;
        this.baseDps = baseDps;
        this.repairNamespace = repairNamespace;
        this.repairPath = repairPath;
        this.hitEffect = hitEffect;
        this.lootingBonus = lootingBonus;
        this.damageMultiplier = damageMultiplier;
    }

    public String prefix() { return prefix; }

    @Override public float getBaseDps()                      { return baseDps; }
    @Override public int getUses()                           { return uses; }
    @Override public float getSpeed()                        { return miningSpeed; }
    @Override public float getAttackDamageBonus()            { return 0.0f; }
    @Override public TagKey<Block> getIncorrectBlocksForDrops() { return incorrectBlocks; }
    @Override public int getEnchantmentValue()               { return enchantmentValue; }
    @Override public int getLootingBonus()                   { return lootingBonus; }

    @Override
    public void applyHitEffect(LivingEntity target, LivingEntity attacker) {
        if (hitEffect != null) hitEffect.accept(target, attacker);
    }

    @Override
    public float getDamageMultiplier(LivingEntity target) {
        return damageMultiplier != null ? damageMultiplier.apply(target) : 1.0f;
    }

    @Override
    public Ingredient getRepairIngredient() {
        var opt = BuiltInRegistries.ITEM.getOptional(
                ResourceLocation.fromNamespaceAndPath(repairNamespace, repairPath));
        return opt.map(Ingredient::of).orElseGet(() -> Ingredient.of(Items.AIR));
    }
}
