/*******************************************************************************
 * Copyright 2014-2017, the Biomes O' Plenty Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package glitchcore.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryHelper
{
    public static ItemStack getSlot(EntityPlayer player, InventorySlotType type, int index)
    {
        InventoryPlayer inventory = player.inventory;

        switch (type)
        {
            case INVENTORY:
                return inventory.mainInventory.get(index);

            case ARMOR:
                return inventory.armorInventory.get(index);

            case OFF_HAND:
                return inventory.offHandInventory.get(index);
        }

        return null;
    }
}
