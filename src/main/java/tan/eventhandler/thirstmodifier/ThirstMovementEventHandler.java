package tan.eventhandler.thirstmodifier;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.material.Material;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import tan.api.event.thirst.ThirstEvent;
import tan.stats.ThirstStat;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ThirstMovementEventHandler
{
    @ForgeSubscribe
    public void modifyThirst(ThirstEvent event)
    {
        EntityPlayer player = event.player;
        
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        
        float thirstExhaustion = event.thirstExhaustionLevel;
        
        if (player.ridingEntity == null)
        {
            if (player.onGround)
            {
                if (player.isSprinting())
                {
                    thirstExhaustion = ThirstStat.addExhaustion(thirstExhaustion, 0.0099999994F * 5);
                }
                else
                {
                    thirstExhaustion = ThirstStat.addExhaustion(thirstExhaustion, 0.001F * 5);
                }
            }
        }
        
        event.thirstExhaustionLevel = thirstExhaustion;
    }
}