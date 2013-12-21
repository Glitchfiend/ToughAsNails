package tan.textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tan.api.utils.TANPlayerStatUtils;
import tan.api.utils.TemperatureUtils;
import tan.stats.TemperatureStat;

public class TextureThermometer extends TextureAtlasSprite
{
    public TextureThermometer()
    {
        super("toughasnails:thermometer");
    }

    @Override
    public void updateAnimation()
    {
        if (!this.framesTextureData.isEmpty())
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            EntityPlayer player = minecraft.thePlayer;

            if (player != null)
            {
                World world = player.worldObj;

                int x = MathHelper.floor_double(player.posX);
                int y = MathHelper.floor_double(player.posY);
                int z = MathHelper.floor_double(player.posZ);

                TemperatureStat temperatureStat = TANPlayerStatUtils.getPlayerStat(player, TemperatureStat.class);

                this.frameCounter = (int)(((TemperatureUtils.getAimedTemperature(TemperatureUtils.getEnvironmentTemperature(player.worldObj, x, y, z), world, player) / 20F) - 1.35F) * this.getFrameCount());
            }

            TextureUtil.uploadTextureSub((int[])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
        }
    }
}
