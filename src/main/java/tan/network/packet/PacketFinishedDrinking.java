package tan.network.packet;

import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import tan.eventhandler.ThirstItemEventHandler;
import tan.network.PacketTypeHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketFinishedDrinking extends PacketTAN
{
    private int itemID;
    private int metadata;

    private String fluidName;
    private int fluidAmount;

    public PacketFinishedDrinking()
    {
        super(PacketTypeHandler.finishedDrinking);
    }

    public PacketFinishedDrinking(int itemID, int metadata, String fluidName, int fluidAmount)
    {
        super(PacketTypeHandler.finishedDrinking);

        this.itemID = itemID;
        this.metadata = metadata;

        this.fluidName = fluidName;
        this.fluidAmount = fluidAmount;
    }

    @Override
    public void readData(DataInputStream data) throws IOException
    {
        this.itemID = data.readInt();
        this.metadata = data.readInt();

        this.fluidName = data.readUTF();
        this.fluidAmount = data.readInt();
    }

    @Override
    public void writeData(DataOutputStream data) throws IOException
    {
        data.writeInt(itemID);
        data.writeInt(metadata);

        data.writeUTF(fluidName);
        data.writeInt(fluidAmount);
    }

    @Override
    public void execute(INetworkManager network, Player player)
    {
        EntityPlayer entityPlayer = (EntityPlayer)player;

        if (!entityPlayer.worldObj.isRemote)
        {
            ThirstItemEventHandler.onFinishedDrinking(entityPlayer, itemID, metadata, fluidName, fluidAmount);
        }
    }
}
