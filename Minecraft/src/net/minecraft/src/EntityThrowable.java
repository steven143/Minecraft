package net.minecraft.src;

import java.util.List;

public abstract class EntityThrowable extends Entity implements IProjectile
{
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile;
	protected boolean inGround;
	public int throwableShake;
	private EntityLivingBase thrower;
	private String throwerName;
	private int ticksInGround;
	private int ticksInAir;
	
	public EntityThrowable(World par1World)
	{
		super(par1World);
		setSize(0.25F, 0.25F);
	}
	
	public EntityThrowable(World par1World, double par2, double par4, double par6)
	{
		super(par1World);
		ticksInGround = 0;
		setSize(0.25F, 0.25F);
		setPosition(par2, par4, par6);
		yOffset = 0.0F;
	}
	
	public EntityThrowable(World par1World, EntityLivingBase par2EntityLivingBase)
	{
		super(par1World);
		thrower = par2EntityLivingBase;
		setSize(0.25F, 0.25F);
		setLocationAndAngles(par2EntityLivingBase.posX, par2EntityLivingBase.posY + par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.posZ, par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
		posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		float var3 = 0.4F;
		motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * var3;
		motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * var3;
		motionY = -MathHelper.sin((rotationPitch + func_70183_g()) / 180.0F * (float) Math.PI) * var3;
		setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), 1.0F);
	}
	
	@Override protected void entityInit()
	{
	}
	
	protected float func_70182_d()
	{
		return 1.5F;
	}
	
	protected float func_70183_g()
	{
		return 0.0F;
	}
	
	protected float getGravityVelocity()
	{
		return 0.03F;
	}
	
	@Override public float getShadowSize()
	{
		return 0.0F;
	}
	
	public EntityLivingBase getThrower()
	{
		if(thrower == null && throwerName != null && throwerName.length() > 0)
		{
			thrower = worldObj.getPlayerEntityByName(throwerName);
		}
		return thrower;
	}
	
	@Override public boolean isInRangeToRenderDist(double par1)
	{
		double var3 = boundingBox.getAverageEdgeLength() * 4.0D;
		var3 *= 64.0D;
		return par1 < var3 * var3;
	}
	
	protected abstract void onImpact(MovingObjectPosition var1);
	
	@Override public void onUpdate()
	{
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		super.onUpdate();
		if(throwableShake > 0)
		{
			--throwableShake;
		}
		if(inGround)
		{
			int var1 = worldObj.getBlockId(xTile, yTile, zTile);
			if(var1 == inTile)
			{
				++ticksInGround;
				if(ticksInGround == 1200)
				{
					setDead();
				}
				return;
			}
			inGround = false;
			motionX *= rand.nextFloat() * 0.2F;
			motionY *= rand.nextFloat() * 0.2F;
			motionZ *= rand.nextFloat() * 0.2F;
			ticksInGround = 0;
			ticksInAir = 0;
		} else
		{
			++ticksInAir;
		}
		Vec3 var16 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
		Vec3 var2 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition var3 = worldObj.clip(var16, var2);
		var16 = worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
		var2 = worldObj.getWorldVec3Pool().getVecFromPool(posX + motionX, posY + motionY, posZ + motionZ);
		if(var3 != null)
		{
			var2 = worldObj.getWorldVec3Pool().getVecFromPool(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
		}
		if(!worldObj.isRemote)
		{
			Entity var4 = null;
			List var5 = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;
			EntityLivingBase var8 = getThrower();
			for(int var9 = 0; var9 < var5.size(); ++var9)
			{
				Entity var10 = (Entity) var5.get(var9);
				if(var10.canBeCollidedWith() && (var10 != var8 || ticksInAir >= 5))
				{
					float var11 = 0.3F;
					AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
					MovingObjectPosition var13 = var12.calculateIntercept(var16, var2);
					if(var13 != null)
					{
						double var14 = var16.distanceTo(var13.hitVec);
						if(var14 < var6 || var6 == 0.0D)
						{
							var4 = var10;
							var6 = var14;
						}
					}
				}
			}
			if(var4 != null)
			{
				var3 = new MovingObjectPosition(var4);
			}
		}
		if(var3 != null)
		{
			if(var3.typeOfHit == EnumMovingObjectType.TILE && worldObj.getBlockId(var3.blockX, var3.blockY, var3.blockZ) == Block.portal.blockID)
			{
				setInPortal();
			} else
			{
				onImpact(var3);
			}
		}
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float var17 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
		for(rotationPitch = (float) (Math.atan2(motionY, var17) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F)
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
		float var18 = 0.99F;
		float var19 = getGravityVelocity();
		if(isInWater())
		{
			for(int var7 = 0; var7 < 4; ++var7)
			{
				float var20 = 0.25F;
				worldObj.spawnParticle("bubble", posX - motionX * var20, posY - motionY * var20, posZ - motionZ * var20, motionX, motionY, motionZ);
			}
			var18 = 0.8F;
		}
		motionX *= var18;
		motionY *= var18;
		motionZ *= var18;
		motionY -= var19;
		setPosition(posX, posY, posZ);
	}
	
	@Override public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		xTile = par1NBTTagCompound.getShort("xTile");
		yTile = par1NBTTagCompound.getShort("yTile");
		zTile = par1NBTTagCompound.getShort("zTile");
		inTile = par1NBTTagCompound.getByte("inTile") & 255;
		throwableShake = par1NBTTagCompound.getByte("shake") & 255;
		inGround = par1NBTTagCompound.getByte("inGround") == 1;
		throwerName = par1NBTTagCompound.getString("ownerName");
		if(throwerName != null && throwerName.length() == 0)
		{
			throwerName = null;
		}
	}
	
	@Override public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
	{
		float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
		par1 /= var9;
		par3 /= var9;
		par5 /= var9;
		par1 += rand.nextGaussian() * 0.007499999832361937D * par8;
		par3 += rand.nextGaussian() * 0.007499999832361937D * par8;
		par5 += rand.nextGaussian() * 0.007499999832361937D * par8;
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
		}
	}
	
	@Override public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("xTile", (short) xTile);
		par1NBTTagCompound.setShort("yTile", (short) yTile);
		par1NBTTagCompound.setShort("zTile", (short) zTile);
		par1NBTTagCompound.setByte("inTile", (byte) inTile);
		par1NBTTagCompound.setByte("shake", (byte) throwableShake);
		par1NBTTagCompound.setByte("inGround", (byte) (inGround ? 1 : 0));
		if((throwerName == null || throwerName.length() == 0) && thrower != null && thrower instanceof EntityPlayer)
		{
			throwerName = thrower.getEntityName();
		}
		par1NBTTagCompound.setString("ownerName", throwerName == null ? "" : throwerName);
	}
}
