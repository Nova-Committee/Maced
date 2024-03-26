package committee.nova.maced.mixin;

import committee.nova.maced.api.ExtendedItem;
import committee.nova.maced.api.ExtendedPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements ExtendedPlayer {
    @Shadow
    @Final
    private static Logger LOGGER;
    @Unique
    private Vec3 maced$currentImpulseImpactPos;

    @Unique
    private boolean maced$ignoreFallDamageFromCurrentImpulse;

    protected MixinPlayer(EntityType<? extends LivingEntity> t, Level l) {
        super(t, l);
    }

    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/player/Player;fallDistance:F"
            ),
            ordinal = 0
    )
    private float modify$attack(float value) {
        final Item item = this.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (!(item instanceof ExtendedItem extended)) return value;
        return value + extended.getAttackDamageBonus((Player) (Object) this, value);
    }

    @Inject(
            method = "causeFallDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;causeFallDamage(FFLnet/minecraft/world/damagesource/DamageSource;)Z"
            ),
            cancellable = true)
    private void inject$causeFallDamage(float f1, float f2, DamageSource dmgSrc, CallbackInfoReturnable<Boolean> cir) {
        if (this.maced$ignoreFallDamageFromCurrentImpulse && this.maced$currentImpulseImpactPos != null) {
            double d = this.maced$currentImpulseImpactPos.y;
            this.maced$resetCurrentImpulseContext();
            if (d < this.getY()) {
                cir.setReturnValue(false);
                return;
            }
            cir.setReturnValue(super.causeFallDamage((float) (d - this.getY()), f2, dmgSrc));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void inject$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.maced$currentImpulseImpactPos != null) {
            tag.put("current_explosion_impact_pos", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, this.maced$currentImpulseImpactPos).getOrThrow(true, LOGGER::error));
            // TODO: Available?
        }
        tag.putBoolean("ignore_fall_damage_from_current_explosion", this.maced$ignoreFallDamageFromCurrentImpulse);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void inject$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("current_explosion_impact_pos", 9)) {
            Vec3.CODEC.parse(NbtOps.INSTANCE, tag.get("current_explosion_impact_pos")).resultOrPartial(LOGGER::error).ifPresent(vec3 -> {
                this.maced$currentImpulseImpactPos = vec3;
            });
        }
        this.maced$ignoreFallDamageFromCurrentImpulse = tag.getBoolean("ignore_fall_damage_from_current_explosion");
    }

    @Override
    public Vec3 maced$getCurrentImpulseImpactPos() {
        return maced$currentImpulseImpactPos;
    }

    @Override
    public void maced$setCurrentImpulseImpactPos(Vec3 impactPos) {
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
