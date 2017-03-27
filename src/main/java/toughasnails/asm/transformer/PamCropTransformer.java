/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.asm.transformer;

import toughasnails.asm.transformer.AbstractCropTransformer.WinterBehavior;

// Pam's Harvestcraft compatibility
public class PamCropTransformer extends AbstractCropTransformer
{
	private static final String[] VALID_HASHES_CROP = new String[] { "03263774b5cda6bfeccc1622fd344710" };
	private static final String[] VALID_HASHES_FRUIT = new String[] { "da4a453d0bcd36b8db130e372865cb24" };
	private static final String[] VALID_HASHES_FRUIT_LOG = new String[] { "ce0043f49abbe83982be20cbf4989bb3" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
		if (transformedName.equals("com.pam.harvestcraft.blocks.growables.BlockPamCrop"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockPamCrop", VALID_HASHES_CROP, WinterBehavior.DECAY);
        }
        if (transformedName.equals("com.pam.harvestcraft.blocks.growables.BlockPamFruit"))
        {
        	return transformCrop(basicClass, !transformedName.equals(name), "BlockPamFruit", VALID_HASHES_FRUIT, WinterBehavior.HIBERNATE);
        }
        if (transformedName.equals("com.pam.harvestcraft.blocks.growables.BlockPamFruitLog"))
        {
        	return transformCrop(basicClass, !transformedName.equals(name), "BlockPamFruitLog", VALID_HASHES_FRUIT_LOG, WinterBehavior.HIBERNATE);
        }
        
        return basicClass;
    }
}
