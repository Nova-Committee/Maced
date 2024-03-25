package committee.nova.maced.init;

import committee.nova.maced.Maced;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MacedSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Maced.MODID);

    public static final RegistryObject<SoundEvent> MACE_SMASH_AIR = SOUNDS.register(
            "item.mace.smash_air",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_air"))
    );

    public static final RegistryObject<SoundEvent> MACE_SMASH_GROUND = SOUNDS.register(
            "item.mace.smash_ground",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_ground"))
    );

    public static final RegistryObject<SoundEvent> MACE_SMASH_GROUND_HEAVY = SOUNDS.register(
            "item.mace.smash_ground_heavy",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Maced.MODID, "item.mace.smash_ground_heavy"))
    );

}
