package committee.nova.maced.api;

@SuppressWarnings("unused")
public interface ExtendedServerPlayer extends ExtendedPlayer {
    boolean maced$shouldSpawnExtraParticlesOnFall();

    void maced$setSpawnExtraParticlesOnFall(boolean spawn);
}
