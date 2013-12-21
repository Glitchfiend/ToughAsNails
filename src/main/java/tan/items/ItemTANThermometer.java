package tan.items;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import tan.ToughAsNails;
import tan.api.utils.TemperatureUtils;
import tan.stats.TemperatureStat;
import tan.textures.TextureThermometer;

public class ItemTANThermometer extends Item
{
    public ItemTANThermometer(int id)
    {
        super(id);
        this.maxStackSize = 1;
        this.setCreativeTab(ToughAsNails.tabToughAsNails);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        if (iconRegister instanceof TextureMap)
        {
            TextureAtlasSprite texture = new TextureThermometer();
            ((TextureMap)iconRegister).setTextureEntry("toughasnails:thermometer", texture);
            this.itemIcon = texture;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);
        
        float environmentTemperature = TemperatureUtils.getEnvironmentTemperature(world, x, y, z);

        if (!world.isRemote)
        {
            ChatMessageComponent environmentTempMessage = new ChatMessageComponent();

            environmentTempMessage.addKey("phrase.tan.tempThermometer");
            environmentTempMessage.addText(" " + TemperatureStat.getConvertedDisplayTemperature(MathHelper.floor_float(TemperatureUtils.getAimedTemperature(environmentTemperature, world, player))) + TemperatureStat.getTemperatureSymbol());
            
            player.sendChatToPlayer(environmentTempMessage);
        }

        return itemStack;
    }
}
