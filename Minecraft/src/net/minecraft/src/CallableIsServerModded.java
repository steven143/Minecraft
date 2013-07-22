package net.minecraft.src;

import java.util.concurrent.Callable;

import net.minecraft.server.MinecraftServer;

public class CallableIsServerModded implements Callable
{
	final MinecraftServer mcServer;
	
	public CallableIsServerModded(MinecraftServer p_i5006_1_)
	{
		mcServer = p_i5006_1_;
	}
	
	@Override public Object call()
	{
		return func_96558_a();
	}
	
	public String func_96558_a()
	{
		return mcServer.theProfiler.profilingEnabled ? mcServer.theProfiler.getNameOfLastSection() : "N/A (disabled)";
	}
}