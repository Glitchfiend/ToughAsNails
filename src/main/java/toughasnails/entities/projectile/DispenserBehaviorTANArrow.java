package toughasnails.entities.projectile;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class DispenserBehaviorTANArrow extends BehaviorProjectileDispense
{
	@Override
	protected IProjectile getProjectileEntity(World world, IPosition iPosition)
	{
		return new EntityTANArrow(world, iPosition.getX(), iPosition.getY(), iPosition.getZ());
	}
}