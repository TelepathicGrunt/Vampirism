package de.teamlapen.vampirism.util;

import com.google.common.collect.Lists;
import de.teamlapen.vampirism.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.player.vampire.actions.BatVampireAction;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

import static de.teamlapen.lib.lib.util.UtilLib.getNull;


@SuppressWarnings("unused")
public class ASMHooks {

    @ObjectHolder("vampirism:sundamage")
    public static final Attribute attribute_sundamage = getNull();
    @ObjectHolder("vampirism:blood_exhaustion")
    public static final Attribute attribute_blood_exhaustion = getNull();
    @ObjectHolder("vampirism:bite_damage")
    public static final Attribute attribute_bite_damage = getNull();

    public static EntitySize getPlayerSize(PlayerEntity player, Pose pose) {
        return BatVampireAction.BAT_SIZE;
    }

    public static boolean overwritePlayerSize(PlayerEntity player) {
        return player.isAlive() && VampirePlayer.getOpt(player).map(vampire -> vampire.getSpecialAttributes().bat).orElse(false);
    }

    /**
     * JigsawPieces in this list only will be generated once per village
     * <p>
     * holds {@link net.minecraft.world.gen.feature.jigsaw.JigsawPiece#toString()}'s
     */
    private static final List<String> onlyOneStructure = Lists.newArrayList();

    public static void addSingleInstanceStructure(List<String> structures){
        onlyOneStructure.addAll(structures);
    }

    public static boolean checkStructures(List<StructurePiece> pieces, JigsawPiece jigsawPiece) {
        if (!onlyOneStructure.contains(jigsawPiece.toString())) return false;
        return pieces.stream().anyMatch(structurePiece -> onlyOneStructure.stream().anyMatch(string -> ((AbstractVillagePiece) structurePiece).getJigsawPiece().toString().equals(string)));
    }
}
