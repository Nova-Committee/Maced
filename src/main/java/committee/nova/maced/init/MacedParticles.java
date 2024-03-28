package committee.nova.maced.init;

import com.mojang.serialization.Codec;
import committee.nova.maced.Maced;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MacedParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Maced.MODID);

    public static final RegistryObject<ParticleType<BlockParticleOption>> DUST_PILLAR = PARTICLES.register(
            "dust_pillar",
            () -> new ParticleType<>(true, BlockParticleOption.DESERIALIZER) {
                @Override
                public Codec<BlockParticleOption> codec() {
                    return BlockParticleOption.codec(this);
                }
            }
    );
}
