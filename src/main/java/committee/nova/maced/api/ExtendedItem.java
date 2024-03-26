package committee.nova.maced.api;

import net.minecraft.entity.player.PlayerEntity;

public interface ExtendedItem {
    float getAttackDamageBonus(PlayerEntity player, float f);
}
