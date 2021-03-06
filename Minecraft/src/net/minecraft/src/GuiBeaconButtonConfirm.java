package net.minecraft.src;

class GuiBeaconButtonConfirm extends GuiBeaconButton
{
	final GuiBeacon beaconGui;
	
	public GuiBeaconButtonConfirm(GuiBeacon par1GuiBeacon, int par2, int par3, int par4)
	{
		super(par2, par3, par4, GuiBeacon.func_110427_g(), 90, 220);
		beaconGui = par1GuiBeacon;
	}
	
	@Override public void func_82251_b(int par1, int par2)
	{
		beaconGui.drawCreativeTabHoveringText(I18n.func_135053_a("gui.done"), par1, par2);
	}
}
