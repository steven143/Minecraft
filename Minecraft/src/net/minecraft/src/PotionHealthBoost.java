package net.minecraft.src;

public class PotionHealthBoost extends Potion
{
	public PotionHealthBoost(int par1, boolean par2, int par3)
	{
		super(par1, par2, par3);
	}
	
	@Override public void func_111187_a(EntityLivingBase par1EntityLivingBase, BaseAttributeMap par2BaseAttributeMap, int par3)
	{
		super.func_111187_a(par1EntityLivingBase, par2BaseAttributeMap, par3);
		if(par1EntityLivingBase.func_110143_aJ() > par1EntityLivingBase.func_110138_aP())
		{
			par1EntityLivingBase.setEntityHealth(par1EntityLivingBase.func_110138_aP());
		}
	}
}
