package committee.nova.maced.init;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

@SuppressWarnings("deprecation")
public class MacedEnchantmentCategories {
    public static final EnchantmentCategory MACE = EnchantmentCategory.create(
            "MACE",
            i -> i.builtInRegistryHolder().is(MacedItemTags.MACE_ENCHANTABLE)
    );

    public static final EnchantmentCategory WEAPON_EXTENDED = EnchantmentCategory.create(
            "WEAPON_EXTENDED",
            i -> i instanceof SwordItem || i.builtInRegistryHolder().is(MacedItemTags.MACE_ENCHANTABLE)
    );
}
