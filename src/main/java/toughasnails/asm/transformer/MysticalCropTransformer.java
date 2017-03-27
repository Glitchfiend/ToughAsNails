package toughasnails.asm.transformer;

//Mystical Agriculture compatibility
public class MysticalCropTransformer extends AbstractCropTransformer
{
    private static final String[] VALID_HASHES_RESOURCE = new String[] { "5fa681a833e27d1b0a1f8d1d62583fae" };
    private static final String[] VALID_HASHES_INFERIUM_1 = new String[] { "56de0b086621ff38e7df680e290cef16" };
    private static final String[] VALID_HASHES_INFERIUM_2 = new String[] { "28d1acc3f789144a51980e60ded64331" };
    private static final String[] VALID_HASHES_INFERIUM_3 = new String[] { "6aa39049a594e7a398a480acc17a6fa3" };
    private static final String[] VALID_HASHES_INFERIUM_4 = new String[] { "0b322f265216c53c7df39c9ebf9af850" };
    private static final String[] VALID_HASHES_INFERIUM_5 = new String[] { "05cdc4dc01b3fb08e242c5fc59fbb13b" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
    	if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockMysticalCrop", VALID_HASHES_RESOURCE, WinterBehavior.DECAY);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier1InferiumCrop"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockTier1InferiumCrop", VALID_HASHES_INFERIUM_1, WinterBehavior.DECAY);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier2InferiumCrop"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockTier2InferiumCrop", VALID_HASHES_INFERIUM_2, WinterBehavior.DECAY);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier3InferiumCrop"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockTier3InferiumCrop", VALID_HASHES_INFERIUM_3, WinterBehavior.DECAY);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier4InferiumCrop"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockTier4InferiumCrop", VALID_HASHES_INFERIUM_4, WinterBehavior.DECAY);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier5InferiumCrop"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockTier5InferiumCrop", VALID_HASHES_INFERIUM_5, WinterBehavior.DECAY);
        }
        
        return basicClass;
    }
}