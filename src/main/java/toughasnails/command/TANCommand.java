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
import toughasnails.api.season.Season.SubSeason;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.handler.season.SeasonHandler;
import toughasnails.season.SeasonSavedData;
import toughasnails.season.SeasonTime;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureHandler;
import toughasnails.tileentity.TileEntityTemperatureSpread;

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
        else if ("setseason".equals(args[0]))
        {
            setSeason(sender, args);
        }
        else if ("coildebug".equals(args[0]))
        {
            showCoilDebug(sender, args);
        }
    }
    
    private void displayTemperatureInfo(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        TemperatureHandler temperatureStats = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
        TemperatureDebugger debugger = temperatureStats.debugger;
        
        debugger.setGuiVisible(!debugger.isGuiVisible(), player);
    }
    
    private void setTemperature(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        TemperatureHandler temperatureStats = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
        int newTemp = parseInt(args[1], 0, TemperatureScale.getScaleTotal());
        Temperature playerTemp = temperatureStats.getTemperature();

        //Remove any existing potion effects for hypo/hyperthermia
        player.removePotionEffect(TANPotions.hypothermia);
        player.removePotionEffect(TANPotions.hyperthermia);

        //Reset the change timer to 0
        temperatureStats.setChangeTime(0);
        //Set to the new temperature
        temperatureStats.setTemperature(new Temperature(newTemp));

        sender.addChatMessage(new TextComponentTranslation("commands.toughasnails.settemp.success", newTemp));
    }
    
    private void setSeason(ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        SubSeason newSeason = null;
        
        for (SubSeason season : SubSeason.values())
        {
            if (season.toString().toLowerCase().equals(args[1].toLowerCase()))
            {
                newSeason = season;
                break;
            }
        }
        
        if (newSeason != null)
        {
            SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(player.worldObj);
            seasonData.seasonCycleTicks = SeasonTime.DAY_TICKS * SeasonTime.SUB_SEASON_DURATION * newSeason.ordinal();
            seasonData.markDirty();
            SeasonHandler.sendSeasonUpdate(player.worldObj);
            sender.addChatMessage(new TextComponentTranslation("commands.toughasnails.setseason.success", args[1]));
        }
        else
        {
            sender.addChatMessage(new TextComponentTranslation("commands.toughasnails.setseason.fail", args[1]));
        }
    }
    
    private void showCoilDebug(ICommandSender sender, String[] args) throws CommandException
    {
        TileEntityTemperatureSpread.enableDebug = !TileEntityTemperatureSpread.enableDebug;
    }
    
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "settemp", "tempinfo");
        }
        
        return null;
    }
}
