package toughasnails.init;

import static toughasnails.api.TANPotions.*;

import toughasnails.potion.PotionHyperthermia;
import toughasnails.potion.PotionHypothermia;
import toughasnails.potion.PotionThirst;

public class ModPotions
{
    public static void init()
    {
        hypothermia = new PotionHypothermia(24).setPotionName("potion.hypothermia").func_188413_j();
        hyperthermia = new PotionHyperthermia(25).setPotionName("potion.hyperthermia").func_188413_j();
        thirst = new PotionThirst(26).setPotionName("potion.thirst").func_188413_j();
    }
}
