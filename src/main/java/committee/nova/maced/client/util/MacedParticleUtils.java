package committee.nova.maced.client.util;

import committee.nova.maced.init.MacedParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class MacedParticleUtils {
    public static void spawnSmashAttackParticles(LevelAccessor levelAccessor, BlockPos blockPos, int i) {
        double k;
        double h;
        double g;
        double f;
        double e;
        double d;
        Vec3 vec3 = blockPos.getCenter().add(0.0, 0.5, 0.0);
        BlockParticleOption blockParticleOption = new BlockParticleOption(MacedParticles.DUST_PILLAR.get(), levelAccessor.getBlockState(blockPos));
        int j = 0;
        while ((float) j < (float) i / 3.0f) {
            d = vec3.x + levelAccessor.getRandom().nextGaussian() / 2.0;
            e = vec3.y;
            f = vec3.z + levelAccessor.getRandom().nextGaussian() / 2.0;
            g = levelAccessor.getRandom().nextGaussian() * (double) 0.2f;
            h = levelAccessor.getRandom().nextGaussian() * (double) 0.2f;
            k = levelAccessor.getRandom().nextGaussian() * (double) 0.2f;
            levelAccessor.addParticle(blockParticleOption, d, e, f, g, h, k);
            ++j;
        }
        j = 0;
        while ((float) j < (float) i / 1.5f) {
            d = vec3.x + 3.5 * Math.cos(j) + levelAccessor.getRandom().nextGaussian() / 2.0;
            e = vec3.y;
            f = vec3.z + 3.5 * Math.sin(j) + levelAccessor.getRandom().nextGaussian() / 2.0;
            g = levelAccessor.getRandom().nextGaussian() * (double) 0.05f;
            h = levelAccessor.getRandom().nextGaussian() * (double) 0.05f;
            k = levelAccessor.getRandom().nextGaussian() * (double) 0.05f;
            levelAccessor.addParticle(blockParticleOption, d, e, f, g, h, k);
            ++j;
        }
    }
}
