package com.bromax.bromaxextras.item;

import com.bromax.bromaxextras.BromaxExtras;
import com.bromax.bromaxextras.compat.CommonMetalsCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ExtrasCreativeTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BromaxExtras.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB =
            TABS.register("bmm_extras", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.bmm_extras"))
                    .icon(() -> {
                        var entries = ExtrasItems.ITEMS.getEntries();
                        if (entries.isEmpty()) return new ItemStack(Items.IRON_SWORD);
                        return new ItemStack(entries.iterator().next().get());
                    })
                    .displayItems((params, output) ->
                            ExtrasItems.ITEMS.getEntries().forEach(e -> {
                                // Generic metals stay hidden until some mod provides c:ingots/<metal>
                                if (e.get() instanceof ExtrasWeapon ew
                                        && !CommonMetalsCompat.isAvailable(ew.tier().prefix())) return;
                                output.accept(e.get());
                            }))
                    .build());

    private ExtrasCreativeTab() {}
}
