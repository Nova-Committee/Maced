package committee.nova.maced;

import committee.nova.maced.config.MacedConfig;
import committee.nova.maced.init.MacedItems;
import committee.nova.maced.init.MacedSounds;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.fml.config.ModConfig;

public class Maced implements ModInitializer {
    public static final String MODID = "maced";

    @Override
    public void onInitialize() {
        ForgeConfigRegistry.INSTANCE.register(MODID, ModConfig.Type.COMMON, MacedConfig.CFG);
        MacedItems.register();
        MacedSounds.register();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(e -> e.accept(MacedItems.MACE));
    }
}
