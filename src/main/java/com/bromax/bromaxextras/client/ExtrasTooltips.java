package com.bromax.bromaxextras.client;

import com.bromax.bromaxextras.item.ExtrasWeapon;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

/** Shows the material bonus under BMM's trait lines. Registered at LOW priority so BMM's traits are already in. */
public final class ExtrasTooltips {

    private static final TextColor TRAIT_COLOR = TextColor.fromLegacyFormat(ChatFormatting.AQUA);

    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!(event.getItemStack().getItem() instanceof ExtrasWeapon ew)) return;
        ModdedTier tier = ew.tier();
        if (tier.bonusLines() <= 0) return;

        List<Component> tooltip = event.getToolTip();
        int insertAt = -1;
        for (int i = 0; i < tooltip.size(); i++) {
            Component line = tooltip.get(i);
            String text = line.getString();
            if (text.contains("Attack Damage") || text.contains("Attack Speed")
                    || TRAIT_COLOR.equals(line.getStyle().getColor())) {
                insertAt = i + 1;
            }
        }
        if (insertAt < 0) insertAt = tooltip.size();

        ChatFormatting color = ew.isPersistBroken(event.getItemStack()) ? ChatFormatting.DARK_GRAY : ChatFormatting.GOLD;
        for (int i = 0; i < tier.bonusLines(); i++) {
            tooltip.add(insertAt++, Component.translatable(
                    "tooltip.bmm_extras." + tier.name() + ".bonus." + i).withStyle(color));
        }
    }

    private ExtrasTooltips() {}
}
