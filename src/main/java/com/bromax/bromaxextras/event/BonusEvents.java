package com.bromax.bromaxextras.event;

import com.bromax.bromaxextras.item.ExtrasWeapon;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * Applies material bonuses from damage events rather than Item.hurtEnemy, so they fire for
 * vanilla attacks and for BHB's direct target.hurt() attacks (including its AOE sweeps) alike.
 */
public final class BonusEvents {

    /** The attacker's main-hand ExtrasWeapon if this is a direct melee hit, else empty stack. */
    private static ItemStack meleeWeapon(DamageSource source) {
        if (!(source.getEntity() instanceof LivingEntity attacker)) return ItemStack.EMPTY;
        if (source.getDirectEntity() != attacker) return ItemStack.EMPTY;
        if (!source.is(DamageTypes.PLAYER_ATTACK) && !source.is(DamageTypes.MOB_ATTACK)) return ItemStack.EMPTY;
        ItemStack weapon = attacker.getMainHandItem();
        if (!(weapon.getItem() instanceof ExtrasWeapon ew) || ew.isPersistBroken(weapon)) return ItemStack.EMPTY;
        return weapon;
    }

    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.isCanceled() || event.getEntity().level().isClientSide()) return;
        ItemStack weapon = meleeWeapon(event.getSource());
        if (weapon.isEmpty()) return;
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        ModdedTier tier = ((ExtrasWeapon) weapon.getItem()).tier();
        event.setAmount(tier.modifyDamage(event.getEntity(), attacker, event.getAmount()));
    }

    public static void onDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;
        ItemStack weapon = meleeWeapon(event.getSource());
        if (weapon.isEmpty()) return;
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
        ((ExtrasWeapon) weapon.getItem()).onMeleeHit(weapon, event.getEntity(), attacker, event.getNewDamage());
    }

    // Adds Looting bonus to weapons whose tier declares getLootingBonus() > 0.
    public static void onEnchantmentLevel(GetEnchantmentLevelEvent event) {
        if (!(event.getStack().getItem() instanceof ExtrasWeapon ew)) return;
        int bonus = ew.tier().getLootingBonus();
        if (bonus <= 0 || ew.isPersistBroken(event.getStack())) return;
        if (!event.isTargetting(Enchantments.LOOTING)) return;
        event.getHolder(Enchantments.LOOTING).ifPresent(looting -> {
            int current = event.getEnchantments().getLevel(looting);
            event.getEnchantments().set(looting, current + bonus);
        });
    }

    // Main-hand attribute bonuses from other mods (e.g. Ars Nouveau Spell Power).
    public static void onAttributeModifiers(ItemAttributeModifierEvent event) {
        if (!(event.getItemStack().getItem() instanceof ExtrasWeapon ew)) return;
        for (ModdedTier.AttributeBonus bonus : ew.tier().attributeBonuses()) {
            var holder = ModdedTier.attributeHolder(bonus.attribute());
            if (holder == null) continue;
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath("bmm_extras",
                    ew.tier().name() + "_" + bonus.attribute().getPath().replaceAll("[^a-z0-9_]", "_"));
            event.addModifier(holder,
                    new AttributeModifier(id, bonus.amount(), AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND);
        }
    }

    private BonusEvents() {}
}
