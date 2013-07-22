package net.minecraft.src;

public class GuiErrorScreen extends GuiScreen
{
	private String message1;
	private String message2;
	
	public GuiErrorScreen(String p_i11003_1_, String p_i11003_2_)
	{
		message1 = p_i11003_1_;
		message2 = p_i11003_2_;
	}
	
	@Override protected void actionPerformed(GuiButton par1GuiButton)
	{
		mc.displayGuiScreen((GuiScreen) null);
	}
	
	@Override public void drawScreen(int par1, int par2, float par3)
	{
		drawGradientRect(0, 0, width, height, -12574688, -11530224);
		drawCenteredString(fontRenderer, message1, width / 2, 90, 16777215);
		drawCenteredString(fontRenderer, message2, width / 2, 110, 16777215);
		super.drawScreen(par1, par2, par3);
	}
	
	@Override public void initGui()
	{
		super.initGui();
		buttonList.add(new GuiButton(0, width / 2 - 100, 140, StatCollector.translateToLocal("gui.cancel")));
	}
	
	@Override protected void keyTyped(char par1, int par2)
	{
	}
}