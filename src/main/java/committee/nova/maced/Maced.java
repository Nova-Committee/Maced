package committee.nova.maced;

import committee.nova.maced.init.MacedItems;
import committee.nova.maced.init.MacedSounds;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Maced.MODID)
public class Maced {
    public static final String MODID = "maced";

    public Maced() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MacedItems.ITEMS.register(bus);
        MacedSounds.SOUNDS.register(bus);
        bus.addListener(this::onBuildCreativeModeTabContentsEvent);
    }

    public void onBuildCreativeModeTabContentsEvent(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(CreativeModeTabs.COMBAT)) return;
        event.accept(MacedItems.MACE::get);
    }
}
