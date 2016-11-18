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
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

public class WorldTransformer implements IClassTransformer
{
    private static final String[] CAN_SNOW_AT_NAMES = new String[] { "canSnowAt", "func_175708_f", "f" };
    private static final String[] CAN_BLOCK_FREEZE_NAMES = new String[] { "canBlockFreeze", "func_175670_e", "e" };
    private static final String[] IS_RAINING_AT_NAMES = new String[] { "isRainingAt", "func_175727_C", "B" };
    private static final String[] GET_BIOME_GEN_FOR_COORDS_NAMES = new String[] { "getBiome", "func_180494_b", "b" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (transformedName.equals("net.minecraft.world.World"))
        {
            return transformWorld(basicClass, !transformedName.equals(name));
        }
        
        return basicClass;
    }
    
    private byte[] transformWorld(byte[] bytes, boolean obfuscatedClass)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        List<String> successfulTransformations = Lists.newArrayList();
        
        //Iterate over the methods in the class
        for (MethodNode methodNode : classNode.methods)
        {
            if (ASMHelper.methodEquals(methodNode, CAN_SNOW_AT_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/util/math/BlockPos", "Z")))
            { 
                InsnList insnList = new InsnList();
                
                //Get the current season
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/api/season/SeasonHelper", "getSeasonData", ObfHelper.createMethodDescriptor(obfuscatedClass, "toughasnails/api/season/ISeasonData", "net/minecraft/world/World"), false));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "toughasnails/api/season/ISeasonData", "getSubSeason", "()Ltoughasnails/api/season/Season$SubSeason;", true));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "toughasnails/api/season/Season$SubSeason", "getSeason", "()Ltoughasnails/api/season/Season;", false));
                insnList.add(new VarInsnNode(Opcodes.ASTORE, 3));

                //Invoke our replacement method
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 2));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "canSnowAtInSeason", ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos", "Z", "toughasnails/api/season/Season"), false));
                insnList.add(new InsnNode(Opcodes.IRETURN));

                //Substitute existing instructions with our new ones
                methodNode.instructions.clear();
                methodNode.instructions.insert(insnList);
                
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
            else if (ASMHelper.methodEquals(methodNode, CAN_BLOCK_FREEZE_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/util/math/BlockPos", "Z")))
            { 
                InsnList insnList = new InsnList();
                
                //Get the current season
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/api/season/SeasonHelper", "getSeasonData", ObfHelper.createMethodDescriptor(obfuscatedClass, "toughasnails/api/season/ISeasonData", "net/minecraft/world/World"), false));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "toughasnails/api/season/ISeasonData", "getSubSeason", "()Ltoughasnails/api/season/Season$SubSeason;", true));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "toughasnails/api/season/Season$SubSeason", "getSeason", "()Ltoughasnails/api/season/Season;", false));
                insnList.add(new VarInsnNode(Opcodes.ASTORE, 3));

                //Invoke our replacement method
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ILOAD, 2));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "canBlockFreezeInSeason", ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos", "Z", "toughasnails/api/season/Season"), false));
                insnList.add(new InsnNode(Opcodes.IRETURN));

                //Substitute existing instructions with our new ones
                methodNode.instructions.clear();
                methodNode.instructions.insert(insnList);
                
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
            else if (ASMHelper.methodEquals(methodNode, IS_RAINING_AT_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/util/math/BlockPos")))
            { 
                InsnList insnList = new InsnList();
                
                //Get the current season
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/api/season/SeasonHelper", "getSeasonData", ObfHelper.createMethodDescriptor(obfuscatedClass, "toughasnails/api/season/ISeasonData", "net/minecraft/world/World"), false));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "toughasnails/api/season/ISeasonData", "getSubSeason", "()Ltoughasnails/api/season/Season$SubSeason;", true));
                insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "toughasnails/api/season/Season$SubSeason", "getSeason", "()Ltoughasnails/api/season/Season;", false));
                insnList.add(new VarInsnNode(Opcodes.ASTORE, 2));

                //Invoke our replacement method
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "isRainingAtInSeason", ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos", "toughasnails/api/season/Season"), false));
                insnList.add(new InsnNode(Opcodes.ICONST_0)); //Necessary for compatibility with RandomThingsCore
                insnList.add(new InsnNode(Opcodes.IRETURN));
                
                MethodInsnNode invokeMethodNode = ASMHelper.getUniqueMethodInsnNode(methodNode, Opcodes.INVOKEVIRTUAL, ObfHelper.unmapType(obfuscatedClass, "net/minecraft/world/World"), GET_BIOME_GEN_FOR_COORDS_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "net/minecraft/world/biome/Biome", "net/minecraft/util/math/BlockPos"));
                AbstractInsnNode insertionPoint = methodNode.instructions.get(methodNode.instructions.indexOf(invokeMethodNode) - 2);
                
                //Insert our new instructions before the insertion point
                methodNode.instructions.insertBefore(insertionPoint, insnList);
                
                ASMHelper.clearNextInstructions(methodNode, insertionPoint);
                
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
        }
        
        if (successfulTransformations.size() != 3) throw new RuntimeException("An error occurred transforming World. Applied transformations: " + successfulTransformations.toString());
        
        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        
        return bytes;
    }
}
