package committee.nova.maced.api;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface ExtendedPlayer {
    @Nullable
    Vec3 maced$getCurrentImpulseImpactPos();

    void maced$setCurrentImpulseImpactPos(Vec3 impactPos);

    boolean maced$shouldIgnoreFallDamageFromCurrentImpulse();

    void maced$setIgnoreFallDamageFromCurrentImpulse(boolean ignore);
}
