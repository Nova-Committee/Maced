package committee.nova.maced.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import committee.nova.maced.api.ExtendedItem;
import committee.nova.maced.api.ExtendedServerPlayer;
import committee.nova.maced.config.MacedConfig;
import committee.nova.maced.enchantment.DensityEnchantment;
import committee.nova.maced.init.MacedEnchantments;
import committee.nova.maced.init.MacedSounds;
import committee.nova.maced.util.Utilities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
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
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MaceItem extends Item implements ExtendedItem, Vanishable {
    private static ImmutableMultimap<Attribute, AttributeModifier> ATTRIBUTES;

    public MaceItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
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
        if (livingEntity2 instanceof ServerPlayer player && canSmashAttack(player)) {
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
            player.setDeltaMovement(player.getDeltaMovement().with(Direction.Axis.Y, 0.0));
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
            if (player.onGround()) {
                extended.maced$setSpawnExtraParticlesOnFall(true);
                SoundEvent soundEvent = player.fallDistance > 5.0f ? MacedSounds.MACE_SMASH_GROUND_HEAVY.get() : MacedSounds.MACE_SMASH_GROUND.get();
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.NEUTRAL, 1.0f, 1.0f);
            } else {
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), MacedSounds.MACE_SMASH_AIR.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
            }
            knockback(serverLevel, player, livingEntity);
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
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
        return itemStack2.is(Items.BLAZE_ROD);
    }

    @Override
    public float getAttackDamageBonus(Player player, float f) {
        int i = EnchantmentHelper.getEnchantmentLevel(MacedEnchantments.DENSITY.get(), player);
        float g = DensityEnchantment.calculateDamageAddition(i, player.fallDistance);
        return MaceItem.canSmashAttack(player) ? 3.0f * player.fallDistance + g : 0.0f;
    }

    private static void knockback(Level level, Player player, Entity entity) {
        level.levelEvent(2013, entity.getOnPos(), 750);
        level.getEntitiesOfClass(
                LivingEntity.class,
                entity.getBoundingBox().inflate(MacedConfig.SMASH_ATTACK_KNOCKBACK_RADIUS.get()),
                knockbackPredicate(player, entity)
        ).forEach(livingEntity -> {
            Vec3 vec3 = livingEntity.position().subtract(entity.position());
            double d = getKnockbackPower(player, livingEntity, vec3);
            Vec3 vec32 = vec3.normalize().scale(d);
            if (d > 0.0) {
                livingEntity.push(vec32.x, MacedConfig.SMASH_ATTACK_KNOCKBACK_POWER.get(), vec32.z);
            }
        });
    }

    private static Predicate<LivingEntity> knockbackPredicate(Player player, Entity entity) {
        return livingEntity -> {
            boolean bl = !livingEntity.isSpectator();
            boolean bl2 = livingEntity != player && livingEntity != entity;
            boolean bl3 = !player.isAlliedTo(livingEntity);
            boolean bl4 = !(livingEntity instanceof ArmorStand armorStand) || !armorStand.isMarker();
            boolean bl5 = entity.distanceToSqr(livingEntity) <= Math.pow(MacedConfig.SMASH_ATTACK_KNOCKBACK_RADIUS.get(), 2.0);
            return bl && bl2 && bl3 && bl4 && bl5;
        };
    }

    private static double getKnockbackPower(Player player, LivingEntity livingEntity, Vec3 vec3) {
        return (MacedConfig.SMASH_ATTACK_KNOCKBACK_RADIUS.get() - vec3.length()) *
                MacedConfig.SMASH_ATTACK_KNOCKBACK_POWER.get() *
                (double) (player.fallDistance > 5.0f ? 2 : 1) *
                (1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }

    public static boolean canSmashAttack(Player player) {
        return player.fallDistance > MacedConfig.SMASH_ATTACK_FALL_THRESHOLD.get().floatValue() && !player.isFallFlying();
    }
}