package tan.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import tan.potions.PotionEventHandler;
import tan.potions.PotionWaterPoisoning;
import cpw.mods.fml.common.FMLCommonHandler;

public class TANPotions
{
    public static Potion waterPoisoning;
    
    public static int potionOffset;
    private static final int MAXNEWPOTIONS = 8;

    public static void init()
    {
        extendPotionsArray();
        intializePotions();

        MinecraftForge.EVENT_BUS.register(new PotionEventHandler());
    }

    private static void intializePotions()
    {
        waterPoisoning = new PotionWaterPoisoning(potionOffset + 0, true, 16767262).setPotionName("potion.waterPoisoning");
    }

    private static void extendPotionsArray()
    {
        FMLCommonHandler.instance().getFMLLogger().log(Level.INFO, "[ToughAsNails] Extending Potions Array.");
        potionOffset = Potion.potionTypes.length;

        Potion[] potionTypes = new Potion[potionOffset + MAXNEWPOTIONS];
        System.arraycopy(Potion.potionTypes, 0, potionTypes, 0, potionOffset);

        Field field = null;
        Field[] fields = Potion.class.getDeclaredFields();

        for (Field f : fields)
            if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
            {
                field = f;
                break;
            }

        try
        {
            field.setAccessible(true);

            Field modfield = Field.class.getDeclaredField("modifiers");
            modfield.setAccessible(true);
            modfield.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.set(null, potionTypes);
        }
        catch (Exception e)
        {
            System.err.println("[ToughAsNails] Severe error, please report this to the mod author:");
            System.err.println(e);
        }
    }
}
