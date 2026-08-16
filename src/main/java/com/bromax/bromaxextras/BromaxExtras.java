package com.bromax.bromaxextras;

import com.bromax.bromaxextras.compat.*;
import com.bromax.bromaxextras.item.ExtrasCreativeTab;
import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxmelees.item.BMMWeapon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@Mod(BromaxExtras.MOD_ID)
public final class BromaxExtras {

    public static final String MOD_ID = "bmm_extras";

    public BromaxExtras(IEventBus modBus) {
        // Each compat checks isLoaded() internally and only registers items if the mod is present
        TwilightForestCompat.register();
        UndergardenCompat.register();
        BetterEndCompat.register();
        BetterNetherCompat.register();
        PrimalMagickCompat.register();
        WyrmroostCompat.register();
        DruidcraftCompat.register();

        ExtrasItems.ITEMS.register(modBus);
        ExtrasCreativeTab.TABS.register(modBus);

        NeoForge.EVENT_BUS.addListener(BromaxExtras::onEnchantmentLevel);
        NeoForge.EVENT_BUS.addListener(BromaxExtras::onIncomingDamage);
    }

    // Adds Looting bonus to weapons whose tier declares getLootingBonus() > 0.
    private static void onEnchantmentLevel(GetEnchantmentLevelEvent event) {
        if (!(event.getStack().getItem() instanceof BMMWeapon bw)) return;
        int bonus = bw.getBmmTier().getLootingBonus();
        if (bonus <= 0) return;
        if (!event.isTargetting(Enchantments.LOOTING)) return;
        event.getHolder(Enchantments.LOOTING).ifPresent(looting -> {
            int current = event.getEnchantments().getLevel(looting);
            event.getEnchantments().set(looting, current + bonus);
        });
    }

    // Applies per-tier damage multipliers (e.g. Forgotten 1.5× vs non-boss Undergarden mobs).
    private static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.isCanceled()) return;
        ItemStack weapon = event.getSource().getWeaponItem();
        if (weapon == null || !(weapon.getItem() instanceof BMMWeapon bw)) return;
        float mult = bw.getBmmTier().getDamageMultiplier(event.getEntity());
        if (mult != 1.0f) event.setAmount(event.getAmount() * mult);
    }
}
