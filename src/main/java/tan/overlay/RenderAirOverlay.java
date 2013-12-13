package tan.overlay;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

public class RenderAirOverlay
{
    @ForgeSubscribe
    public void render(RenderGameOverlayEvent.Pre event)
    {
        if (event.type == ElementType.AIR)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -10.0F, 0.0F);
        }
    }
    
    @ForgeSubscribe
    public void render(RenderGameOverlayEvent.Post event)
    {
        if (event.type == ElementType.AIR)
        {
            GL11.glPopMatrix();
        }
    }
}
