package de.teamlapen.vampirism.block;

import net.java.games.input.Keyboard;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import de.teamlapen.vampirism.item.ItemVampiresFear;
import de.teamlapen.vampirism.tileEntity.TileEntityBloodAltar;
import de.teamlapen.vampirism.util.Logger;

public class BlockBloodAltar extends BlockContainerVampirism {
	private final String TAG = "BlockBloodAltar";

	public BlockBloodAltar() {
		super(Material.rock);
		this.setBlockName("bloodAltar");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		Logger.i("BlockBloodAltar", "createNewTileEntity called");
		return new TileEntityBloodAltar();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack stack) {
		int l = MathHelper
				.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0)
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		else if (l == 1)
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		else if (l == 2)
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		else if (l == 3)
			world.setBlockMetadataWithNotify(x, y, z, 1, 2);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3,
			int par4, EntityPlayer player, int par6, float par7,
			float par8, float par9) {
		if (!par1World.isRemote) {
			Item item = null;
			try {
				item = player.inventory.getCurrentItem().getItem();
			} catch (NullPointerException e) {
				Logger.i(TAG, "No item in hand");
				//e.printStackTrace();
				return false;
			}
			
			if(item != null && ItemVampiresFear.class.isInstance(item)) {
				Logger.i(TAG, "Block activated");
				//TODO Become a vampire
			}
			return true;
		}
		return false;
	}
}
