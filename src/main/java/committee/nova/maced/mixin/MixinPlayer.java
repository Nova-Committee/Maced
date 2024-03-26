package committee.nova.maced.mixin;

import committee.nova.maced.api.ExtendedItem;
import committee.nova.maced.api.ExtendedPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayer extends LivingEntity implements ExtendedPlayer {
    @Unique
    private Vector3d maced$currentImpulseImpactPos;

    @Unique
    private boolean maced$ignoreFallDamageFromCurrentImpulse;

    protected MixinPlayer(EntityType<? extends LivingEntity> t, World l) {
        super(t, l);
    }

    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;fallDistance:F"
            ),
            ordinal = 0
    )
    private float modify$attack(float value) {
        final Item item = this.getItemInHand(Hand.MAIN_HAND).getItem();
        if (!(item instanceof ExtendedItem)) return value;
        return value + ((ExtendedItem) item).getAttackDamageBonus((PlayerEntity) (Object) this, value);
    }

    @Inject(
            method = "causeFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;causeFallDamage(FF)Z"
            ),
            cancellable = true)
    private void inject$causeFallDamage(float f1, float f2, CallbackInfoReturnable<Boolean> cir) {
        if (this.maced$ignoreFallDamageFromCurrentImpulse && this.maced$currentImpulseImpactPos != null) {
            double d = this.maced$currentImpulseImpactPos.y;
            this.maced$resetCurrentImpulseContext();
            if (d < this.getY()) {
                cir.setReturnValue(false);
                return;
            }
            cir.setReturnValue(super.causeFallDamage((float) (d - this.getY()), f2));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void inject$addAdditionalSaveData(CompoundNBT tag, CallbackInfo ci) {
        if (this.maced$currentImpulseImpactPos != null) {
            final CompoundNBT vec = new CompoundNBT();
            vec.putDouble("x", this.maced$currentImpulseImpactPos.x);
            vec.putDouble("y", this.maced$currentImpulseImpactPos.y);
            vec.putDouble("z", this.maced$currentImpulseImpactPos.z);
            tag.put("current_explosion_impact_pos", vec);
        }
        tag.putBoolean("ignore_fall_damage_from_current_explosion", this.maced$ignoreFallDamageFromCurrentImpulse);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void inject$readAdditionalSaveData(CompoundNBT tag, CallbackInfo ci) {
        if (tag.contains("current_explosion_impact_pos", 10)) {
            final CompoundNBT vec = tag.getCompound("current_explosion_impact_pos");
            this.maced$currentImpulseImpactPos = new Vector3d(
                    vec.getDouble("x"),
                    vec.getDouble("y"),
                    vec.getDouble("z")
            );
        }
        this.maced$ignoreFallDamageFromCurrentImpulse = tag.getBoolean("ignore_fall_damage_from_current_explosion");
    }

    @Override
    public Vector3d maced$getCurrentImpulseImpactPos() {
        return maced$currentImpulseImpactPos;
    }

    @Override
    public void maced$setCurrentImpulseImpactPos(Vector3d impactPos) {
        this.maced$currentImpulseImpactPos = impactPos;
    }

    @Override
    public boolean maced$shouldIgnoreFallDamageFromCurrentImpulse() {
        return maced$ignoreFallDamageFromCurrentImpulse;
    }

    @Override
    public void maced$setIgnoreFallDamageFromCurrentImpulse(boolean ignore) {
        this.maced$ignoreFallDamageFromCurrentImpulse = ignore;
    }

    @Unique
    private void maced$resetCurrentImpulseContext() {
        this.maced$currentImpulseImpactPos = null;
        this.maced$ignoreFallDamageFromCurrentImpulse = false;
    }
}
