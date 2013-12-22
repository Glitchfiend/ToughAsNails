package tan.api.thirst;

import java.util.ArrayList;

public class TANDrinkContainer
{
    public int id;
    public int metadata;

    public static ArrayList<TANDrinkContainer> drinkContainers = new ArrayList<TANDrinkContainer>();

    public TANDrinkContainer(int id, int metadata)
    {
        this.id = id;
        this.metadata = metadata;
    }

    public static void addDrinkContainer(int itemID, int metadata)
    {
        drinkContainers.add(new TANDrinkContainer(itemID, metadata));
    }

    public static void addDrinkContainer(int itemID)
    {
        addDrinkContainer(itemID, 0);
    }

    public static boolean contains(int itemID, int metadata)
    {
        for (int i = 0; i < drinkContainers.size(); i++)
        {
            TANDrinkContainer drinkContainer = drinkContainers.get(i);

            if (drinkContainer.id == itemID && drinkContainer.metadata == metadata) return true;
        }

        return false;
    }
}
