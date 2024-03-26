package committee.nova.maced.init;

import committee.nova.maced.Maced;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MacedSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Maced.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MACE_SMASH_AIR = SOUNDS.register(
            "item.mace.smash_air",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_air"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> MACE_SMASH_GROUND = SOUNDS.register(
            "item.mace.smash_ground",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_ground"))
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> MACE_SMASH_GROUND_HEAVY = SOUNDS.register(
            "item.mace.smash_ground_heavy",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_ground_heavy"))
    );

}
