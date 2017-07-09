package toughasnails.init;

import static toughasnails.api.TANPotions.cold_resistance;
import static toughasnails.api.TANPotions.cold_resistance_type;
import static toughasnails.api.TANPotions.long_cold_resistance_type;
import static toughasnails.api.TANPotions.heat_resistance;
import static toughasnails.api.TANPotions.heat_resistance_type;
import static toughasnails.api.TANPotions.long_heat_resistance_type;
import static toughasnails.api.TANPotions.hyperthermia;
import static toughasnails.api.TANPotions.hypothermia;
import static toughasnails.api.TANPotions.thirst;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import toughasnails.api.TANPotions;
import toughasnails.core.ToughAsNails;
import toughasnails.potion.PotionColdResistance;
import toughasnails.potion.PotionHeatResistance;
import toughasnails.potion.PotionHyperthermia;
import toughasnails.potion.PotionHypothermia;
import toughasnails.potion.PotionThirst;

public class ModPotions
{
    public static void init()
    {
        hypothermia = registerPotion("hypothermia", new PotionHypothermia(24).setPotionName("potion.hypothermia"));
        hyperthermia = registerPotion("hyperthermia", new PotionHyperthermia(25).setPotionName("potion.hyperthermia"));
        thirst = registerPotion("thirst", new PotionThirst(26).setPotionName("potion.thirst"));
        cold_resistance = registerPotion("cold_resistance", new PotionColdResistance(27).setPotionName("potion.cold_resistance").setBeneficial());
        heat_resistance = registerPotion("heat_resistance", new PotionHeatResistance(28).setPotionName("potion.heat_resistance").setBeneficial());
        
        cold_resistance_type = registerPotionType("cold_resistance_type", new PotionType(new PotionEffect[] {new PotionEffect(TANPotions.cold_resistance, 1200)}));
        long_cold_resistance_type = registerPotionType("long_cold_resistance_type", new PotionType(new PotionEffect[] {new PotionEffect(TANPotions.cold_resistance, 2400)}));
        heat_resistance_type = registerPotionType("heat_resistance_type", new PotionType(new PotionEffect[] {new PotionEffect(TANPotions.heat_resistance, 1200)}));
        long_heat_resistance_type = registerPotionType("long_heat_resistance_type", new PotionType(new PotionEffect[] {new PotionEffect(TANPotions.heat_resistance, 2400)}));
    }
    
    public static Potion registerPotion(String name, Potion potion)
    {
        GameRegistry.register(potion, new ResourceLocation(ToughAsNails.MOD_ID, name));
        return potion;
    }
    
    public static PotionType registerPotionType(String name, PotionType potionType)
    {
        GameRegistry.register(potionType, new ResourceLocation(ToughAsNails.MOD_ID, name));
        return potionType;
    }
}
