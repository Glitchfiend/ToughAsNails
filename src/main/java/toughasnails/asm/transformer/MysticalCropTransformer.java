package toughasnails.asm.transformer;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

//Mystical Agriculture compatibility
public class MysticalCropTransformer implements IClassTransformer
{
    private static final String[] VALID_HASHES_RESOURCE = new String[] { "5fa681a833e27d1b0a1f8d1d62583fae" };
    private static final String[] VALID_HASHES_INFERIUM_1 = new String[] { "56de0b086621ff38e7df680e290cef16" };
    private static final String[] VALID_HASHES_INFERIUM_2 = new String[] { "28d1acc3f789144a51980e60ded64331" };
    private static final String[] VALID_HASHES_INFERIUM_3 = new String[] { "6aa39049a594e7a398a480acc17a6fa3" };
    private static final String[] VALID_HASHES_INFERIUM_4 = new String[] { "0b322f265216c53c7df39c9ebf9af850" };
    private static final String[] VALID_HASHES_INFERIUM_5 = new String[] { "05cdc4dc01b3fb08e242c5fc59fbb13b" };
    
    private static final String[] UPDATE_TICK_NAMES = new String[] { "updateTick", "func_180650_b", "b" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
    	//TODO: Add config option with false by default.
    	boolean decayEnabledMysticalAgriculture = true;
    	if (!decayEnabledMysticalAgriculture)
    	{
    		return basicClass;
    	}
    	
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop"))
        {
            return transformMysticalCrop(basicClass, !transformedName.equals(name), "BlockMysticalCrop", VALID_HASHES_RESOURCE);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier1InferiumCrop"))
        {
            return transformMysticalCrop(basicClass, !transformedName.equals(name), "BlockTier1InferiumCrop", VALID_HASHES_INFERIUM_1);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier2InferiumCrop"))
        {
            return transformMysticalCrop(basicClass, !transformedName.equals(name), "BlockTier2InferiumCrop", VALID_HASHES_INFERIUM_2);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier3InferiumCrop"))
        {
            return transformMysticalCrop(basicClass, !transformedName.equals(name), "BlockTier3InferiumCrop", VALID_HASHES_INFERIUM_3);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier4InferiumCrop"))
        {
            return transformMysticalCrop(basicClass, !transformedName.equals(name), "BlockTier4InferiumCrop", VALID_HASHES_INFERIUM_4);
        }
        if (transformedName.equals("com.blakebr0.mysticalagriculture.blocks.crop.BlockTier5InferiumCrop"))
        {
            return transformMysticalCrop(basicClass, !transformedName.equals(name), "BlockTier5InferiumCrop", VALID_HASHES_INFERIUM_5);
        }
        
        return basicClass;
    }
    
    private byte[] transformMysticalCrop(byte[] bytes, boolean obfuscatedClass, String shortClassName, String[] validHashes)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        //Check this class is unmodified
        ASMHelper.verifyClassHash(shortClassName, bytes, validHashes);
        
        //All Mystical crops should decay
        classNode.interfaces.add("toughasnails/api/season/IDecayableCrop");
        
        List<String> successfulTransformations = Lists.newArrayList();
        
        //Iterate over the methods in the class
        for (MethodNode methodNode : classNode.methods)
        {
            if (ASMHelper.methodEquals(methodNode, UPDATE_TICK_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos", "net/minecraft/block/state/IBlockState", "java/util/Random")))
            { 
                InsnList insnList = new InsnList();
                
                //Get the current season
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "onUpdateTick", ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/block/BlockCrops", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos"), false));
            
                //Insert our new instructions before returning
                methodNode.instructions.insertBefore(methodNode.instructions.get(methodNode.instructions.indexOf(methodNode.instructions.getLast()) - 1), insnList);
            
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
        }
        
        if (successfulTransformations.size() != 1) throw new RuntimeException("An error occurred transforming " + shortClassName + ". Applied transformations: " + successfulTransformations.toString());

        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        
        return bytes;
    }
}