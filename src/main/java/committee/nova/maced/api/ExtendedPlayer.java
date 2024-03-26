package committee.nova.maced.api;


import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public interface ExtendedPlayer {
    @Nullable
    Vector3d maced$getCurrentImpulseImpactPos();

    void maced$setCurrentImpulseImpactPos(Vector3d impactPos);

    boolean maced$shouldIgnoreFallDamageFromCurrentImpulse();

    void maced$setIgnoreFallDamageFromCurrentImpulse(boolean ignore);
}
