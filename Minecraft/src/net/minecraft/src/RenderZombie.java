package net.minecraft.src;

public class RenderZombie extends RenderBiped
{
	private static final ResourceLocation field_110866_o = new ResourceLocation("textures/entity/zombie_pigman.png");
	private static final ResourceLocation field_110865_p = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation field_110864_q = new ResourceLocation("textures/entity/zombie/zombie_villager.png");
	private ModelBiped field_82434_o;
	private ModelZombieVillager field_82432_p;
	protected ModelBiped field_82437_k;
	protected ModelBiped field_82435_l;
	protected ModelBiped field_82436_m;
	protected ModelBiped field_82433_n;
	private int field_82431_q = 1;
	
	public RenderZombie()
	{
		super(new ModelZombie(), 0.5F, 1.0F);
		field_82434_o = modelBipedMain;
		field_82432_p = new ModelZombieVillager();
	}
	
	@Override public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		func_82426_a((EntityZombie) par1Entity, par2, par4, par6, par8, par9);
	}
	
	@Override public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
	{
		func_82426_a((EntityZombie) par1EntityLiving, par2, par4, par6, par8, par9);
	}
	
	@Override protected ResourceLocation func_110775_a(Entity par1Entity)
	{
		return func_110863_a((EntityZombie) par1Entity);
	}
	
	@Override protected ResourceLocation func_110856_a(EntityLiving par1EntityLiving)
	{
		return func_110863_a((EntityZombie) par1EntityLiving);
	}
	
	protected ResourceLocation func_110863_a(EntityZombie par1EntityZombie)
	{
		return par1EntityZombie instanceof EntityPigZombie ? field_110866_o : par1EntityZombie.isVillager() ? field_110864_q : field_110865_p;
	}
	
	@Override protected void func_130005_c(EntityLiving par1EntityLiving, float par2)
	{
		func_82428_a((EntityZombie) par1EntityLiving, par2);
	}
	
	@Override protected int func_130006_a(EntityLiving par1EntityLiving, int par2, float par3)
	{
		return func_82429_a((EntityZombie) par1EntityLiving, par2, par3);
	}
	
	@Override protected void func_82421_b()
	{
		field_82423_g = new ModelZombie(1.0F, true);
		field_82425_h = new ModelZombie(0.5F, true);
		field_82437_k = field_82423_g;
		field_82435_l = field_82425_h;
		field_82436_m = new ModelZombieVillager(1.0F, 0.0F, true);
		field_82433_n = new ModelZombieVillager(0.5F, 0.0F, true);
	}
	
	public void func_82426_a(EntityZombie par1EntityZombie, double par2, double par4, double par6, float par8, float par9)
	{
		func_82427_a(par1EntityZombie);
		super.doRenderLiving(par1EntityZombie, par2, par4, par6, par8, par9);
	}
	
	private void func_82427_a(EntityZombie par1EntityZombie)
	{
		if(par1EntityZombie.isVillager())
		{
			if(field_82431_q != field_82432_p.func_82897_a())
			{
				field_82432_p = new ModelZombieVillager();
				field_82431_q = field_82432_p.func_82897_a();
				field_82436_m = new ModelZombieVillager(1.0F, 0.0F, true);
				field_82433_n = new ModelZombieVillager(0.5F, 0.0F, true);
			}
			mainModel = field_82432_p;
			field_82423_g = field_82436_m;
			field_82425_h = field_82433_n;
		} else
		{
			mainModel = field_82434_o;
			field_82423_g = field_82437_k;
			field_82425_h = field_82435_l;
		}
		modelBipedMain = (ModelBiped) mainModel;
	}
	
	protected void func_82428_a(EntityZombie par1EntityZombie, float par2)
	{
		func_82427_a(par1EntityZombie);
		super.func_130005_c(par1EntityZombie, par2);
	}
	
	protected int func_82429_a(EntityZombie par1EntityZombie, int par2, float par3)
	{
		func_82427_a(par1EntityZombie);
		return super.func_130006_a(par1EntityZombie, par2, par3);
	}
	
	protected void func_82430_a(EntityZombie par1EntityZombie, float par2, float par3, float par4)
	{
		if(par1EntityZombie.isConverting())
		{
			par3 += (float) (Math.cos(par1EntityZombie.ticksExisted * 3.25D) * Math.PI * 0.25D);
		}
		super.rotateCorpse(par1EntityZombie, par2, par3, par4);
	}
	
	@Override protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2)
	{
		func_82428_a((EntityZombie) par1EntityLivingBase, par2);
	}
	
	@Override public void renderPlayer(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9)
	{
		func_82426_a((EntityZombie) par1EntityLivingBase, par2, par4, par6, par8, par9);
	}
	
	@Override protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
	{
		func_82430_a((EntityZombie) par1EntityLivingBase, par2, par3, par4);
	}
	
	@Override protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
	{
		return func_82429_a((EntityZombie) par1EntityLivingBase, par2, par3);
	}
}
