package committee.nova.maced.mixin;

import com.mojang.authlib.GameProfile;
import committee.nova.maced.api.ExtendedServerPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer extends PlayerEntity implements ExtendedServerPlayer {

    public MixinServerPlayer(World pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Shadow
    public abstract ServerWorld getLevel();

    @Unique
    private boolean maced$spawnExtraParticlesOnFall;

    @Inject(
            method = "doCheckFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;checkFallDamage(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V"
            )
    )
    private void inject$doCheckFallDamage(double pY, boolean pOnGround, CallbackInfo ci) {
        BlockPos blockPos = this.getOnPos();
        BlockState blockState = this.level.getBlockState(blockPos);
        if (this.maced$spawnExtraParticlesOnFall && pOnGround && this.fallDistance > 0.0f) {
            Vector3d vec3 = Vector3d.atCenterOf(blockPos).add(0.0, 0.5, 0.0);
            int i = (int) (50.0f * this.fallDistance);
            this.getLevel().sendParticles(new BlockParticleData(ParticleTypes.BLOCK, blockState), vec3.x, vec3.y, vec3.z, i, 0.3f, 0.3f, 0.3f, 0.15f);
            this.maced$spawnExtraParticlesOnFall = false;
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void inject$readAdditionalSaveData(CompoundNBT tag, CallbackInfo ci) {
        this.maced$spawnExtraParticlesOnFall = tag.getBoolean("spawn_extra_particles_on_fall");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void inject$addAdditionalSaveData(CompoundNBT tag, CallbackInfo ci) {
        tag.putBoolean("spawn_extra_particles_on_fall", this.maced$spawnExtraParticlesOnFall);
    }

    @Override
    public boolean maced$shouldSpawnExtraParticlesOnFall() {
        return maced$spawnExtraParticlesOnFall;
    }

    @Override
    public void maced$setSpawnExtraParticlesOnFall(boolean spawn) {
        this.maced$spawnExtraParticlesOnFall = spawn;
    }
}
