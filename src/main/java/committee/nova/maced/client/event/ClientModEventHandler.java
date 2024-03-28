package committee.nova.maced.client.event;

import committee.nova.maced.client.particle.DustPillarProvider;
import committee.nova.maced.init.MacedParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void onRegProvider(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(MacedParticles.DUST_PILLAR.get(), new DustPillarProvider());
    }
}
