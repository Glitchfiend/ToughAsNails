package tan.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tan.ToughAsNails;
import tan.api.utils.TemperatureUtils;
import tan.stats.TemperatureStat;

public class ItemTANThermometer extends Item
{
    public ItemTANThermometer(int id)
    {
        super(id);
        this.maxStackSize = 1;
        this.setCreativeTab(ToughAsNails.tabToughAsNails);
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("toughasnails:thermometer");
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
