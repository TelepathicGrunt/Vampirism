package de.teamlapen.vampirism.entity.player.lord.actions;

import de.teamlapen.vampirism.REFERENCE;
import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus;

public class LordActions {
    public static final DeferredRegister<IAction<?>> ACTIONS = DeferredRegister.create(VampirismRegistries.ACTIONS_ID, REFERENCE.MODID);

    public static final RegistryObject<SpeedLordAction<?>> LORD_SPEED = ACTIONS.register("lord_speed", SpeedLordAction::new);
    public static final RegistryObject<AttackSpeedLordAction<?>> LORD_ATTACK_SPEED = ACTIONS.register("lord_attack_speed", AttackSpeedLordAction::new);

    @ApiStatus.Internal
    public static void register(IEventBus bus){
        ACTIONS.register(bus);
    }
}
