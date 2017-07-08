/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class TANLoadingPlugin implements IFMLLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {
        		"toughasnails.asm.transformer.BlockCropsTransformer",
        		"toughasnails.asm.transformer.BlockStemTransformer",
        		"toughasnails.asm.transformer.PamCropTransformer",
        		"toughasnails.asm.transformer.MysticalCropTransformer",
        		"toughasnails.asm.transformer.EntityRendererTransformer",
        		"toughasnails.asm.transformer.WorldTransformer"
        		};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) 
    {
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
