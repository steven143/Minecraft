package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Packet44UpdateAttributes extends Packet
{
	private int field_111005_a;
	private final List field_111004_b = new ArrayList();
	
	public Packet44UpdateAttributes()
	{
	}
	
	public Packet44UpdateAttributes(int par1, Collection par2Collection)
	{
		field_111005_a = par1;
		Iterator var3 = par2Collection.iterator();
		while(var3.hasNext())
		{
			AttributeInstance var4 = (AttributeInstance) var3.next();
			field_111004_b.add(new Packet44UpdateAttributesSnapshot(this, var4.func_111123_a().func_111108_a(), var4.func_111125_b(), var4.func_111122_c()));
		}
	}
	
	public int func_111002_d()
	{
		return field_111005_a;
	}
	
	public List func_111003_f()
	{
		return field_111004_b;
	}
	
	@Override public int getPacketSize()
	{
		return 8 + field_111004_b.size() * 24;
	}
	
	@Override public void processPacket(NetHandler par1NetHandler)
	{
		par1NetHandler.func_110773_a(this);
	}
	
	@Override public void readPacketData(DataInput par1DataInput) throws IOException
	{
		field_111005_a = par1DataInput.readInt();
		int var2 = par1DataInput.readInt();
		for(int var3 = 0; var3 < var2; ++var3)
		{
			String var4 = readString(par1DataInput, 64);
			double var5 = par1DataInput.readDouble();
			ArrayList var7 = new ArrayList();
			short var8 = par1DataInput.readShort();
			for(int var9 = 0; var9 < var8; ++var9)
			{
				UUID var10 = new UUID(par1DataInput.readLong(), par1DataInput.readLong());
				var7.add(new AttributeModifier(var10, "Unknown synced attribute modifier", par1DataInput.readDouble(), par1DataInput.readByte()));
			}
			field_111004_b.add(new Packet44UpdateAttributesSnapshot(this, var4, var5, var7));
		}
	}
	
	@Override public void writePacketData(DataOutput par1DataOutput) throws IOException
	{
		par1DataOutput.writeInt(field_111005_a);
		par1DataOutput.writeInt(field_111004_b.size());
		Iterator var2 = field_111004_b.iterator();
		while(var2.hasNext())
		{
			Packet44UpdateAttributesSnapshot var3 = (Packet44UpdateAttributesSnapshot) var2.next();
			writeString(var3.func_142040_a(), par1DataOutput);
			par1DataOutput.writeDouble(var3.func_142041_b());
			par1DataOutput.writeShort(var3.func_142039_c().size());
			Iterator var4 = var3.func_142039_c().iterator();
			while(var4.hasNext())
			{
				AttributeModifier var5 = (AttributeModifier) var4.next();
				par1DataOutput.writeLong(var5.func_111167_a().getMostSignificantBits());
				par1DataOutput.writeLong(var5.func_111167_a().getLeastSignificantBits());
				par1DataOutput.writeDouble(var5.func_111164_d());
				par1DataOutput.writeByte(var5.func_111169_c());
			}
		}
	}
}
