package committee.nova.maced.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MacedConfig {
    public static final ForgeConfigSpec CFG;
    public static final ForgeConfigSpec.DoubleValue ATTACK_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ATTACK_SPEED;
    public static final ForgeConfigSpec.DoubleValue SMASH_ATTACK_FALL_THRESHOLD;
    public static final ForgeConfigSpec.DoubleValue SMASH_ATTACK_KNOCKBACK_RADIUS;
    public static final ForgeConfigSpec.DoubleValue SMASH_ATTACK_KNOCKBACK_POWER;


    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Maced Settings").push("mace");
        ATTACK_DAMAGE = builder
                .defineInRange("attackDamage", 7.0, .0, Float.MAX_VALUE);
        ATTACK_SPEED = builder
                .defineInRange("attackSpeed", 1.6, .1, 10.0);
        SMASH_ATTACK_FALL_THRESHOLD = builder
                .defineInRange("smashAttackFallThreshold", 1.5, .0, Double.MAX_VALUE);
        SMASH_ATTACK_KNOCKBACK_RADIUS = builder
                .defineInRange("smashAttackKnockbackRadius", 2.5, .0, 10.0);
        SMASH_ATTACK_KNOCKBACK_POWER = builder
                .defineInRange("smashAttackKnockbackPower", .6, .0, 5.0);
        builder.pop();
        CFG = builder.build();
    }
}
