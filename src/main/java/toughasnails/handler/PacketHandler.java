package toughasnails.handler;

import toughasnails.core.ToughAsNails;
import toughasnails.network.message.MessageUpdateTemperature;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(ToughAsNails.MOD_ID);

    public static void init()
    {
        instance.registerMessage(MessageUpdateTemperature.class, MessageUpdateTemperature.class, 0, Side.CLIENT);
    }
}
