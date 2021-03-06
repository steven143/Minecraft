package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packet56MapChunks extends Packet
{
	private int[] chunkPostX;
	private int[] chunkPosZ;
	public int[] field_73590_a;
	public int[] field_73588_b;
	private byte[] chunkDataBuffer;
	private byte[][] field_73584_f;
	private int dataLength;
	private boolean skyLightSent;
	private static byte[] chunkDataNotCompressed = new byte[0];
	
	public Packet56MapChunks()
	{
	}
	
	public Packet56MapChunks(List par1List)
	{
		int var2 = par1List.size();
		chunkPostX = new int[var2];
		chunkPosZ = new int[var2];
		field_73590_a = new int[var2];
		field_73588_b = new int[var2];
		field_73584_f = new byte[var2][];
		skyLightSent = !par1List.isEmpty() && !((Chunk) par1List.get(0)).worldObj.provider.hasNoSky;
		int var3 = 0;
		for(int var4 = 0; var4 < var2; ++var4)
		{
			Chunk var5 = (Chunk) par1List.get(var4);
			Packet51MapChunkData var6 = Packet51MapChunk.getMapChunkData(var5, true, 65535);
			if(chunkDataNotCompressed.length < var3 + var6.compressedData.length)
			{
				byte[] var7 = new byte[var3 + var6.compressedData.length];
				System.arraycopy(chunkDataNotCompressed, 0, var7, 0, chunkDataNotCompressed.length);
				chunkDataNotCompressed = var7;
			}
			System.arraycopy(var6.compressedData, 0, chunkDataNotCompressed, var3, var6.compressedData.length);
			var3 += var6.compressedData.length;
			chunkPostX[var4] = var5.xPosition;
			chunkPosZ[var4] = var5.zPosition;
			field_73590_a[var4] = var6.chunkExistFlag;
			field_73588_b[var4] = var6.chunkHasAddSectionFlag;
			field_73584_f[var4] = var6.compressedData;
		}
		Deflater var11 = new Deflater(-1);
		try
		{
			var11.setInput(chunkDataNotCompressed, 0, var3);
			var11.finish();
			chunkDataBuffer = new byte[var3];
			dataLength = var11.deflate(chunkDataBuffer);
		} finally
		{
			var11.end();
		}
	}
	
	public byte[] getChunkCompressedData(int par1)
	{
		return field_73584_f[par1];
	}
	
	public int getChunkPosX(int par1)
	{
		return chunkPostX[par1];
	}
	
	public int getChunkPosZ(int par1)
	{
		return chunkPosZ[par1];
	}
	
	public int getNumberOfChunkInPacket()
	{
		return chunkPostX.length;
	}
	
	@Override public int getPacketSize()
	{
		return 6 + dataLength + 12 * getNumberOfChunkInPacket();
	}
	
	@Override public void processPacket(NetHandler par1NetHandler)
	{
		par1NetHandler.handleMapChunks(this);
	}
	
	@Override public void readPacketData(DataInput par1DataInput) throws IOException
	{
		short var2 = par1DataInput.readShort();
		dataLength = par1DataInput.readInt();
		skyLightSent = par1DataInput.readBoolean();
		chunkPostX = new int[var2];
		chunkPosZ = new int[var2];
		field_73590_a = new int[var2];
		field_73588_b = new int[var2];
		field_73584_f = new byte[var2][];
		if(chunkDataNotCompressed.length < dataLength)
		{
			chunkDataNotCompressed = new byte[dataLength];
		}
		par1DataInput.readFully(chunkDataNotCompressed, 0, dataLength);
		byte[] var3 = new byte[196864 * var2];
		Inflater var4 = new Inflater();
		var4.setInput(chunkDataNotCompressed, 0, dataLength);
		try
		{
			var4.inflate(var3);
		} catch(DataFormatException var12)
		{
			throw new IOException("Bad compressed data format");
		} finally
		{
			var4.end();
		}
		int var5 = 0;
		for(int var6 = 0; var6 < var2; ++var6)
		{
			chunkPostX[var6] = par1DataInput.readInt();
			chunkPosZ[var6] = par1DataInput.readInt();
			field_73590_a[var6] = par1DataInput.readShort();
			field_73588_b[var6] = par1DataInput.readShort();
			int var7 = 0;
			int var8 = 0;
			int var9;
			for(var9 = 0; var9 < 16; ++var9)
			{
				var7 += field_73590_a[var6] >> var9 & 1;
				var8 += field_73588_b[var6] >> var9 & 1;
			}
			var9 = 2048 * 4 * var7 + 256;
			var9 += 2048 * var8;
			if(skyLightSent)
			{
				var9 += 2048 * var7;
			}
			field_73584_f[var6] = new byte[var9];
			System.arraycopy(var3, var5, field_73584_f[var6], 0, var9);
			var5 += var9;
		}
	}
	
	@Override public void writePacketData(DataOutput par1DataOutput) throws IOException
	{
		par1DataOutput.writeShort(chunkPostX.length);
		par1DataOutput.writeInt(dataLength);
		par1DataOutput.writeBoolean(skyLightSent);
		par1DataOutput.write(chunkDataBuffer, 0, dataLength);
		for(int var2 = 0; var2 < chunkPostX.length; ++var2)
		{
			par1DataOutput.writeInt(chunkPostX[var2]);
			par1DataOutput.writeInt(chunkPosZ[var2]);
			par1DataOutput.writeShort((short) (field_73590_a[var2] & 65535));
			par1DataOutput.writeShort((short) (field_73588_b[var2] & 65535));
		}
	}
}
