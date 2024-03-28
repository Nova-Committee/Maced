package committee.nova.maced.init;

import committee.nova.maced.Maced;
import committee.nova.maced.enchantment.BreachEnchantment;
import committee.nova.maced.enchantment.DensityEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MacedEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Maced.MODID);
    public static final RegistryObject<Enchantment> BREACH = ENCHANTMENTS.register(
            "breach",
            BreachEnchantment::new
    );
    public static final RegistryObject<Enchantment> DENSITY = ENCHANTMENTS.register(
            "density",
            DensityEnchantment::new
    );
}
