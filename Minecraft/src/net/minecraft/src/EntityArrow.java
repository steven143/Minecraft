package net.minecraft.src;

import java.util.List;

public class EntityArrow extends Entity implements IProjectile
{
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile;
	private int inData;
	private boolean inGround;
	public int canBePickedUp;
	public int arrowShake;
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	private double damage = 2.0D;
	private int knockbackStrength;
	
	public EntityArrow(World par1World)
	{
		super(par1World);
		renderDistanceWeight = 10.0D;
		setSize(0.5F, 0.5F);
	}
	
	public EntityArrow(World par1World, double par2, double par4, double par6)
	{
		super(par1World);
		renderDistanceWeight = 10.0D;
		setSize(0.5F, 0.5F);
		setPosition(par2, par4, par6);
		yOffset = 0.0F;
	}
	
	public EntityArrow(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5)
	{
		super(par1World);
		renderDistanceWeight = 10.0D;
		shootingEntity = par2EntityLivingBase;
		if(par2EntityLivingBase instanceof EntityPlayer)
		{
			canBePickedUp = 1;
		}
		posY = par2EntityLivingBase.posY + par2EntityLivingBase.getEyeHeight() - 0.10000000149011612D;
		double var6 = par3EntityLivingBase.posX - par2EntityLivingBase.posX;
		double var8 = par3EntityLivingBase.boundingBox.minY + par3EntityLivingBase.height / 3.0F - posY;
		double var10 = par3EntityLivingBase.posZ - par2EntityLivingBase.posZ;
		double var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);
		if(var12 >= 1.0E-7D)
		{
			float var14 = (float) (Math.atan2(var10, var6) * 180.0D / Math.PI) - 90.0F;
			float var15 = (float) -(Math.atan2(var8, var12) * 180.0D / Math.PI);
			double var16 = var6 / var12;
			double var18 = var10 / var12;
			setLocationAndAngles(par2EntityLivingBase.posX + var16, posY, par2EntityLivingBase.posZ + var18, var14, var15);
			yOffset = 0.0F;
			float var20 = (float) var12 * 0.2F;
			setThrowableHeading(var6, var8 + var20, var10, par4, par5);
		}
	}
	
	public EntityArrow(World par1World, EntityLivingBase par2EntityLivingBase, float par3)
	{
		super(par1World);
		renderDistanceWeight = 10.0D;
		shootingEntity = par2EntityLivingBase;
		if(par2EntityLivingBase instanceof EntityPlayer)
		{
			canBePickedUp = 1;
		}
		setSize(0.5F, 0.5F);
		setLocationAndAngles(par2EntityLivingBase.posX, par2EntityLivingBase.posY + par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.posZ, par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
		motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
		setThrowableHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
	}
	
	@Override public boolean canAttackWithItem()
	{
		return false;
	}
	
	@Override protected boolean canTriggerWalking()
	{
		return false;
	}
	
	@Override protected void entityInit()
	{
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}
	
	public double getDamage()
	{
		return damage;
	}
	
	public boolean getIsCritical()
	{
		byte var1 = dataWatcher.getWatchableObjectByte(16);
		return (var1 & 1) != 0;
	}
	
	@Override public float getShadowSize()
	{
		return 0.0F;
	}
	
	@Override public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
	{
		if(!worldObj.isRemote && inGround && arrowShake <= 0)
		{
			boolean var2 = canBePickedUp == 1 || canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;
			if(canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
			{
				var2 = false;
			}
			if(var2)
			{
				playSound("random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				par1EntityPlayer.onItemPickup(this, 1);
				setDead();
			}
		}
	}
	
	@Override public void onUpdate()
	{
		super.onUpdate();
		if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
		{
			float var1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, var1) * 180.0D / Math.PI);
		}
		int var16 = worldObj.getBlockId(xTile, yTile, zTile);
		if(var16 > 0)
		{
			Block.blocksList[var16].setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
			AxisAlignedBB var2 = Block.blocksList[var16].getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);
			if(var2 != null && var2.isVecInside(worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ)))
			{
				inGround = true;
			}
		}
		if(arrowShake > 0)
		{
			--arrowShake;
		}
		if(inGround)
		{
			int var18 = worldObj.getBlockId(xTile, yTile, zTile);
			int var19 = worldObj.getBlockMetadata(xTile, yTile, zTile);
			if(var18 == inTile && var19 == inData)
			{
				++ticksInGround;
				if(ticksInGround == 1200)
				{
					setDead();
				}
			} else
			{
				inGround = false;
				motionX *= rand.nextFloat() * 0.2F;
				motionY *= rand.nextFloat() * 0.2F;
				motionZ *= rand.nextFloat() * 0.2F;
				ticksInGround = 0;
				ticksInAir = 0;
			}
		} else
		{
			++ticksInAir;
			Vec3 var17 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
			Vec3 var3 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
			MovingObjectPosition var4 = worldObj.rayTraceBlocks_do_do(var17, var3, false, true);
			var17 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
			var3 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
			if(var4 != null)
			{
				var3 = worldObj.getWorldVec3Pool().getVecFromPool(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
			}
			Entity var5 = null;
			List var6 = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double var7 = 0.0D;
			int var9;
			float var11;
			for(var9 = 0; var9 < var6.size(); ++var9)
			{
				Entity var10 = (Entity) var6.get(var9);
				if(var10.canBeCollidedWith() && (var10 != shootingEntity || ticksInAir >= 5))
				{
					var11 = 0.3F;
					AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
					MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);
					if(var13 != null)
					{
						double var14 = var17.distanceTo(var13.hitVec);
						if(var14 < var7 || var7 == 0.0D)
						{
							var5 = var10;
							var7 = var14;
						}
					}
				}
			}
			if(var5 != null)
			{
				var4 = new MovingObjectPosition(var5);
			}
			if(var4 != null && var4.entityHit != null && var4.entityHit instanceof EntityPlayer)
			{
				EntityPlayer var21 = (EntityPlayer) var4.entityHit;
				if(var21.capabilities.disableDamage || shootingEntity instanceof EntityPlayer && !((EntityPlayer) shootingEntity).func_96122_a(var21))
				{
					var4 = null;
				}
			}
			float var20;
			float var27;
			if(var4 != null)
			{
				if(var4.entityHit != null)
				{
					var20 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
					int var24 = MathHelper.ceiling_double_int(var20 * damage);
					if(getIsCritical())
					{
						var24 += rand.nextInt(var24 / 2 + 2);
					}
					DamageSource var22 = null;
					if(shootingEntity == null)
					{
						var22 = DamageSource.causeArrowDamage(this, this);
					} else
					{
						var22 = DamageSource.causeArrowDamage(this, shootingEntity);
					}
					if(isBurning() && !(var4.entityHit instanceof EntityEnderman))
					{
						var4.entityHit.setFire(5);
					}
					if(var4.entityHit.attackEntityFrom(var22, var24))
					{
						if(var4.entityHit instanceof EntityLivingBase)
						{
							EntityLivingBase var25 = (EntityLivingBase) var4.entityHit;
							if(!worldObj.isRemote)
							{
								var25.setArrowCountInEntity(var25.getArrowCountInEntity() + 1);
							}
							if(knockbackStrength > 0)
							{
								var27 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
								if(var27 > 0.0F)
								{
									var4.entityHit.addVelocity(motionX * knockbackStrength * 0.6000000238418579D / var27, 0.1D, motionZ * knockbackStrength * 0.6000000238418579D / var27);
								}
							}
							if(shootingEntity != null)
							{
								EnchantmentThorns.func_92096_a(shootingEntity, var25, rand);
							}
							if(shootingEntity != null && var4.entityHit != shootingEntity && var4.entityHit instanceof EntityPlayer && shootingEntity instanceof EntityPlayerMP)
							{
								((EntityPlayerMP) shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
							}
						}
						playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
						if(!(var4.entityHit instanceof EntityEnderman))
						{
							setDead();
						}
					} else
					{
						motionX *= -0.10000000149011612D;
						motionY *= -0.10000000149011612D;
						motionZ *= -0.10000000149011612D;
						rotationYaw += 180.0F;
						prevRotationYaw += 180.0F;
						ticksInAir = 0;
					}
				} else
				{
					xTile = var4.blockX;
					yTile = var4.blockY;
					zTile = var4.blockZ;
					inTile = worldObj.getBlockId(xTile, yTile, zTile);
					inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
					motionX = (float) (var4.hitVec.xCoord - posX);
					motionY = (float) (var4.hitVec.yCoord - posY);
					motionZ = (float) (var4.hitVec.zCoord - posZ);
					var20 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
					posX -= motionX / var20 * 0.05000000074505806D;
					posY -= motionY / var20 * 0.05000000074505806D;
					posZ -= motionZ / var20 * 0.05000000074505806D;
					playSound("random.bowhit", 1.0F, 1.2F / (rand.nextFloat() * 0.2F + 0.9F));
					inGround = true;
					arrowShake = 7;
					setIsCritical(false);
					if(inTile != 0)
					{
						Block.blocksList[inTile].onEntityCollidedWithBlock(worldObj, xTile, yTile, zTile, this);
					}
				}
			}
			if(getIsCritical())
			{
				for(var9 = 0; var9 < 4; ++var9)
				{
					worldObj.spawnParticle("crit", posX + motionX * var9 / 4.0D, posY + motionY * var9 / 4.0D, posZ + motionZ * var9 / 4.0D, -motionX, -motionY + 0.2D, -motionZ);
				}
			}
			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			var20 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
			for(rotationPitch = (float) (Math.atan2(motionY, var20) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F)
			{
				;
			}
			while(rotationPitch - prevRotationPitch >= 180.0F)
			{
				prevRotationPitch += 360.0F;
			}
			while(rotationYaw - prevRotationYaw < -180.0F)
			{
				prevRotationYaw -= 360.0F;
			}
			while(rotationYaw - prevRotationYaw >= 180.0F)
			{
				prevRotationYaw += 360.0F;
			}
			rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
			float var23 = 0.99F;
			var11 = 0.05F;
			if(isInWater())
			{
				for(int var26 = 0; var26 < 4; ++var26)
				{
					var27 = 0.25F;
					worldObj.spawnParticle("bubble", posX - motionX * var27, posY - motionY * var27, posZ - motionZ * var27, motionX, motionY, motionZ);
				}
				var23 = 0.8F;
			}
			motionX *= var23;
			motionY *= var23;
			motionZ *= var23;
			motionY -= var11;
			setPosition(posX, posY, posZ);
			doBlockCollisions();
		}
	}
	
	@Override public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		xTile = par1NBTTagCompound.getShort("xTile");
		yTile = par1NBTTagCompound.getShort("yTile");
		zTile = par1NBTTagCompound.getShort("zTile");
		inTile = par1NBTTagCompound.getByte("inTile") & 255;
		inData = par1NBTTagCompound.getByte("inData") & 255;
		arrowShake = par1NBTTagCompound.getByte("shake") & 255;
		inGround = par1NBTTagCompound.getByte("inGround") == 1;
		if(par1NBTTagCompound.hasKey("damage"))
		{
			damage = par1NBTTagCompound.getDouble("damage");
		}
		if(par1NBTTagCompound.hasKey("pickup"))
		{
			canBePickedUp = par1NBTTagCompound.getByte("pickup");
		} else if(par1NBTTagCompound.hasKey("player"))
		{
			canBePickedUp = par1NBTTagCompound.getBoolean("player") ? 1 : 0;
		}
	}
	
	public void setDamage(double par1)
	{
		damage = par1;
	}
	
	public void setIsCritical(boolean par1)
	{
		byte var2 = dataWatcher.getWatchableObjectByte(16);
		if(par1)
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
		} else
		{
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
		}
	}
	
	public void setKnockbackStrength(int par1)
	{
		knockbackStrength = par1;
	}
	
	@Override public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		setPosition(par1, par3, par5);
		setRotation(par7, par8);
	}
	
	@Override public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
	{
		float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
		par1 /= var9;
		par3 /= var9;
		par5 /= var9;
		par1 += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
		par3 += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
		par5 += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
		par1 *= par7;
		par3 *= par7;
		par5 *= par7;
		motionX = par1;
		motionY = par3;
		motionZ = par5;
		float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		prevRotationYaw = rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float) (Math.atan2(par3, var10) * 180.0D / Math.PI);
		ticksInGround = 0;
	}
	
	@Override public void setVelocity(double par1, double par3, double par5)
	{
		motionX = par1;
		motionY = par3;
		motionZ = par5;
		if(prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
		{
			float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(par3, var7) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch;
			prevRotationYaw = rotationYaw;
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			ticksInGround = 0;
		}
	}
	
	@Override public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("xTile", (short) xTile);
		par1NBTTagCompound.setShort("yTile", (short) yTile);
		par1NBTTagCompound.setShort("zTile", (short) zTile);
		par1NBTTagCompound.setByte("inTile", (byte) inTile);
		par1NBTTagCompound.setByte("inData", (byte) inData);
		par1NBTTagCompound.setByte("shake", (byte) arrowShake);
		par1NBTTagCompound.setByte("inGround", (byte) (inGround ? 1 : 0));
		par1NBTTagCompound.setByte("pickup", (byte) canBePickedUp);
		par1NBTTagCompound.setDouble("damage", damage);
	}
}
