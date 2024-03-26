package committee.nova.maced.init;

import committee.nova.maced.Maced;
import committee.nova.maced.item.MaceItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MacedItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Maced.MODID);

    public static final DeferredItem<MaceItem> MACE = ITEMS.register("mace", () -> new MaceItem(new Item.Properties().durability(250)));
}
