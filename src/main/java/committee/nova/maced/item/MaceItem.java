package committee.nova.maced.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import committee.nova.maced.api.ExtendedItem;
import committee.nova.maced.api.ExtendedServerPlayer;
import committee.nova.maced.config.MacedConfig;
import committee.nova.maced.init.MacedSounds;
import committee.nova.maced.util.Utilities;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MaceItem extends Item implements ExtendedItem {
    private static ImmutableMultimap<Attribute, AttributeModifier> ATTRIBUTES;

    public MaceItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, World level, BlockPos blockPos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, entity, (e) -> e.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        Utilities.hurtAndBreak(itemStack, 1, livingEntity2, EquipmentSlotType.MAINHAND);
        if (livingEntity2 instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) livingEntity2;
            if (player.fallDistance > MacedConfig.SMASH_ATTACK_FALL_THRESHOLD.get().floatValue()) {
                ServerWorld serverLevel = (ServerWorld) livingEntity2.level;
                final ExtendedServerPlayer extended = (ExtendedServerPlayer) player;
                Vector3d impact;
                if (
                        !extended.maced$shouldIgnoreFallDamageFromCurrentImpulse() ||
                                (impact = extended.maced$getCurrentImpulseImpactPos()) == null ||
                                impact.y() > player.getY()
                ) {
                    extended.maced$setCurrentImpulseImpactPos(player.position());
                    extended.maced$setIgnoreFallDamageFromCurrentImpulse(true);
                }
                if (livingEntity.isOnGround()) {
                    extended.maced$setSpawnExtraParticlesOnFall(true);
                    SoundEvent soundEvent = player.fallDistance > 5.0f ? MacedSounds.MACE_SMASH_GROUND_HEAVY.get() : MacedSounds.MACE_SMASH_GROUND.get();
                    serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                } else {
                    serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), MacedSounds.MACE_SMASH_AIR.get(), SoundCategory.NEUTRAL, 1.0f, 1.0f);
                }
                this.knockback(serverLevel, player, livingEntity);
            }
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            if (ATTRIBUTES == null) {
                ATTRIBUTES = ImmutableMultimap
                        .<Attribute, AttributeModifier>builder()
                        .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                                BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                                MacedConfig.ATTACK_DAMAGE.get() - 1.0, AttributeModifier.Operation.ADDITION
                        ))
                        .put(Attributes.ATTACK_SPEED, new AttributeModifier(
                                BASE_ATTACK_SPEED_UUID, "Weapon modifier",
                                MacedConfig.ATTACK_SPEED.get() - 4.0, AttributeModifier.Operation.ADDITION
                        ))
                        .build();
            }
            return ATTRIBUTES;
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean isValidRepairItem(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack2.getItem().equals(Items.BLAZE_ROD);
    }

    @Override
    public float getAttackDamageBonus(PlayerEntity player, float f) {
        return player.fallDistance > MacedConfig.SMASH_ATTACK_FALL_THRESHOLD.get().floatValue() ? f * 0.5f * player.fallDistance : 0.0f;
    }

    private void knockback(World level, PlayerEntity player, Entity entity) {
        level.getEntitiesOfClass(
                LivingEntity.class,
                entity.getBoundingBox().inflate(MacedConfig.SMASH_ATTACK_KNOCKBACK_RADIUS.get()),
                livingEntity -> livingEntity != player &&
                        livingEntity != entity &&
                        !entity.isAlliedTo(livingEntity) &&
                        (!(livingEntity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingEntity).isMarker()) &&
                        entity.distanceToSqr(livingEntity) <= Math.pow(MacedConfig.SMASH_ATTACK_KNOCKBACK_RADIUS.get(), 2.0)
        ).forEach(livingEntity -> {
            Vector3d vec3 = livingEntity.position().subtract(entity.position());
            double d = (MacedConfig.SMASH_ATTACK_KNOCKBACK_RADIUS.get() - vec3.length()) *
                    MacedConfig.SMASH_ATTACK_KNOCKBACK_POWER.get() *
                    (1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vector3d vec32 = vec3.normalize().scale(d);
            if (d > 0.0) {
                livingEntity.push(vec32.x, MacedConfig.SMASH_ATTACK_KNOCKBACK_POWER.get(), vec32.z);
                if (level instanceof ServerWorld) {
                    BlockPos blockPos = Utilities.getOnPos(livingEntity);
                    Vector3d vec33 = Vector3d.atCenterOf(blockPos).add(0.0, 0.5, 0.0);
                    int i = (int) (100.0 * d);
                    final ServerWorld serverLevel = (ServerWorld) level;
                    serverLevel.sendParticles(
                            new BlockParticleData(ParticleTypes.BLOCK, serverLevel.getBlockState(blockPos)),
                            vec33.x, vec33.y, vec33.z,
                            i, 0.3f, 0.3f, 0.3f, 0.15f
                    );
                }
            }
        });
    }
}