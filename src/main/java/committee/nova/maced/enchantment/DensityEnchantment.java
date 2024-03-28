package committee.nova.maced.enchantment;

import committee.nova.maced.init.MacedEnchantmentCategories;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

public class DensityEnchantment
        extends Enchantment {
    public DensityEnchantment() {
        super(Rarity.COMMON, MacedEnchantmentCategories.MACE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinCost(int pLevel) {
        return 1 + 11 * (pLevel - 1);
    }

    @Override
    public int getMaxCost(int pLevel) {
        return 21 + 11 * (pLevel - 1);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    public static float calculateDamageAddition(int i, float f) {
        return f * (float) i;
    }


}
