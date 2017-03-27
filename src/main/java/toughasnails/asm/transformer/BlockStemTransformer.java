package toughasnails.asm.transformer;

//Melons and pumpkins
public class BlockStemTransformer extends AbstractCropTransformer
{
    private static final String[] VALID_HASHES = new String[] { "6a28b8cb3a448cb0b9fa7f5c5d7df8d9" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
    	if (transformedName.equals("net.minecraft.block.BlockStem"))
        {
            return transformCrop(basicClass, !transformedName.equals(name), "BlockStem", VALID_HASHES, WinterBehavior.DECAY);
        }
    	
        return basicClass;
    }
}