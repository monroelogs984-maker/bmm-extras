package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import com.bromax.bromaxmelees.item.BMMWeapon;
import com.bromax.bromaxmelees.weapon.WeaponCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;

public final class UndergardenCompat {

    // damage=7.0 at speed 1.6 → baseDps=11.2; uses=286 (fragile iron-level material)
    static final ModdedTier CLOGGRUM = new ModdedTier(
            "cloggrum_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            858, 6.0f, 8, 11.2f,
            "undergarden", "cloggrum_ingot");

    // Applies UG's custom Chilly effect on hit (600 ticks, amplifier 2) — mirrors UG's froststeelAttackEvent
    // damage=6.0 at speed 1.6 → baseDps=9.6; iron-level mining despite special effect
    static final ModdedTier FROSTSTEEL = new ModdedTier(
            "froststeel_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            1725, 7.0f, 20, 9.6f,
            "undergarden", "froststeel_ingot",
            UndergardenCompat::froststeelHit, 0);

    // Utherium is a crystal, not an ingot — item ID is utherium_crystal
    // UG's 1.5x damage to Rotspawn is applied by UG's own event handler; no separate BMM effect
    // damage=7.5 at speed 1.6 → baseDps=12.0
    static final ModdedTier UTHERIUM = new ModdedTier(
            "utherium_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3837, 8.5f, 17, 12.0f,
            "undergarden", "utherium_crystal");

    // Smithing upgrade of Cloggrum — UG's rarest material
    // Effect: 1.5× damage to non-boss Undergarden mobs (via LivingIncomingDamageEvent, mirrors UG's forgottenAttackEvent)
    // damage=7.0 at speed 1.6 → baseDps=11.2
    static final ModdedTier FORGOTTEN = new ModdedTier(
            "forgotten_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            5628, 8.0f, 2, 11.2f,
            "undergarden", "forgotten_ingot",
            null, 0,
            UndergardenCompat::forgottenMultiplier);

    private static final ModdedTier[] TIERS = { CLOGGRUM, FROSTSTEEL, UTHERIUM, FORGOTTEN };

    public static void register() {
        if (!ModList.get().isLoaded("undergarden")) return;
        for (ModdedTier tier : TIERS) {
            for (WeaponCategory cat : WeaponCategory.values()) {
                final ModdedTier t = tier;
                final WeaponCategory c = cat;
                ExtrasItems.add(t.prefix() + c.id(), () -> new BMMWeapon(c, t));
            }
        }
    }

    private static void froststeelHit(LivingEntity target, LivingEntity attacker) {
        // Chilly III for 30 seconds — same params as UG's froststeelAttackEvent for swords
        BuiltInRegistries.MOB_EFFECT
                .getHolder(ResourceLocation.fromNamespaceAndPath("undergarden", "chilly"))
                .ifPresent(chilly -> target.addEffect(new MobEffectInstance(chilly, 600, 2)));
    }

    static Float forgottenMultiplier(LivingEntity target) {
        EntityType<?> type = target.getType();
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        if (key == null || !"undergarden".equals(key.getNamespace())) return 1.0f;
        if (type.is(Tags.EntityTypes.BOSSES)) return 1.0f;
        return 1.5f;
    }

    private UndergardenCompat() {}
}
