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
import toughasnails.api.TANPotions;
import toughasnails.api.config.GameplayOption;
import toughasnails.api.config.SyncedConfig;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureHandler;

public class SetTemperatureCommand extends CommandBase
{
    @Override
    public String getName()
    {
        return "settemp";
    }
    
    @Override
    public List<String> getAliases()
    {
        return Lists.newArrayList("settemp");
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
    	if (SyncedConfig.getBooleanValue(GameplayOption.ENABLE_TEMPERATURE))
    	{
    		setTemperature(sender, args);
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

        // Remove any existing potion effects for hypo/hyperthermia
        player.removePotionEffect(TANPotions.hypothermia);
        player.removePotionEffect(TANPotions.hyperthermia);

        // Reset the change timer to 0
        temperatureStats.setChangeTime(0);
        // Set to the new temperature
        temperatureStats.setTemperature(new Temperature(newTemp));

        sender.sendMessage(new TextComponentTranslation("commands.toughasnails.settemp.success", newTemp));   
    }
    
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "settemp");
        }
        
        return null;
    }
}