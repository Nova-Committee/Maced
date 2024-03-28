package committee.nova.maced.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MacedItemTags {
    public static final TagKey<Item> MACE_ENCHANTABLE = ItemTags.create(new ResourceLocation("enchantable/mace"));
}
