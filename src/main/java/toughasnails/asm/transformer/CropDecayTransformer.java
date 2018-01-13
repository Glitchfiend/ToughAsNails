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
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

public class CropDecayTransformer implements IClassTransformer
{
    private static final String[] RANDOM_TICK_NAMES = new String[] { "randomTick", "func_180645_a", "a" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (transformedName.equals("net.minecraft.block.BlockCrops") || transformedName.equals("net.minecraft.block.BlockStem"))
        {
            // This is a vanilla crop; ; let's implement the interface
            return transformToDecay(basicClass, !transformedName.equals(name), transformedName, true);
        }
        else if (transformedName.equals("net.minecraft.block.Block") )
        {
            // Inject the hook used for crop decay
            return addRandomTickHook(basicClass, !transformedName.equals(name), transformedName, true);
        }
        else if (basicClass != null)
        {
            // Check if some crop implements the interface, and if it does then inject the crop decay hook
            ClassReader classReader = new ClassReader(basicClass);
            InterfaceCheckVisitor visitor = new InterfaceCheckVisitor("toughasnails/api/season/IDecayableCrop");
            classReader.accept(visitor, ClassReader.SKIP_CODE);
            if (visitor.isInterfaceFound) {
                return transformToDecay(basicClass, !transformedName.equals(name), transformedName, false);
            }
        }
        return basicClass;
    }
    
    private byte[] transformToDecay(byte[] bytes, boolean obfuscatedClass, String name, boolean isVanilla)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        if (isVanilla)
        {
            // Vanilla crops need the interface added
            classNode.interfaces.add("toughasnails/api/season/IDecayableCrop");
        }
        
        if (isVanilla)
        {
            //Implement shouldDecay() method, which simply returns true. The method allows subclasses to override behavior.
            MethodNode decayMethod = new MethodNode(Opcodes.ACC_PUBLIC, "shouldDecay", "()Z", null, null);
            InsnList decayInsns = new InsnList();
            decayInsns.add(new LdcInsnNode(new Integer(1)));
            decayInsns.add(new InsnNode(Opcodes.IRETURN));
            decayMethod.instructions.add(decayInsns);
            classNode.methods.add(decayMethod);
        }
        
        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        bytes = writer.toByteArray();
        
        return bytes;
    }

    private byte[] addRandomTickHook(byte[] bytes, boolean obfuscatedClass, String name, boolean isVanilla)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        List<String> successfulTransformations = Lists.newArrayList();

        //Iterate over the methods in the class
        for (MethodNode methodNode : classNode.methods)
        {
            if (ASMHelper.methodEquals(methodNode, RANDOM_TICK_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos", "net/minecraft/block/state/IBlockState", "java/util/Random")))
            {
                InsnList insnList = new InsnList();

                //Get the current season
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "onRandomTick", ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/block/Block", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos"), false));

                //Insert our new instructions before returning
                methodNode.instructions.insertBefore(methodNode.instructions.get(methodNode.instructions.indexOf(methodNode.instructions.getLast()) - 1), insnList);

                successfulTransformations.add(methodNode.name + " " + methodNode.desc);
            }
        }

        // The vanilla method does not exist? What is this sorcery?!?
        if (successfulTransformations.size() != 1) throw new RuntimeException("An error occurred transforming " + name + ". Applied transformations: " + successfulTransformations.toString());

        //Encode the altered class back into bytes
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        bytes = writer.toByteArray();

        return bytes;
    }
    
    public static class InterfaceCheckVisitor extends ClassVisitor
    {
        public boolean isInterfaceFound;
        public String searchInterface;
        public InterfaceCheckVisitor(String searchInterface)
        {
            super(Opcodes.ASM5);
            this.isInterfaceFound = false;
            this.searchInterface = searchInterface;
        }
        
        @Override
        public void visit(int version, int access, String name, String signature,
                String superName, String[] interfaces)
        {
            for (String iface : interfaces)
            {
                if (iface.equals(searchInterface))
                {
                    isInterfaceFound = true;
                    return;
                }
            }
        }
    }
}
