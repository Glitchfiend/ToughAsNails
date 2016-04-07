package toughasnails.init;

import static toughasnails.api.TANPotions.hyperthermia;
import static toughasnails.api.TANPotions.hypothermia;
import static toughasnails.api.TANPotions.thirst;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.core.ToughAsNails;
import toughasnails.potion.PotionHyperthermia;
import toughasnails.potion.PotionHypothermia;
import toughasnails.potion.PotionThirst;

public class ModPotions
{
    public static void init()
    {
        hypothermia = registerPotion("hypothermia", new PotionHypothermia(24).setPotionName("potion.hypothermia").func_188413_j());
        hyperthermia = registerPotion("hyperthermia", new PotionHyperthermia(25).setPotionName("potion.hyperthermia").func_188413_j());
        thirst = registerPotion("thirst", new PotionThirst(26).setPotionName("potion.thirst").func_188413_j());
    }
    
    public static Potion registerPotion(String name, Potion potion)
    {
        GameRegistry.register(potion, new ResourceLocation(ToughAsNails.MOD_ID, name));
        return potion;
    }
}
