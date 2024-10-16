package de.teamlapen.vampirism.api;

import com.google.common.base.Suppliers;
import de.teamlapen.vampirism.api.entity.actions.IEntityAction;
import de.teamlapen.vampirism.api.entity.minion.IMinionTask;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.refinement.IRefinement;
import de.teamlapen.vampirism.api.entity.player.refinement.IRefinementSet;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.task.Task;
import de.teamlapen.vampirism.api.items.oil.IOil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Registry keys for all Vampirism registries and registry access for api usages
 */
@SuppressWarnings("unused")
public class VampirismRegistries {

    public static final ResourceKey<Registry<ISkill<?>>> SKILLS_ID = key("vampirism:skills");
    public static final ResourceKey<Registry<IAction<?>>> ACTIONS_ID = key("vampirism:actions");
    public static final ResourceKey<Registry<IEntityAction>> ENTITY_ACTIONS_ID = key("vampirism:entityactions");
    public static final ResourceKey<Registry<IMinionTask<?, ?>>> MINION_TASKS_ID = key("vampirism:miniontasks");
    public static final ResourceKey<Registry<Task>> TASK_ID = key("vampirism:tasks");
    public static final ResourceKey<Registry<IRefinement>> REFINEMENT_ID = key("vampirism:refinement");
    public static final ResourceKey<Registry<IRefinementSet>> REFINEMENT_SET_ID = key("vampirism:refinement_set");
    public static final ResourceKey<Registry<IOil>> OILS_ID = key("vampirism:oil");

    public static final Supplier<IForgeRegistry<ISkill<?>>> SKILLS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(SKILLS_ID));
    public static final Supplier<IForgeRegistry<IAction<?>>> ACTIONS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(ACTIONS_ID));
    public static final Supplier<IForgeRegistry<IEntityAction>> ENTITY_ACTIONS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(ENTITY_ACTIONS_ID));
    public static final Supplier<IForgeRegistry<IMinionTask<?, ?>>> MINION_TASKS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(MINION_TASKS_ID));
    public static final Supplier<IForgeRegistry<Task>> TASKS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(TASK_ID));
    public static final Supplier<IForgeRegistry<IRefinement>> REFINEMENTS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(REFINEMENT_ID));
    public static final Supplier<IForgeRegistry<IRefinementSet>> REFINEMENT_SETS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(REFINEMENT_SET_ID));
    public static final Supplier<IForgeRegistry<IOil>> OILS = Suppliers.memoize(() -> RegistryManager.ACTIVE.getRegistry(OILS_ID));

    private static <T> @NotNull ResourceKey<Registry<T>> key(@NotNull String name) {
        return ResourceKey.createRegistryKey(new ResourceLocation(name));
    }

}