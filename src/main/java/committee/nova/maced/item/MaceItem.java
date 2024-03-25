package committee.nova.maced.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import committee.nova.maced.api.ExtendedItem;
import committee.nova.maced.api.ExtendedServerPlayer;
import committee.nova.maced.init.MacedSounds;
import committee.nova.maced.util.Utilities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MaceItem extends Item implements ExtendedItem {
    private static final double DEFAULT_ATTACK_DAMAGE = 6.0;
    private static final double DEFAULT_ATTACK_SPEED = -2.4;
    private static final float SMASH_ATTACK_FALL_THRESHOLD = 1.5f;
    private static final double SMASH_ATTACK_KNOCKBACK_RADIUS = 2.5;
    private static final double SMASH_ATTACK_KNOCKBACK_POWER = .6;
    private static final ImmutableMultimap<Attribute, AttributeModifier> ATTRIBUTES = ImmutableMultimap
            .<Attribute, AttributeModifier>builder()
            .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", DEFAULT_ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION))
            .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", DEFAULT_ATTACK_SPEED, AttributeModifier.Operation.ADDITION))
            .build();

    public MaceItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, entity, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        Utilities.hurtAndBreak(itemStack, 1, livingEntity2, EquipmentSlot.MAINHAND);
        if (livingEntity2 instanceof ServerPlayer player) {
            if (player.fallDistance > SMASH_ATTACK_FALL_THRESHOLD) {
                ServerLevel serverLevel = (ServerLevel) livingEntity2.level();
                final ExtendedServerPlayer extended = (ExtendedServerPlayer) player;
                Vec3 impact;
                if (
                        !extended.maced$shouldIgnoreFallDamageFromCurrentImpulse() ||
                                (impact = extended.maced$getCurrentImpulseImpactPos()) == null ||
                                impact.y() > player.getY()
                ) {
                    extended.maced$setCurrentImpulseImpactPos(player.position());
                    extended.maced$setIgnoreFallDamageFromCurrentImpulse(true);
                }
                if (livingEntity.onGround()) {
                    extended.maced$setSpawnExtraParticlesOnFall(true);
                    SoundEvent soundEvent = player.fallDistance > 5.0f ? MacedSounds.MACE_SMASH_GROUND_HEAVY.get() : MacedSounds.MACE_SMASH_GROUND.get();
                    serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.NEUTRAL, 1.0f, 1.0f);
                } else {
                    serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), MacedSounds.MACE_SMASH_AIR.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
                }
                this.knockback(serverLevel, player, livingEntity);
            }
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            return ATTRIBUTES;
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack2.is(Items.BLAZE_ROD);
    }

    @Override
    public float getAttackDamageBonus(Player player, float f) {
        return player.fallDistance > SMASH_ATTACK_FALL_THRESHOLD ? f * 0.5f * player.fallDistance : 0.0f;
    }

    private void knockback(Level level, Player player, Entity entity) {
        level.getEntitiesOfClass(
                LivingEntity.class,
                entity.getBoundingBox().inflate(SMASH_ATTACK_KNOCKBACK_RADIUS),
                livingEntity -> livingEntity != player && livingEntity != entity && !entity.isAlliedTo(livingEntity) && (!(livingEntity instanceof ArmorStand) || !((ArmorStand) livingEntity).isMarker()) && entity.distanceToSqr(livingEntity) <= Math.pow(SMASH_ATTACK_KNOCKBACK_RADIUS, 2.0)
        ).forEach(livingEntity -> {
            Vec3 vec3 = livingEntity.position().subtract(entity.position());
            double d = (SMASH_ATTACK_KNOCKBACK_RADIUS - vec3.length()) * SMASH_ATTACK_KNOCKBACK_POWER * (1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 vec32 = vec3.normalize().scale(d);
            if (d > 0.0) {
                livingEntity.push(vec32.x, SMASH_ATTACK_KNOCKBACK_POWER, vec32.z);
                if (level instanceof ServerLevel serverLevel) {
                    BlockPos blockPos = livingEntity.getOnPos();
                    Vec3 vec33 = blockPos.getCenter().add(0.0, 0.5, 0.0);
                    int i = (int) (100.0 * d);
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, serverLevel.getBlockState(blockPos)), vec33.x, vec33.y, vec33.z, i, 0.3f, 0.3f, 0.3f, 0.15f);
                }
            }
        });
    }
}