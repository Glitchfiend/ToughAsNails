package toughasnails.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import toughasnails.api.TANCapabilities;
import toughasnails.api.TANPotions;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;

public class TANCommand extends CommandBase
{
    @Override
    public String getName()
    {
        return "toughasnails";
    }
    
    @Override
    public List<String> getAliases()
    {
        return Lists.newArrayList("tan");
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
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.toughasnails.usage");
        }
        else if ("tempinfo".equals(args[0]))
        {
            displayTemperatureInfo(sender, args);
        }
        else if ("settemp".equals(args[0]))
        {
            setTemperature(sender, args);
        }
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
    
    private void setTemperature(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        TemperatureHandler temperatureStats = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
        int newTemp = parseInt(args[1], 0, TemperatureScale.getScaleTotal());

        if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE))
    	{
	        //Remove any existing potion effects for hypo/hyperthermia
	        player.removePotionEffect(TANPotions.hypothermia);
	        player.removePotionEffect(TANPotions.hyperthermia);
	
	        //Reset the change timer to 0
	        temperatureStats.setChangeTime(0);
	        //Set to the new temperature
	        temperatureStats.setTemperature(new Temperature(newTemp));
	
	        sender.sendMessage(new TextComponentTranslation("commands.toughasnails.settemp.success", newTemp));
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
            return getListOfStringsMatchingLastWord(args, "settemp", "tempinfo");
        }
        
        return null;
    }
}
