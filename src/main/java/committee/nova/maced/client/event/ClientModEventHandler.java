package committee.nova.maced.client.event;

import committee.nova.maced.client.particle.DustPillarProvider;
import committee.nova.maced.init.MacedParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void onRegProvider(RegisterParticleProvidersEvent event) {
        event.register(MacedParticles.DUST_PILLAR.get(), new DustPillarProvider());
    }
}
