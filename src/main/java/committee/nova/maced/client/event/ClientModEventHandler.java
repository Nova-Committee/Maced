package committee.nova.maced.client.event;

import committee.nova.maced.client.particle.DustPillarProvider;
import committee.nova.maced.init.MacedParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void onRegProvider(RegisterParticleProvidersEvent event) {
        event.registerSpecial(MacedParticles.DUST_PILLAR.get(), new DustPillarProvider());
    }
}
