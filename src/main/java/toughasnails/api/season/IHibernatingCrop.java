/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

/** 
 * An interface which should be implemented by crops
 * which become inactive in the winter in the absence of
 * proper heating.
 * 
 * Crops using this interface should make sure they use
 * Forge's crop growth events and appropriately cancel
 * the crop growth when the event result is set to DENY.
 * (See net.minecraftforge.event.BlockEvent$CropGrowEvent or
 * alternatively net.minecraftforge.common.ForgeHooks.onCropsGrowPre)
 */
public interface IHibernatingCrop {
    // Crop will only hibernate in winter if this returns true
    boolean shouldHibernate();
}
