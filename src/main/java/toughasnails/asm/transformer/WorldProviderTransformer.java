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
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

public class WorldProviderTransformer implements IClassTransformer
{
    private static final String[] CALCULATE_CELESTIAL_ANGLE_NAMES = new String[] { "calculateCelestialAngle", "func_76563_a", "a" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (transformedName.equals("net.minecraft.world.WorldProvider"))
        {
            return transformWorldProvider(basicClass, !transformedName.equals(name));
        }
        
        return basicClass;
    }
    
    private byte[] transformWorldProvider(byte[] bytes, boolean obfuscatedClass)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        List<String> successfulTransformations = Lists.newArrayList();
        
        //Iterate over the methods in the class
        for (MethodNode methodNode : classNode.methods)
        {
            if (ASMHelper.methodEquals(methodNode, CALCULATE_CELESTIAL_ANGLE_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "F", "J", "F")))
            { 
                InsnList insnList = new InsnList();
                
                // Get the new celestial angle
                insnList.add(new VarInsnNode(Opcodes.LLOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.FLOAD, 3));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "calculateCelestialAngle", ObfHelper.createMethodDescriptor(obfuscatedClass, "F", "J", "F"), false));
                insnList.add(new InsnNode(Opcodes.FRETURN));

                //Substitute existing instructions with our new ones
                methodNode.instructions.clear();
                methodNode.instructions.insert(insnList);
                
                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
        }
        
        if (successfulTransformations.size() != 1) throw new RuntimeException("An error occurred transforming WorldProvider. Applied transformations: " + successfulTransformations.toString());
        
        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        
        return bytes;
    }
}
