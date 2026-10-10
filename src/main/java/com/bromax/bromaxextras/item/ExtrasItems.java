package com.bromax.bromaxextras.item;

import com.bromax.bromaxextras.BromaxExtras;
import com.bromax.bromaxextras.tier.ModdedTier;
import com.bromax.bromaxmelees.item.BMMWeapon;
import com.bromax.bromaxmelees.weapon.WeaponCategory;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class ExtrasItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, BromaxExtras.MOD_ID);

    public static DeferredHolder<Item, BMMWeapon> add(String id, Supplier<BMMWeapon> factory) {
        return ITEMS.register(id, factory);
    }

    /** Registers every weapon category in each tier as a plain ExtrasWeapon. */
    public static void addTiers(ModdedTier... tiers) {
        addTiers(ExtrasWeapon::new, tiers);
    }

    public static void addTiers(BiFunction<WeaponCategory, ModdedTier, ? extends BMMWeapon> factory, ModdedTier... tiers) {
        for (ModdedTier tier : tiers) {
            for (WeaponCategory cat : WeaponCategory.values()) {
                add(tier.prefix() + cat.id(), () -> factory.apply(cat, tier));
            }
        }
    }

    private ExtrasItems() {}
}
