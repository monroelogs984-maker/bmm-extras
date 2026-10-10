package com.bromax.bromaxextras.item;

import com.bromax.bromaxextras.tier.ModdedTier;
import com.bromax.bromaxmelees.item.BMMWeapon;
import com.bromax.bromaxmelees.weapon.WeaponCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/** BMMWeapon for modded tiers — hosts the item-level material bonuses (repair ticks, persist, craft enchants). */
public class ExtrasWeapon extends BMMWeapon {

    public ExtrasWeapon(WeaponCategory category, ModdedTier tier) {
        super(category, tier);
    }

    public ModdedTier tier() {
        return (ModdedTier) getBmmTier();
    }

    /** True for a persistent (Goety-style) weapon sitting at its last point of durability. */
    public boolean isPersistBroken(ItemStack stack) {
        return tier().isPersistent() && stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    /** Called from BonusEvents after a melee hit lands (works under both vanilla and BHB attacks). */
    public void onMeleeHit(ItemStack stack, LivingEntity target, LivingEntity attacker, float dealt) {
        tier().onHit(target, attacker, dealt);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if (!level.isClientSide() && entity instanceof Player player && !isPersistBroken(stack)) {
            tier().inventoryTick(stack, player, selected);
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        tier().applyCraftEnchants(stack, level);
    }

    // Mirrors Goety's DarkSwordItem.damageItem: clamp at 1 durability instead of breaking.
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        if (tier().isPersistent() && stack.getDamageValue() + amount >= stack.getMaxDamage()) {
            if (stack.getDamageValue() != stack.getMaxDamage() - 1) {
                stack.setDamageValue(stack.getMaxDamage() - 1);
                onBroken.accept(this);
            }
            return 0;
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return isPersistBroken(stack) ? 1.0f : super.getDestroySpeed(stack, state);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (isPersistBroken(stack)) {
            tooltip.add(Component.translatable("tooltip.bmm_extras.broken").withStyle(ChatFormatting.DARK_RED));
        }
    }
}
