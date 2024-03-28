package committee.nova.maced.init;

import com.mojang.serialization.Codec;
import committee.nova.maced.Maced;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MacedParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Maced.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<BlockParticleOption>> DUST_PILLAR = PARTICLES.register(
            "dust_pillar",
            () -> new ParticleType<>(true, BlockParticleOption.DESERIALIZER) {
                @Override
                public Codec<BlockParticleOption> codec() {
                    return BlockParticleOption.codec(this);
                }
            }
    );
}
