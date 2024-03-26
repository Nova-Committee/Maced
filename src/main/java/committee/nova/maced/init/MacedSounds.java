package committee.nova.maced.init;

import committee.nova.maced.Maced;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class MacedSounds {
    public static final SoundEvent MACE_SMASH_AIR = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            new ResourceLocation(Maced.MODID, "item.mace.smash_air"),
            SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_air"))
    );

    public static final SoundEvent MACE_SMASH_GROUND = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            new ResourceLocation(Maced.MODID, "item.mace.smash_ground"),
            SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_ground"))
    );

    public static final SoundEvent MACE_SMASH_GROUND_HEAVY = Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            new ResourceLocation(Maced.MODID, "item.mace.smash_ground_heavy"),
            SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_ground_heavy"))
    );

    public static void register() {

    }
}
