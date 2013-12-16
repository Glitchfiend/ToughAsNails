package tan.eventhandler.modifiers;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.material.Material;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import tan.api.event.thirst.ThirstEvent;
import tan.api.utils.TANPlayerStatUtils;
import tan.stats.ThirstStat;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ThirstMovementEventHandler
{
    @ForgeSubscribe
    public void modifyThirst(ThirstEvent event)
    {
        EntityPlayer player = event.player;
        
        ThirstStat thirstStat = event.thirstStat;
        
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        
        if (player.ridingEntity == null)
        {
            if (player.onGround)
            {
                if (player.isSprinting())
                {
                    thirstStat.addExhaustion(0.0099999994F * 5);
                }
                else
                {
                    thirstStat.addExhaustion(0.001F * 5);
                }
            }
        }
    }
}