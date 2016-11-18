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
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

public class BlockCropsTransformer implements IClassTransformer
{
    private static final String[] UPDATE_TICK_NAMES = new String[] { "updateTick", "func_180650_b", "b" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (transformedName.equals("net.minecraft.block.BlockCrops"))
        {
            return transformBlockCrops(basicClass, !transformedName.equals(name));
        }
        
        return basicClass;
    }
    
    private byte[] transformBlockCrops(byte[] bytes, boolean obfuscatedClass)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        //All Vanilla crops should decay
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
        
        if (successfulTransformations.size() != 1) throw new RuntimeException("An error occurred transforming BlockCrops. Applied transformations: " + successfulTransformations.toString());

        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        
        return bytes;
    }
}
