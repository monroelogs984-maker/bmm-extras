package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

/**
 * Generic c:ingots/* metals with no single source mod. Always registered (items can't wait for tags),
 * but hidden from the creative tab and uncraftable while c:ingots/<metal> is empty.
 */
public final class CommonMetalsCompat {

    // Copper: 5 dmg → baseDps=8.0, iron mining, 200 uses ×3
    static final ModdedTier COPPER = new ModdedTier(
            "copper_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            600, 5.0f, 13, 8.0f)
            .repairTag("c", "ingots/copper");

    // Silver: 6 dmg → baseDps=9.6, iron mining, 250 uses ×3, 1.5× damage vs undead
    static final ModdedTier SILVER = new ModdedTier(
            "silver_", BlockTags.INCORRECT_FOR_IRON_TOOL,
            750, 6.0f, 20, 9.6f)
            .repairTag("c", "ingots/silver")
            .damageModifier(CommonMetalsCompat::silverBonus)
            .bonusLines(1);

    // Steel: 7 dmg → baseDps=11.2, diamond mining, 1000 uses ×3
    static final ModdedTier STEEL = new ModdedTier(
            "steel_", BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            3000, 7.0f, 10, 11.2f)
            .repairTag("c", "ingots/steel");

    public static void register() {
        ExtrasItems.addTiers(COPPER, SILVER, STEEL);
    }

    /** False when no loaded mod provides c:ingots/<metal> — used to hide the tier. */
    public static boolean isAvailable(String prefix) {
        String metal = switch (prefix) {
            case "copper_" -> "copper";
            case "silver_" -> "silver";
            case "steel_"  -> "steel";
            default -> null;
        };
        if (metal == null) return true;
        TagKey<Item> tag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/" + metal));
        return BuiltInRegistries.ITEM.getTag(tag).map(set -> set.size() > 0).orElse(false);
    }

    private static float silverBonus(LivingEntity target, LivingEntity attacker, float amount) {
        return target.getType().is(EntityTypeTags.UNDEAD) ? amount * 1.5f : amount;
    }

    private CommonMetalsCompat() {}
}
