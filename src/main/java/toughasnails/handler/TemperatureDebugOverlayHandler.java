package toughasnails.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toughasnails.api.TANCapabilities;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureHandler;

public class TemperatureDebugOverlayHandler
{
    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        ScaledResolution resolution = event.getResolution();
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        
        TemperatureHandler temperatureStats = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
        TemperatureDebugger debugger = temperatureStats.debugger;
        
        if (event.getType() == ElementType.ALL && debugger.isGuiVisible())
        {
            drawModifierTable(width, height, temperatureStats.getTemperature(), debugger);
        }
    }
    
    private void drawModifierTable(int width, int height, Temperature temperature, TemperatureDebugger debugger)
    {
        Map<Modifier, Integer> rateModifiers = debugger.modifiers[0];
        Map<Modifier, Integer> targetModifiers = debugger.modifiers[1];
        
        if (targetModifiers != null && rateModifiers != null)
        {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

            int targetTableHeight = getTableHeight(targetModifiers);
            int totalTableHeight = targetTableHeight + getTableHeight(rateModifiers) + 2;
            int startY = height / 2 - totalTableHeight / 2;
            
            String targetProgress = "" + TextFormatting.RED + temperature.getRawValue() + "/" + debugger.targetTemperature + getCappedText(debugger.targetTemperature);
            String rateProgress = "" + TextFormatting.RED + debugger.temperatureTimer + "/" + debugger.changeTicks;
            
            drawTable("Target " + targetProgress, 1, startY, targetModifiers);
            drawTable("Rate " + rateProgress, 1, startY + targetTableHeight + 2, rateModifiers);
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static void drawTable(String title, int x, int y, Map<Modifier, Integer> contents)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        
        int lineWidth = getLineWidth(title, contents);
        int textStart = x + 2;
        int textEnd = textStart + lineWidth;
        
        Iterator contentsIterator = contents.entrySet().iterator();
        
        for (int i = 0; i < contents.size() + 1; i++)
        {
            int rowTopY = y + i * fontRenderer.FONT_HEIGHT + 1;
            int rowBottomY = rowTopY + fontRenderer.FONT_HEIGHT;
            
            if (i == 0)
            {
                Gui.drawRect(x, rowBottomY - 1, textEnd, y, 1610612736);
                Gui.drawRect(x, rowBottomY, textEnd, rowBottomY - 1, 1342177280);
                fontRenderer.drawString(title, textStart + lineWidth / 2 - fontRenderer.getStringWidth(title) / 2, y + 1, 553648127);
            }
            else
            {
                Entry<Modifier, Integer> entry = (Entry<Modifier, Integer>)contentsIterator.next();
                String string = entry.getKey().name;
                int value = entry.getValue();
                String formattedValue = getFormattedInt(value);
                
                Gui.drawRect(x, rowBottomY, textEnd, rowTopY, 1342177280);
                fontRenderer.drawString(string, textStart, rowTopY, 553648127);
                fontRenderer.drawString(formattedValue, textEnd - fontRenderer.getStringWidth(formattedValue), rowTopY, 553648127);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static int getTableHeight(Map contents)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        
        return (contents.size() + 1) * fontRenderer.FONT_HEIGHT + 1;
    }
    
    @SideOnly(Side.CLIENT)
    private static int getLineWidth(String title, Map<Modifier, Integer> elements)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int lineWidth = 0;
        
        for (Entry<Modifier, Integer> entry : elements.entrySet())
        {
            int i = entry.getValue();
            String string = entry.getKey().name + ": " + getFormattedInt(i);
            
            lineWidth = Math.max(fontRenderer.getStringWidth(string), lineWidth);
        }
        
        lineWidth = Math.max(fontRenderer.getStringWidth(title), lineWidth);

        return lineWidth;
    }
    
    private static String getCappedText(int targetTemperature)
    {
        return TextFormatting.BLUE + " " + (targetTemperature < 0 ? "(0)" : targetTemperature > TemperatureScale.getScaleTotal() ? "(" + TemperatureScale.getScaleTotal() + ")" : "");
    }
    
    private static String getFormattedInt(int i)
    {
        TextFormatting format = i > 0 ? TextFormatting.RED : i < 0 ? TextFormatting.BLUE : TextFormatting.RESET;
        
        return "" + format + getNumberSign(i) + i;
    }
    
    private static char getNumberSign(int i)
    {
        return i > 0 ? '+' : ' ';
    }
}
