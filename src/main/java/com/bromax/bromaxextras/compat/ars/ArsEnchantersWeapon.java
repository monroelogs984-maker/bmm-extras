package com.bromax.bromaxextras.compat.ars;

import com.bromax.bromaxextras.item.ExtrasWeapon;
import com.bromax.bromaxextras.tier.ModdedTier;
import com.bromax.bromaxmelees.weapon.WeaponCategory;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.AbstractCastMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractCaster;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.EntitySpellResolver;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellCaster;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.client.gui.SpellTooltip;
import com.hollingsworth.arsnouveau.common.perk.RepairingPerk;
import com.hollingsworth.arsnouveau.common.spell.method.MethodTouch;
import com.hollingsworth.arsnouveau.common.util.PortUtil;
import com.hollingsworth.arsnouveau.setup.config.Config;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enchanter's BMM weapon — the spell-inscription half of Ars Nouveau's EnchantersSword,
 * ported onto BMMWeapon. Only loaded when Ars Nouveau is present.
 */
public class ArsEnchantersWeapon extends ExtrasWeapon implements ICasterTool {

    public ArsEnchantersWeapon(WeaponCategory category, ModdedTier tier) {
        super(category, tier);
    }

    /** BMMWeapon builds its own Properties, so the spell caster component is added as a default here. */
    public static void addSpellCasterComponent(ModifyDefaultComponentsEvent event) {
        event.modifyMatching(item -> item instanceof ArsEnchantersWeapon,
                b -> b.set(DataComponentRegistry.SPELL_CASTER.get(), new SpellCaster()));
    }

    // Mirrors EnchantersSword.hurtEnemy, but driven from BonusEvents so BHB attacks cast too.
    @Override
    public void onMeleeHit(ItemStack stack, LivingEntity target, LivingEntity attacker, float dealt) {
        super.onMeleeHit(stack, target, attacker, dealt);
        if (!(target.level() instanceof ServerLevel level)) return;
        AbstractCaster<?> caster = getSpellCaster(stack);
        if (caster == null || caster.getSpell().isEmpty()) return;
        IWrappedCaster wrapped = attacker instanceof Player player ? new PlayerCaster(player) : new LivingCaster(attacker);
        SpellContext context = new SpellContext(level,
                caster.modifySpellBeforeCasting(level, attacker, InteractionHand.MAIN_HAND, caster.getSpell()),
                attacker, wrapped, stack);
        SpellResolver resolver = attacker instanceof Player ? new SpellResolver(context) : new EntitySpellResolver(context);
        resolver.onCastOnEntity(stack, new EntityHitResult(target).getEntity(), InteractionHand.MAIN_HAND);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (entity instanceof Player player) RepairingPerk.attemptRepair(stack, player);
    }

    @Override
    public boolean isScribedSpellValid(AbstractCaster<?> caster, Player player, InteractionHand hand, ItemStack stack, Spell spell) {
        return spell.unsafeList().stream().noneMatch(s -> s instanceof AbstractCastMethod);
    }

    @Override
    public void sendInvalidMessage(Player player) {
        PortUtil.sendMessageNoSpam(player, Component.translatable("ars_nouveau.sword.invalid"));
    }

    @Override
    public void scribeModifiedSpell(AbstractCaster<?> caster, Player player, InteractionHand hand, ItemStack stack, Spell.Mutable spell) {
        List<AbstractSpellPart> recipe = new ArrayList<>();
        recipe.add(MethodTouch.INSTANCE);
        recipe.addAll(spell.recipe);
        spell.recipe = recipe;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (Screen.hasShiftDown() || !Config.GLYPH_TOOLTIPS.get()) {
            getInformation(stack, context, tooltip, flag);
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        AbstractCaster<?> caster = getSpellCaster(stack);
        return caster != null && !Screen.hasShiftDown() && Config.GLYPH_TOOLTIPS.get()
                && !caster.isSpellHidden() && !caster.getSpell().isEmpty()
                ? Optional.of(new SpellTooltip(caster))
                : Optional.empty();
    }
}
