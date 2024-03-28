package committee.nova.maced.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DustPillarProvider
        implements ParticleProvider<BlockParticleOption> {
    @Override
    @Nullable
    public Particle createParticle(BlockParticleOption blockParticleOption, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        TerrainParticle particle = createTerrainParticle(blockParticleOption, clientLevel, d, e, f, g, h, i);
        if (particle != null) {
            particle.setParticleSpeed(clientLevel.random.nextGaussian() / 30.0, h + clientLevel.random.nextGaussian() / 2.0, clientLevel.random.nextGaussian() / 30.0);
            particle.setLifetime(clientLevel.random.nextInt(20) + 20);
        }
        return particle;
    }

    static TerrainParticle createTerrainParticle(BlockParticleOption blockParticleOption, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        BlockState blockState = blockParticleOption.getState();
        if (blockState.isAir() || blockState.is(Blocks.MOVING_PISTON) || shouldNotSpawnTerrainParticles(blockState)) {
            return null;
        }
        return new TerrainParticle(clientLevel, d, e, f, g, h, i, blockState);
    }

    static boolean shouldNotSpawnTerrainParticles(BlockState state) {
        return state.is(Blocks.BARRIER) || state.is(Blocks.STRUCTURE_VOID);
    }
}
