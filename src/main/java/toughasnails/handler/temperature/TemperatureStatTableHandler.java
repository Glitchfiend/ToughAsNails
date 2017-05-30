package toughasnails.handler.temperature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
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
import org.apache.commons.lang3.tuple.Pair;
import toughasnails.api.TANCapabilities;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale;
import toughasnails.temperature.TemperatureDebugger;
import toughasnails.temperature.TemperatureDebugger.Modifier;
import toughasnails.temperature.TemperatureHandler;

public class TemperatureStatTableHandler
{
    private static ArrayList<StatTable> statTables = Lists.newArrayList();
    private static final StatTable TEMPERATURE_TABLE = new StatTable("Target");
    private static final StatTable RATE_TABLE = new StatTable("Rate");

    static
    {
        statTables.add(TEMPERATURE_TABLE);
        statTables.add(RATE_TABLE);
    }

    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        TemperatureHandler temperatureHandler = (TemperatureHandler)player.getCapability(TANCapabilities.TEMPERATURE, null);
        TemperatureDebugger debugger = temperatureHandler.debugger;
        int currentTemp = temperatureHandler.getTemperature().getRawValue();

        if (event.getType() == ElementType.ALL && debugger.isGuiVisible())
        {
            String targetProgress = "Target " + TextFormatting.RED + currentTemp + "/" + debugger.targetTemperature + getCappedText(debugger.targetTemperature);
            String rateProgress = "Rate " + TextFormatting.RED + debugger.temperatureTimer + "/" + debugger.changeTicks;

            TEMPERATURE_TABLE.reset(targetProgress);
            RATE_TABLE.reset(rateProgress);

            // translate old modifiers map to new table setup
            // TODO: Replace this once the debugger has been updated properly
            for (Entry<Modifier, Integer> entry : debugger.modifiers[0].entrySet())
            {
                TEMPERATURE_TABLE.addRow(entry.getKey().name, entry.getValue());
            }

            int gradient = TemperatureScale.getRateForTemperatures(currentTemp, debugger.targetTemperature) - TemperatureScale.getAdjustedBaseRate(currentTemp);
            if (gradient != 0) RATE_TABLE.addRow("Gradient", gradient);

            int extremityModifier = TemperatureScale.getAdjustedBaseRate(currentTemp) - TemperatureScale.BASE_TEMPERATURE_CHANGE_TICKS;
            if (extremityModifier != 0) RATE_TABLE.addRow("Extremity", extremityModifier);

            drawTables(event.getResolution());
        }
    }

    private void drawTables(ScaledResolution resolution)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int screenWidth = resolution.getScaledWidth();
        int screenHeight = resolution.getScaledHeight();

        int totalTableHeight = 2 * (statTables.size() - 1); // account for the gaps between tables
        for (StatTable statTable : statTables)
        {
            totalTableHeight += statTable.getHeight();
        }

        int startY = screenHeight / 2 - totalTableHeight / 2;
        int accumulatedHeight = 0;

        for (int i = 0; i < statTables.size(); i++)
        {
            StatTable currentTable = statTables.get(i);
            currentTable.drawTable(1, startY + accumulatedHeight);
            accumulatedHeight += currentTable.getHeight() + 2; // draw the next table below this one with a slight gap
        }
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

    @SideOnly(Side.CLIENT)
    private static class StatTable
    {
        private ArrayList<Pair<String, Integer>> rows = Lists.newArrayList();
        private String title;

        public StatTable(String title)
        {
            this.title = title;
        }

        public void addRow(String fieldName, int value)
        {
            this.rows.add(Pair.of(fieldName, value));
        }

        public void reset(String title)
        {
            this.rows.clear();
            this.title = title;
        }

        public void drawTable(int x, int y)
        {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            int lineWidth = getRowWidth(title);
            int textStart = x + 2;
            int textEnd = textStart + lineWidth;

            for (int i = 0; i < this.rows.size() + 1; i++)
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
                    Pair<String, Integer> row = this.rows.get(i - 1);
                    String string = row.getLeft();
                    int value = row.getRight();
                    String formattedValue = getFormattedInt(value);

                    Gui.drawRect(x, rowBottomY, textEnd, rowTopY, 1342177280);
                    fontRenderer.drawString(string, textStart, rowTopY, 553648127);
                    fontRenderer.drawString(formattedValue, textEnd - fontRenderer.getStringWidth(formattedValue), rowTopY, 553648127);
                }
            }
        }

        public int getRowWidth(String title)
        {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            int lineWidth = 0;

            for (Pair<String, Integer> entry : this.rows)
            {
                int i = entry.getValue();
                String string = entry.getKey() + ": " + getFormattedInt(i);

                lineWidth = Math.max(fontRenderer.getStringWidth(string), lineWidth);
            }

            lineWidth = Math.max(fontRenderer.getStringWidth(title), lineWidth);

            return lineWidth;
        }

        public int getHeight()
        {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            return (this.rows.size() + 1) * fontRenderer.FONT_HEIGHT + 1;
        }
    }
}
