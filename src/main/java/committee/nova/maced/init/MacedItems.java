package committee.nova.maced.init;

import committee.nova.maced.Maced;
import committee.nova.maced.item.MaceItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MacedItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Maced.MODID);

    public static final RegistryObject<Item> MACE = ITEMS.register("mace", () -> new MaceItem(new Item.Properties().durability(250)));
}
