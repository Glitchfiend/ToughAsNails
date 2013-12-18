package tan.api.thirst;

import java.util.HashMap;

public class TANDrinkInfo
{
    public int thirstAmount;
    public float hydrationModifier;
    public float poisoningChance;
    
    public static HashMap<String, TANDrinkInfo> drinkInfoMap = new HashMap();
    
    public TANDrinkInfo(int thirstAmount, float hydrationModifier, float poisoningChance)
    {
        this.thirstAmount = thirstAmount;
        this.hydrationModifier = hydrationModifier;
        this.poisoningChance = poisoningChance;
    }
    
    public static TANDrinkInfo getDrinkInfo(String fluidName)
    {
        return drinkInfoMap.get(fluidName);
    }

    public static void addDrinkInfo(String fluidName, int thirstAmount, float hydrationModifier, float poisoningChance)
    {
        drinkInfoMap.put(fluidName, new TANDrinkInfo(thirstAmount, hydrationModifier, poisoningChance));
    }
}
