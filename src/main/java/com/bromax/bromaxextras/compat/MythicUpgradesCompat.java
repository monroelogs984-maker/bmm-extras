package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.tags.BlockTags;
import net.neoforged.fml.ModList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Mythic Upgrades — all eight gem tiers are netherite upgrades (template + netherite + gem).
 * Stats mirror MythicToolMaterials (2031 uses, 9.0 speed, +4 dmg, ench 15, netherite mining);
 * sword damage = 1 + 3 + 4 = 8 → baseDps 12.8 (Sapphire sword uses +4 bonus → 9 → 14.4).
 *
 * Hit effects mirror Mythic's LivingEntityMixin tool branch, which only recognises Mythic's own
 * tool items. Effects and damage types are looked up from Mythic's registries at runtime, so
 * their death-triggered payoffs (Ice Bomb burst, Lethal Incubation, Charged lightning) still fire.
 */
public final class MythicUpgradesCompat {

    private static final String MU = "mythicupgrades";

    static final ModdedTier AQUAMARINE = gem("aquamarine", 12.8f).onHit(MythicUpgradesCompat::aquamarineHit).bonusLines(2);
    static final ModdedTier CITRINE    = gem("citrine",    12.8f).onHit(MythicUpgradesCompat::citrineHit).bonusLines(2);
    static final ModdedTier TOPAZ      = gem("topaz",      12.8f).onHit(MythicUpgradesCompat::topazHit).bonusLines(1);
    static final ModdedTier PERIDOT    = gem("peridot",    12.8f).onHit(MythicUpgradesCompat::peridotHit).bonusLines(1);
    static final ModdedTier RUBY       = gem("ruby",       12.8f).onHit(MythicUpgradesCompat::rubyHit).bonusLines(1);
    static final ModdedTier SAPPHIRE   = gem("sapphire",   14.4f).onHit(MythicUpgradesCompat::sapphireHit).bonusLines(1);
    static final ModdedTier JADE       = gem("jade",       12.8f).onHit(MythicUpgradesCompat::jadeHit).bonusLines(2);
    static final ModdedTier AMETRINE   = gem("ametrine",   12.8f).onHit(MythicUpgradesCompat::ametrineHit).bonusLines(2);

    private static ModdedTier gem(String name, float baseDps) {
        return new ModdedTier(name + "_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
                6093, 9.0f, 15, baseDps, MU, name + "_ingot");
    }

    public static void register() {
        if (!ModList.get().isLoaded(MU)) return;
        ExtrasItems.addTiers(AQUAMARINE, CITRINE, TOPAZ, PERIDOT, RUBY, SAPPHIRE, JADE, AMETRINE);
    }

    // ---- registry helpers --------------------------------------------------------------

    private static void addEffect(LivingEntity target, String id, int ticks, int amplifier) {
        Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT
                .getHolder(ResourceLocation.fromNamespaceAndPath(MU, id)).orElse(null);
        if (effect != null) target.addEffect(new MobEffectInstance(effect, ticks, amplifier));
    }

    private static DamageSource damage(LivingEntity attacker, String typeId) {
        ResourceKey<net.minecraft.world.damagesource.DamageType> key =
                ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MU, typeId));
        return attacker.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolder(key)
                .map(h -> new DamageSource(h, attacker, attacker))
                .orElseGet(() -> attacker.damageSources().indirectMagic(attacker, attacker));
    }

    // Same as Mythic's randomTeleportNear: up to 16 tries within ±8 blocks.
    private static void randomTeleportNear(LivingEntity entity) {
        for (int i = 0; i < 16; i++) {
            double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
            double y = Mth.clamp(entity.getY() + (entity.getRandom().nextInt(16) - 8),
                    entity.level().getMinBuildHeight(), entity.level().getMaxBuildHeight() - 1);
            double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
            if (entity.randomTeleport(x, y, z, true)) break;
        }
    }

    // ---- hit effects (values from MythicStats) -----------------------------------------

    // Freeze 18t + Slowness I 3s + Ice Bomb 3s (bursts for 4 dmg in 5 blocks if the target dies)
    private static void aquamarineHit(LivingEntity target, LivingEntity attacker, float dealt) {
        addEffect(target, "freeze", 18, 0);
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));
        addEffect(target, "ice_bomb", 60, 0);
    }

    // Charged 3s + Freeze 5t; chain lightning for 1/3 of the damage to the 3 nearest mobs within 10 blocks
    private static void citrineHit(LivingEntity target, LivingEntity attacker, float dealt) {
        addEffect(target, "charged", 60, 0);
        addEffect(target, "freeze", 5, 0);
        if (dealt <= 0.0f || !(target.level() instanceof ServerLevel level)) return;
        float chain = dealt * 0.333f;
        AABB box = attacker.getBoundingBox().inflate(10.0);
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, box);
        nearby.sort(Comparator.comparingDouble(e -> e.distanceTo(target)));
        int hits = 0;
        for (LivingEntity e : nearby) {
            if (hits >= 3) break;
            if (e == target || e == attacker) continue;
            e.hurt(damage(attacker, "citrine_chain_damage_type"), chain);
            hits++;
        }
    }

    private static final Map<LivingEntity, Integer> TOPAZ_HITS = new WeakHashMap<>();

    // Every 3rd hit: shockwave, radius 5, 1.25 damage + 1.25 knockback (effective level 5).
    // Mythic's version also shocks the wielder if they're in range — skipped here.
    private static void topazHit(LivingEntity target, LivingEntity attacker, float dealt) {
        int count = TOPAZ_HITS.getOrDefault(attacker, 0) + 1;
        if (count < 3) { TOPAZ_HITS.put(attacker, count); return; }
        TOPAZ_HITS.put(attacker, 0);
        float radius = 5.0f;
        AABB box = target.getBoundingBox().inflate(radius);
        for (LivingEntity e : target.level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (e == attacker || e.distanceTo(target) > radius) continue;
            e.hurt(damage(attacker, "topaz_shock_damage_type"), 1.25f);
            e.knockback(1.25, target.getX() - e.getX(), target.getZ() - e.getZ());
        }
    }

    // Lethal Incubation III for 5s (poison burst on death)
    private static void peridotHit(LivingEntity target, LivingEntity attacker, float dealt) {
        addEffect(target, "lethal_incubation", 100, 2);
    }

    // Lifesteal: heal 30% of damage dealt + 1
    private static void rubyHit(LivingEntity target, LivingEntity attacker, float dealt) {
        if (dealt > 0.0f) attacker.heal(dealt * 0.3f + 1.0f);
    }

    // Bonus true damage = 5% of the target's max health, capped at 15.
    // Invulnerability frames are cleared for this one hit — otherwise the bonus is swallowed
    // by the i-frames the main hit just set.
    private static void sapphireHit(LivingEntity target, LivingEntity attacker, float dealt) {
        if (!target.isAlive()) return;
        float bonus = Math.min(target.getMaxHealth() * 0.05f, 15.0f);
        int iframes = target.invulnerableTime;
        target.invulnerableTime = 0;
        target.hurt(damage(attacker, "percentage_damage_type"), bonus);
        target.invulnerableTime = Math.max(iframes, target.invulnerableTime);
    }

    // Jade Aura V on the wielder for 1s; 5% chance to teleport the target away
    private static void jadeHit(LivingEntity target, LivingEntity attacker, float dealt) {
        addEffect(attacker, "jade_aura", 20, 4);
        if (target.getRandom().nextFloat() < 0.05f) randomTeleportNear(target);
    }

    // Levitation III for 1s; 5% chance to teleport the target away
    private static void ametrineHit(LivingEntity target, LivingEntity attacker, float dealt) {
        target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20, 2));
        if (target.getRandom().nextFloat() < 0.05f) randomTeleportNear(target);
    }

    private MythicUpgradesCompat() {}
}
