package net.minecraft.src;

import java.util.regex.Pattern;

public class StringUtils
{
	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	
	public static String stripControlCodes(String p_76338_0_)
	{
		return patternControlCode.matcher(p_76338_0_).replaceAll("");
	}
	
	public static String ticksToElapsedTime(int par0)
	{
		int var1 = par0 / 20;
		int var2 = var1 / 60;
		var1 %= 60;
		return var1 < 10 ? var2 + ":0" + var1 : var2 + ":" + var1;
	}
}