/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.asm.transformer;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

// Pam's Harvestcraft compatibility
public class PamCropTransformer implements IClassTransformer
{
	private static final String[] VALID_HASHES_CROP = new String[] { "03263774b5cda6bfeccc1622fd344710" };
	private static final String[] VALID_HASHES_FRUIT = new String[] { "da4a453d0bcd36b8db130e372865cb24" };
	private static final String[] VALID_HASHES_FRUIT_LOG = new String[] { "ce0043f49abbe83982be20cbf4989bb3" };
    
    private static final String[] UPDATE_TICK_NAMES = new String[] { "updateTick", "func_180650_b", "b" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
		if (transformedName.equals("com.pam.harvestcraft.blocks.growables.BlockPamCrop"))
        {
            return transformPamCrop(basicClass, !transformedName.equals(name), "BlockPamCrop", VALID_HASHES_CROP, true);
        }
        if (transformedName.equals("com.pam.harvestcraft.blocks.growables.BlockPamFruit"))
        {
        	return transformPamCrop(basicClass, !transformedName.equals(name), "BlockPamFruit", VALID_HASHES_FRUIT, false);
        }
        if (transformedName.equals("com.pam.harvestcraft.blocks.growables.BlockPamFruitLog"))
        {
        	return transformPamCrop(basicClass, !transformedName.equals(name), "BlockPamFruitLog", VALID_HASHES_FRUIT_LOG, false);
        }
        
        return basicClass;
    }
    
    private byte[] transformPamCrop(byte[] bytes, boolean obfuscatedClass, String shortClassName, String[] validHashes, boolean shouldDecay)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        //Check this class is unmodified
        ASMHelper.verifyClassHash(shortClassName, bytes, validHashes);
        
        if (shouldDecay)
        {
	        //Normal Pam crops should decay just like vanilla crops
	        classNode.interfaces.add("toughasnails/api/season/IDecayableCrop");
        }
        else
        {
	        //Pam fruits and logs should simply not grow, to increase realism and avoid breaking untouched worldgen
	        classNode.interfaces.add("toughasnails/api/season/IHibernatingCrop");
        }
        
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
                if (shouldDecay)
                {
                	insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "onUpdateTick", ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/block/BlockCrops", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos"), false));
                	//Insert our new instructions before returning
                    methodNode.instructions.insertBefore(methodNode.instructions.get(methodNode.instructions.indexOf(methodNode.instructions.getLast()) - 1), insnList);
                }
                else
                {
                	insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "shouldHibernate", ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/block/Block", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos"), false));
                	//If above statement is true, don't let the function do anything else (i.e. stop the crop from ticking into a ripe block state)
                	LabelNode resumeUpdateTick = new LabelNode();
                	insnList.add(new JumpInsnNode(Opcodes.IFEQ, resumeUpdateTick));
                	insnList.add(new InsnNode(Opcodes.RETURN));
                	//TODO: either uncomment or remove the line below after running Minecraft, depending on if we crash
                	//insnList.add(new FrameNode(Opcodes.F_SAME, 5, new Object[]{}, 0, new Object[]{}));
                	insnList.add(resumeUpdateTick);
                	//Insert our new instructions before anything else happens
                	methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), insnList);
                }
                
            
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
        }
        
        if (successfulTransformations.size() != 1) throw new RuntimeException("An error occurred transforming " + shortClassName + ". Applied transformations: " + successfulTransformations.toString());

        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        
        return bytes;
    }
}
