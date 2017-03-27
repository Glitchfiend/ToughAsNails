/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.asm.transformer;

public class BlockCropsTransformer extends AbstractCropTransformer
{
    private static final String[] VALID_HASHES = new String[] { "3d74307bb515539176e7a84967b10a28", "b835f0bbb24031fee6ad804d8c48d2dc" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (transformedName.equals("net.minecraft.block.BlockCrops"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockCrops", VALID_HASHES, WinterBehavior.DECAY);
        }
        
        return basicClass;
    }
}
