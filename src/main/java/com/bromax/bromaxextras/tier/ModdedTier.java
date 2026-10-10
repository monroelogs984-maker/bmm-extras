// All `uses` values are 3× the source mod's tier — BMM weapons are expensive to craft.
// Any new tiers added in future compat files must follow the same ×3 rule.
package com.bromax.bromaxextras.tier;

import com.bromax.bromaxmelees.weapon.IBMMTier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * A modded material tier. Core stats are fixed at construction; material bonuses are
 * attached with the fluent setters and applied by {@code BonusEvents} / {@code ExtrasWeapon}
 * (never through BMMWeapon.hurtEnemy, which BHB's attack pipeline bypasses).
 *
 * Every bonus must also declare its tooltip lines via {@link #bonusLines(int)} —
 * lang keys are {@code tooltip.bmm_extras.<name>.bonus.<i>}.
 */
public final class ModdedTier implements IBMMTier {

    /** Runs after the hit landed; {@code dealt} is the post-armor damage actually applied. */
    @FunctionalInterface
    public interface HitEffect { void apply(LivingEntity target, LivingEntity attacker, float dealt); }

    /** Adjusts incoming (pre-armor) melee damage. */
    @FunctionalInterface
    public interface DamageModifier { float apply(LivingEntity target, LivingEntity attacker, float amount); }

    /** Runs every server tick while the weapon is in a player's inventory. */
    @FunctionalInterface
    public interface InventoryEffect { void tick(ItemStack stack, Player player, boolean selected); }

    public record AttributeBonus(ResourceLocation attribute, double amount) {}
    public record CraftEnchant(ResourceKey<Enchantment> enchantment, int level) {}

    private final String prefix;
    private final TagKey<Block> incorrectBlocks;
    private final int uses;
    private final float miningSpeed;
    private final int enchantmentValue;
    private final float baseDps;
    private ResourceLocation repairItem;
    private TagKey<net.minecraft.world.item.Item> repairTag;

    private HitEffect hitEffect;
    private DamageModifier damageModifier;
    private InventoryEffect inventoryEffect;
    private int lootingBonus;
    private boolean persistent;
    private final List<AttributeBonus> attributeBonuses = new ArrayList<>();
    private final List<CraftEnchant> craftEnchants = new ArrayList<>();
    private int bonusLines;

    public ModdedTier(String prefix, TagKey<Block> incorrectBlocks,
                      int uses, float miningSpeed, int enchantmentValue, float baseDps) {
        this.prefix = prefix;
        this.incorrectBlocks = incorrectBlocks;
        this.uses = uses;
        this.miningSpeed = miningSpeed;
        this.enchantmentValue = enchantmentValue;
        this.baseDps = baseDps;
    }

    public ModdedTier(String prefix, TagKey<Block> incorrectBlocks,
                      int uses, float miningSpeed, int enchantmentValue, float baseDps,
                      String repairNamespace, String repairPath) {
        this(prefix, incorrectBlocks, uses, miningSpeed, enchantmentValue, baseDps);
        repairItem(repairNamespace, repairPath);
    }

    // ---- fluent configuration ----------------------------------------------------------

    public ModdedTier repairItem(String namespace, String path) {
        this.repairItem = ResourceLocation.fromNamespaceAndPath(namespace, path);
        return this;
    }

    public ModdedTier repairTag(String namespace, String path) {
        this.repairTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
        return this;
    }

    public ModdedTier onHit(HitEffect effect)              { this.hitEffect = effect; return this; }
    public ModdedTier damageModifier(DamageModifier mod)   { this.damageModifier = mod; return this; }
    public ModdedTier inventoryEffect(InventoryEffect eff) { this.inventoryEffect = eff; return this; }
    public ModdedTier looting(int bonus)                   { this.lootingBonus = bonus; return this; }
    /** Goety-style: never breaks, stops at 1 durability and loses its bonuses until repaired. */
    public ModdedTier persistent()                         { this.persistent = true; return this; }
    public ModdedTier bonusLines(int lines)                { this.bonusLines = lines; return this; }

    public ModdedTier attribute(String namespace, String path, double amount) {
        attributeBonuses.add(new AttributeBonus(ResourceLocation.fromNamespaceAndPath(namespace, path), amount));
        return this;
    }

    public ModdedTier craftEnchant(ResourceKey<Enchantment> enchantment, int level) {
        craftEnchants.add(new CraftEnchant(enchantment, level));
        return this;
    }

    // ---- accessors ---------------------------------------------------------------------

    public String prefix() { return prefix; }
    /** Tier name used for lang keys, e.g. "fiery" for prefix "fiery_". */
    public String name() { return prefix.substring(0, prefix.length() - 1); }

    public int bonusLines()                        { return bonusLines; }
    public boolean isPersistent()                  { return persistent; }
    public List<AttributeBonus> attributeBonuses() { return attributeBonuses; }

    public void onHit(LivingEntity target, LivingEntity attacker, float dealt) {
        if (hitEffect != null) hitEffect.apply(target, attacker, dealt);
    }

    public float modifyDamage(LivingEntity target, LivingEntity attacker, float amount) {
        return damageModifier != null ? damageModifier.apply(target, attacker, amount) : amount;
    }

    public void inventoryTick(ItemStack stack, Player player, boolean selected) {
        if (inventoryEffect != null) inventoryEffect.tick(stack, player, selected);
    }

    public void applyCraftEnchants(ItemStack stack, Level level) {
        for (CraftEnchant ce : craftEnchants) {
            level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                    .getHolder(ce.enchantment())
                    .ifPresent(h -> {
                        if (stack.getEnchantmentLevel(h) < ce.level()) stack.enchant(h, ce.level());
                    });
        }
    }

    public static Holder<Attribute> attributeHolder(ResourceLocation id) {
        return BuiltInRegistries.ATTRIBUTE.getHolder(id).orElse(null);
    }

    // ---- IBMMTier ----------------------------------------------------------------------
    // applyHitEffect / getDamageMultiplier are intentionally NOT overridden: they are driven
    // from BMMWeapon.hurtEnemy, which never runs under BHB. BonusEvents applies them instead.

    @Override public float getBaseDps()                         { return baseDps; }
    @Override public int getUses()                              { return uses; }
    @Override public float getSpeed()                           { return miningSpeed; }
    @Override public float getAttackDamageBonus()               { return 0.0f; }
    @Override public TagKey<Block> getIncorrectBlocksForDrops() { return incorrectBlocks; }
    @Override public int getEnchantmentValue()                  { return enchantmentValue; }
    @Override public int getLootingBonus()                      { return lootingBonus; }

    @Override
    public Ingredient getRepairIngredient() {
        if (repairTag != null) return Ingredient.of(repairTag);
        if (repairItem == null) return Ingredient.of(Items.AIR);
        return BuiltInRegistries.ITEM.getOptional(repairItem)
                .map(Ingredient::of)
                .orElseGet(() -> Ingredient.of(Items.AIR));
    }
}
