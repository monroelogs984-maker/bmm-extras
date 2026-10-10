package com.bromax.bromaxextras.compat;

import com.bromax.bromaxextras.compat.ars.ArsEnchantersWeapon;
import com.bromax.bromaxextras.item.ExtrasItems;
import com.bromax.bromaxextras.tier.ModdedTier;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

public final class ArsNouveauCompat {

    // Enchanter's Sword: Tiers.NETHERITE with +3 → 8 dmg at speed 1.6 → baseDps=12.8; 2031 uses ×3.
    // Bonuses mirror EnchantersSword:
    //   - +4 Spell Power while held (PerkAttributes.SPELL_DAMAGE_BONUS)
    //   - inscribe an effect-only spell at the Scribes Table; it's cast with Touch on every hit
    static final ModdedTier ENCHANTERS = new ModdedTier(
            "enchanters_", BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            6093, 9.0f, 15, 12.8f,
            "minecraft", "netherite_ingot")
            .attribute("ars_nouveau", "ars_nouveau.perk.spell_damage", 4.0)
            .bonusLines(2);

    public static void register(IEventBus modBus) {
        if (!ModList.get().isLoaded("ars_nouveau")) return;
        ExtrasItems.addTiers(ArsEnchantersWeapon::new, ENCHANTERS);
        modBus.addListener(ArsEnchantersWeapon::addSpellCasterComponent);
    }

    private ArsNouveauCompat() {}
}
