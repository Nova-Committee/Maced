package committee.nova.maced.mixin;

import committee.nova.maced.client.util.MacedParticleUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow
    @Nullable
    private ClientLevel level;

    @Inject(method = "levelEvent", at = @At("HEAD"))
    private void inject$levelEvent(int pType, BlockPos pPos, int pData, CallbackInfo ci) {
        if (pType != 2013 || level == null) return;
        MacedParticleUtils.spawnSmashAttackParticles(level, pPos, pData);
    }
}
