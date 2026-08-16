package com.bromax.bromaxextras.item;

import com.bromax.bromaxextras.BromaxExtras;
import com.bromax.bromaxmelees.item.BMMWeapon;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ExtrasItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, BromaxExtras.MOD_ID);

    public static DeferredHolder<Item, BMMWeapon> add(String id, Supplier<BMMWeapon> factory) {
        return ITEMS.register(id, factory);
    }

    private ExtrasItems() {}
}
