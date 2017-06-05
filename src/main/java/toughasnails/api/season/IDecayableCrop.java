/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

/** 
 * A marker interface which should be implemented by crops which decay
 * in the winter in the absence of proper heating.
 * 
 * Please note that due to how Java bytecode works, you must explicitly
 * implement this interface if your class overrides updateTick.
 */
public interface IDecayableCrop {
    // Crop will only decay in winter if this returns true
    boolean shouldDecay();
}
