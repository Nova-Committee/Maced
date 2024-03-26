package committee.nova.maced.api;

import net.minecraft.world.entity.player.Player;

public interface ExtendedItem {
    float getAttackDamageBonus(Player player, float f);
}
