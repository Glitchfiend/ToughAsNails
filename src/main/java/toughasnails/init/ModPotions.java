package toughasnails.init;

import static toughasnails.api.TANPotions.hyperthermia;
import static toughasnails.api.TANPotions.hypothermia;

import toughasnails.potion.PotionHyperthermia;
import toughasnails.potion.PotionHypothermia;

public class ModPotions
{
    public static void init()
    {
        hypothermia = new PotionHypothermia(24).setPotionName("potion.hypothermia");
        hyperthermia = new PotionHyperthermia(25).setPotionName("potion.hyperthermia");
    }
}
