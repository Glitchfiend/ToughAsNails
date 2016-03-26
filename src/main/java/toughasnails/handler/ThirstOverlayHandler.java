package toughasnails.handler;

import static toughasnails.util.RenderUtils.drawTexturedModalRect;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import toughasnails.api.TANCapabilities;
import toughasnails.thirst.ThirstHandler;

public class ThirstOverlayHandler
{
    public static final ResourceLocation OVERLAY = new ResourceLocation("toughasnails:textures/gui/overlay.png");
    
    private final Random random = new Random();
    private final Minecraft minecraft = Minecraft.getMinecraft();
    
    private int updateCounter;
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == Phase.END)
        {
            updateCounter++;
        }
    }
    
    @SubscribeEvent
    public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == ElementType.AIR)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -10.0F, 0.0F);
        }
    }
    
    @SubscribeEvent
    public void onPostRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        ScaledResolution resolution = event.getResolution();
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        
        ThirstHandler thirstStats = (ThirstHandler)player.getCapability(TANCapabilities.THIRST, null);
        int thirstLevel = thirstStats.getThirst();
        float thirstHydrationLevel = thirstStats.getHydration();
        
        if (event.getType() == ElementType.AIR)
        {
            GlStateManager.popMatrix();
        }
        else if (event.getType() == ElementType.EXPERIENCE)
        {
            minecraft.getTextureManager().bindTexture(OVERLAY);

            if (minecraft.playerController.gameIsSurvivalOrAdventure())
            {
                drawThirst(width, height, thirstLevel, thirstHydrationLevel);
            }
        }
    }
    
    private void drawThirst(int width, int height, int thirstLevel, float thirstHydrationLevel)
    {
        int left = width / 2 + 91;
        int top = height - 49;
        
        for (int i = 0; i < 10; i++)
        {
            int dropletHalf = i * 2 + 1;
            int iconIndex = 0;
            int startX = left - i * 8 - 9;
            int startY = top;
            
            if (thirstHydrationLevel <= 0.0F && updateCounter % (thirstLevel * 3 + 1) == 0)
            {
                startY = top + (random.nextInt(3) - 1);
            }
            
            drawTexturedModalRect(startX, startY, iconIndex * 8, 16, 9, 9);
            
            if (thirstLevel > dropletHalf)
            {
                drawTexturedModalRect(startX, startY, (iconIndex + 4) * 9, 16, 9, 9);
            }
            if (thirstLevel == dropletHalf)
            {
                drawTexturedModalRect(startX, startY, (iconIndex + 5) * 9, 16, 9, 9);
            }
        }
    }
}
