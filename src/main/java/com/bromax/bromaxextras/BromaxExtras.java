package com.bromax.bromaxextras;

import com.bromax.bromaxextras.client.ExtrasTooltips;
import com.bromax.bromaxextras.compat.*;
import com.bromax.bromaxextras.event.BonusEvents;
import com.bromax.bromaxextras.item.ExtrasCreativeTab;
import com.bromax.bromaxextras.item.ExtrasItems;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(BromaxExtras.MOD_ID)
public final class BromaxExtras {

    public static final String MOD_ID = "bmm_extras";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BromaxExtras(IEventBus modBus) {
        // Each compat checks isLoaded() internally and only registers items if the mod is present
        TwilightForestCompat.register();
        UndergardenCompat.register();
        BetterEndCompat.register();
        BetterNetherCompat.register();
        PrimalMagickCompat.register();
        WyrmroostCompat.register();
        DruidcraftCompat.register();
        MythicUpgradesCompat.register();
        GoetyCompat.register();
        AbyssborneCompat.register();
        GaiaCompat.register();
        ArsNouveauCompat.register(modBus);
        UnusualFishCompat.register();
        RootsCompat.register();
        CommonMetalsCompat.register();

        ExtrasItems.ITEMS.register(modBus);
        ExtrasCreativeTab.TABS.register(modBus);

        NeoForge.EVENT_BUS.addListener(BonusEvents::onIncomingDamage);
        NeoForge.EVENT_BUS.addListener(BonusEvents::onDamagePost);
        NeoForge.EVENT_BUS.addListener(BonusEvents::onEnchantmentLevel);
        NeoForge.EVENT_BUS.addListener(BonusEvents::onAttributeModifiers);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.addListener(EventPriority.LOW, ExtrasTooltips::onItemTooltip);
        }
    }
}
