package toughasnails.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import toughasnails.api.TANCapabilities;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;

public class TemperatureInfoCommand extends CommandBase
{
    @Override
    public String getName()
    {
        return "tempinfo";
    }
    
    @Override
    public List<String> getAliases()
    {
        return Lists.newArrayList("tempinfo");
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "commands.toughasnails.usage";
    }
    
    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
    	displayTemperatureInfo(sender, args);
    }
    
    private void displayTemperatureInfo(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        TemperatureHandler temperatureStats = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
        TemperatureDebugger debugger = temperatureStats.debugger;

        if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE))
    	{
        	debugger.setGuiVisible(!debugger.isGuiVisible(), player);
    	}
        else
        {
        	sender.sendMessage(new TextComponentTranslation("commands.toughasnails.settemp.disabled"));
        }
    }
        
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "tempinfo");
        }
        
        return null;
    }
}