package committee.nova.maced.init;

import committee.nova.maced.Maced;
import committee.nova.maced.item.MaceItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MacedItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Maced.MODID);

    public static final DeferredHolder<Item, MaceItem> MACE = ITEMS.register("mace", () -> new MaceItem(new Item.Properties().durability(250)));
}
