package toughasnails.handler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import toughasnails.core.ToughAsNails;
import toughasnails.network.message.MessageSyncConfigs;
import toughasnails.network.message.MessageSyncSeasonCycle;
import toughasnails.network.message.MessageTemperatureClient;
import toughasnails.network.message.MessageToggleUI;
import toughasnails.network.message.MessageUpdateStat;

public class PacketHandler
{
    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(ToughAsNails.MOD_ID);

    public static void init()
    {
        instance.registerMessage(MessageUpdateStat.class, MessageUpdateStat.class, 0, Side.CLIENT);
        instance.registerMessage(MessageTemperatureClient.class, MessageTemperatureClient.class, 1, Side.CLIENT);
        instance.registerMessage(MessageToggleUI.class, MessageToggleUI.class, 2, Side.CLIENT);
        instance.registerMessage(MessageSyncSeasonCycle.class, MessageSyncSeasonCycle.class, 3, Side.CLIENT);
        instance.registerMessage(MessageSyncConfigs.class, MessageSyncConfigs.class, 4, Side.CLIENT);
    }
}
