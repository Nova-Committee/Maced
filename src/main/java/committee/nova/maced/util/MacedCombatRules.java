package committee.nova.maced.util;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;

public class MacedCombatRules {
    public static float getDamageAfterAbsorb(float f, DamageSource damageSource, float g, float h) {
        float i = 2.0f + h / 4.0f;
        float j = Mth.clamp(g - f / i, g * 0.2f, 20.0f);
        float k = j / 25.0f;
        float l = MacedEnchantmentHelper.calculateArmorBreach(damageSource.getEntity(), k);
        float m = 1.0f - l;
        return f * m;
    }
}
