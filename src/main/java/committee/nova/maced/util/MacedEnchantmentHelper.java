package committee.nova.maced.util;

import committee.nova.maced.enchantment.BreachEnchantment;
import committee.nova.maced.init.MacedEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

public class MacedEnchantmentHelper {
    public static float calculateArmorBreach(@Nullable Entity entity, float f) {
        int i;
        if (entity instanceof LivingEntity l && (i = EnchantmentHelper.getEnchantmentLevel(MacedEnchantments.BREACH.get(), l)) > 0) {
            return BreachEnchantment.calculateArmorBreach(i, f);
        }
        return f;
    }
}
