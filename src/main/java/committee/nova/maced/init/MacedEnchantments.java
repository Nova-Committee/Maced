package committee.nova.maced.init;

import committee.nova.maced.Maced;
import committee.nova.maced.enchantment.BreachEnchantment;
import committee.nova.maced.enchantment.DensityEnchantment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MacedEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT, Maced.MODID);
    public static final DeferredHolder<Enchantment, BreachEnchantment> BREACH = ENCHANTMENTS.register(
            "breach",
            BreachEnchantment::new
    );
    public static final DeferredHolder<Enchantment, DensityEnchantment> DENSITY = ENCHANTMENTS.register(
            "density",
            DensityEnchantment::new
    );
}
