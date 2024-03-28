package committee.nova.maced.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.Nullable;

public class Utilities {
    public static void hurtAndBreak(ItemStack stack, int i, LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        ServerPlayer serverPlayer = livingEntity instanceof ServerPlayer sp ? sp : null;
        Player player = livingEntity instanceof Player p ? p : null;
        if (livingEntity.level.isClientSide || player != null && player.getAbilities().instabuild) {
            return;
        }
        hurtAndBreak(stack, i, livingEntity.getRandom(), serverPlayer, () -> {
            livingEntity.broadcastBreakEvent(equipmentSlot);
            Item item = stack.getItem();
            stack.shrink(1);
            if (livingEntity instanceof Player) {
                ((Player) livingEntity).awardStat(Stats.ITEM_BROKEN.get(item));
            }
            stack.setDamageValue(0);
        });
    }

    private static void hurtAndBreak(ItemStack stack, int i, RandomSource randomSource, @Nullable ServerPlayer serverPlayer, Runnable runnable) {
        int j;
        if (!stack.isDamageableItem()) {
            return;
        }
        if (i > 0) {
            j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
            int k = 0;
            for (int l = 0; j > 0 && l < i; ++l) {
                if (!DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(stack, j, randomSource)) continue;
                ++k;
            }
            if ((i -= k) <= 0) {
                return;
            }
        }
        if (serverPlayer != null && i != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(serverPlayer, stack, stack.getDamageValue() + i);
        }
        j = stack.getDamageValue() + i;
        stack.setDamageValue(j);
        if (j >= stack.getMaxDamage()) {
            runnable.run();
        }
    }
}
