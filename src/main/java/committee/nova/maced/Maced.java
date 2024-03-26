package committee.nova.maced;

import committee.nova.maced.config.MacedConfig;
import committee.nova.maced.init.MacedItems;
import committee.nova.maced.init.MacedSounds;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Maced.MODID)
public class Maced {
    public static final String MODID = "maced";

    public Maced(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MacedConfig.CFG);
        MacedItems.ITEMS.register(bus);
        MacedSounds.SOUNDS.register(bus);
        bus.addListener(this::onBuildCreativeModeTabContentsEvent);
    }

    public void onBuildCreativeModeTabContentsEvent(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(CreativeModeTabs.COMBAT)) return;
        event.accept(MacedItems.MACE::get);
    }
}
