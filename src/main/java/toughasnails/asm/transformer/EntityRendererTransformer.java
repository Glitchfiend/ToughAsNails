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
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

public class EntityRendererTransformer implements IClassTransformer
{
    private static final String[] VALID_HASHES = new String[] { "7039efd63c08f2d8fa9f000a4a194d5c", "48321722b6b3220fc8d2b5dd4a703476" };
    
    private static final String[] RENDER_RAIN_SNOW_NAMES = new String[] { "renderRainSnow", "func_78474_d", "c" };
    private static final String[] ADD_RAIN_PARTICLES_NAMES = new String[] { "addRainParticles", "func_78484_h", "p" };
    
    private static final String[] GET_FLOAT_TEMPERATURE_NAMES = new String[] { "getFloatTemperature", "func_180626_a", "a" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer"))
        {
            return transformEntityRenderer(basicClass, !transformedName.equals(name));
        }
        
        return basicClass;
    }
    
    private byte[] transformEntityRenderer(byte[] bytes, boolean obfuscatedClass)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        //Check this class is unmodified
        ASMHelper.verifyClassHash("EntityRenderer", bytes, VALID_HASHES);
        
        List<String> successfulTransformations = Lists.newArrayList();
        
        //Iterate over the methods in the class
        for (MethodNode methodNode : classNode.methods)
        {
            if (ASMHelper.methodEquals(methodNode, RENDER_RAIN_SNOW_NAMES, "(F)V"))
            { 
                MethodInsnNode targetMethodInsnNode = ASMHelper.getUniqueMethodInsnNode(methodNode, Opcodes.INVOKEVIRTUAL, ObfHelper.unmapType(obfuscatedClass, "net/minecraft/world/biome/Biome"), GET_FLOAT_TEMPERATURE_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "F", "net/minecraft/util/math/BlockPos"));
                
                //Redirect the call to our own version of getFloatTemperature
                targetMethodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                targetMethodInsnNode.owner = "toughasnails/season/SeasonASMHelper";
                targetMethodInsnNode.name = "getFloatTemperature";
                targetMethodInsnNode.desc = ObfHelper.createMethodDescriptor(obfuscatedClass, "F", "net/minecraft/world/biome/Biome", "net/minecraft/util/math/BlockPos");
                
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
            else if (ASMHelper.methodEquals(methodNode, ADD_RAIN_PARTICLES_NAMES, "()V"))
            { 
                MethodInsnNode targetMethodInsnNode = ASMHelper.getUniqueMethodInsnNode(methodNode, Opcodes.INVOKEVIRTUAL, ObfHelper.unmapType(obfuscatedClass, "net/minecraft/world/biome/Biome"), GET_FLOAT_TEMPERATURE_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "F", "net/minecraft/util/math/BlockPos"));
                
                //Redirect the call to our own version of getFloatTemperature
                targetMethodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                targetMethodInsnNode.owner = "toughasnails/season/SeasonASMHelper";
                targetMethodInsnNode.name = "getFloatTemperature";
                targetMethodInsnNode.desc = ObfHelper.createMethodDescriptor(obfuscatedClass, "F", "net/minecraft/world/biome/Biome", "net/minecraft/util/math/BlockPos");
                
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
        }
        
        if (successfulTransformations.size() != 2) throw new RuntimeException("An error occurred transforming EntityRenderer. Applied transformations: " + successfulTransformations.toString());
        
        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        
        return bytes;
    }
}
