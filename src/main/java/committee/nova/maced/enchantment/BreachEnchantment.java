package committee.nova.maced.enchantment;

import committee.nova.maced.init.MacedEnchantmentCategories;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class BreachEnchantment
        extends Enchantment {
    public BreachEnchantment() {
        super(Rarity.RARE, MacedEnchantmentCategories.MACE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int pLevel) {
        return 15 + 9 * (pLevel - 1);
    }

    @Override
    public int getMaxCost(int pLevel) {
        return 65 + 9 * (pLevel - 1);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    public static float calculateArmorBreach(float f, float g) {
        return Mth.clamp(g - 0.15f * f, 0.0f, 1.0f);
    }
}
