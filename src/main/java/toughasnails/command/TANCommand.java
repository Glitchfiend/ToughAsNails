package toughasnails.command;

import java.util.List;

import toughasnails.api.TANPotions;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureInfo;
import toughasnails.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureStats;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class TANCommand extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "toughasnails";
    }
    
    @Override
    public List getCommandAliases()
    {
        return Lists.newArrayList("tan");
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.toughasnails.usage";
    }
    
    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
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
        TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");
        TemperatureDebugger debugger = temperatureStats.debugger;
        
        debugger.setGuiVisible(!debugger.isGuiVisible(), player);
    }
    
    private void setTemperature(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        TemperatureStats temperatureStats = (TemperatureStats)player.getExtendedProperties("temperature");
        int newTemp = parseInt(args[1], 0, TemperatureScale.getScaleTotal());
        TemperatureInfo playerTemp = temperatureStats.getTemperature();
        
        if (newTemp != temperatureStats.getTemperature().getScalePos())
        {
            //Remove any existing potion effects for hypo/hyperthermia
            player.removePotionEffect(TANPotions.hypothermia.id);
            player.removePotionEffect(TANPotions.hyperthermia.id);
            
            temperatureStats.setTemperature(newTemp);
            
            sender.addChatMessage(new ChatComponentTranslation("commands.toughasnails.settemp.success", newTemp));
        }
    }
    
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "settemp", "tempinfo");
        }
        
        return null;
    }
}
