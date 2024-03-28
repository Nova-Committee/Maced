package committee.nova.maced.mixin;

import committee.nova.maced.init.MacedEnchantmentCategories;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Mutable
    @Shadow
    @Final
    public EnchantmentCategory category;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void inject$init(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots, CallbackInfo ci) {
        if (this.category.equals(EnchantmentCategory.WEAPON))
            this.category = MacedEnchantmentCategories.WEAPON_EXTENDED;
    }
}
