package committee.nova.maced.util;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Random;

public class Utilities {
    public static void hurtAndBreak(ItemStack stack, int i, LivingEntity livingEntity, EquipmentSlotType equipmentSlot) {
        ServerPlayerEntity serverPlayer = livingEntity instanceof ServerPlayerEntity ? (ServerPlayerEntity) livingEntity : null;
        PlayerEntity player = livingEntity instanceof PlayerEntity ? (PlayerEntity) livingEntity : null;
        if (livingEntity.level.isClientSide || player != null && player.abilities.instabuild) {
            return;
        }
        hurtAndBreak(stack, i, livingEntity.getRandom(), serverPlayer, () -> {
            livingEntity.broadcastBreakEvent(equipmentSlot);
            Item item = stack.getItem();
            stack.shrink(1);
            if (livingEntity instanceof PlayerEntity) {
                ((PlayerEntity) livingEntity).awardStat(Stats.ITEM_BROKEN.get(item));
            }
            stack.setDamageValue(0);
        });
    }

    private static void hurtAndBreak(ItemStack stack, int i, Random rand, @Nullable ServerPlayerEntity serverPlayer, Runnable runnable) {
        int j;
        if (!stack.isDamageableItem()) {
            return;
        }
        if (i > 0) {
            j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack);
            int k = 0;
            for (int l = 0; j > 0 && l < i; ++l) {
                if (!UnbreakingEnchantment.shouldIgnoreDurabilityDrop(stack, j, rand)) continue;
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

    public static BlockPos getOnPos(LivingEntity living) {
        int i = MathHelper.floor(living.position().x);
        int j = MathHelper.floor(living.position().y - (double) 0.2F);
        int k = MathHelper.floor(living.position().z);
        BlockPos blockpos = new BlockPos(i, j, k);
        if (living.level.isEmptyBlock(blockpos)) {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = living.level.getBlockState(blockpos1);
            if (blockstate.collisionExtendsVertically(living.level, blockpos1, living)) {
                return blockpos1;
            }
        }

        return blockpos;
    }
}
