package committee.nova.maced.mixin;

import com.mojang.authlib.GameProfile;
import committee.nova.maced.api.ExtendedServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements ExtendedServerPlayer {
    @Shadow
    public abstract ServerLevel serverLevel();

    @Unique
    private boolean maced$spawnExtraParticlesOnFall;

    public MixinServerPlayer(Level l, BlockPos p, float f, GameProfile g) {
        super(l, p, f, g);
    }

    @Inject(
            method = "doCheckFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V"
            )
    )
    private void inject$doCheckFallDamage(double pMovementX, double pMovementY, double pMovementZ, boolean pOnGround, CallbackInfo ci) {
        BlockPos blockPos = this.getOnPos(.2F);
        BlockState blockState = this.level().getBlockState(blockPos);
        if (this.maced$spawnExtraParticlesOnFall && pOnGround && this.fallDistance > 0.0f) {
            Vec3 vec3 = blockPos.getCenter().add(0.0, 0.5, 0.0);
            int i = (int) (50.0f * this.fallDistance);
            this.serverLevel().sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), vec3.x, vec3.y, vec3.z, i, 0.3f, 0.3f, 0.3f, 0.15f);
            this.maced$spawnExtraParticlesOnFall = false;
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void inject$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.maced$spawnExtraParticlesOnFall = tag.getBoolean("spawn_extra_particles_on_fall");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void inject$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
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
