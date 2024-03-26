package committee.nova.maced.init;

import committee.nova.maced.Maced;
import committee.nova.maced.item.MaceItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class MacedItems {
    public static final Item MACE = Registry.register(
            BuiltInRegistries.ITEM,
            new ResourceLocation(Maced.MODID, "mace"),
            new MaceItem(new Item.Properties().durability(250))
    );

    public static void register() {

    }
}
