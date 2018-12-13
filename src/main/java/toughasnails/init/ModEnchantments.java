package toughasnails.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toughasnails.core.ToughAsNails;
import toughasnails.enchantment.EnchantmentCooling;
import toughasnails.enchantment.EnchantmentWarming;


@Mod.EventBusSubscriber(modid = ToughAsNails.MOD_ID)
public class ModEnchantments {
    public final static Enchantment WARMING = new EnchantmentWarming();
    public final static Enchantment COOLING = new EnchantmentCooling();

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().registerAll(WARMING, COOLING);
    }
}
