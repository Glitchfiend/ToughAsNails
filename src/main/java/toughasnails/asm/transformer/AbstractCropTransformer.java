package toughasnails.asm.transformer;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

public abstract class AbstractCropTransformer implements IClassTransformer
{
    private static final String[] UPDATE_TICK_NAMES = new String[] { "updateTick", "func_180650_b", "b" };
    
    public static enum WinterBehavior {
        DECAY,
        HIBERNATE;
    }
    
    protected byte[] transformCrop(byte[] bytes, boolean obfuscatedClass, String shortClassName, String[] validHashes, WinterBehavior transformType)
    {
        //Decode the class from bytes
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        //Check this class is unmodified
        ASMHelper.verifyClassHash(shortClassName, bytes, validHashes);
        
        if (transformType == WinterBehavior.DECAY)
        {
            //Instances of IDecayableCrop decay into dead crops when cold
            classNode.interfaces.add("toughasnails/api/season/IDecayableCrop");
        }
        else if (transformType == WinterBehavior.HIBERNATE)
        {
            //Instances of IHibernatingCrop do not tick when cold
            classNode.interfaces.add("toughasnails/api/season/IHibernatingCrop");
        }
        
        List<String> successfulTransformations = Lists.newArrayList();
        
        //Iterate over the methods in the class
        for (MethodNode methodNode : classNode.methods)
        {
            if (ASMHelper.methodEquals(methodNode, UPDATE_TICK_NAMES, ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos", "net/minecraft/block/state/IBlockState", "java/util/Random")))
            { 
                InsnList insnList = new InsnList();
                
                //Determine crop behavior based on the current season
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                if (transformType == WinterBehavior.DECAY)
                {
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "onUpdateTick", ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/block/Block", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos"), false));
                    //Insert our new instructions before returning
                    methodNode.instructions.insertBefore(methodNode.instructions.get(methodNode.instructions.indexOf(methodNode.instructions.getLast()) - 1), insnList);
                }
                else if (transformType == WinterBehavior.HIBERNATE)
                {
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "shouldHibernate", ObfHelper.createMethodDescriptor(obfuscatedClass, "Z", "net/minecraft/block/Block", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos"), false));
                    //If above statement is true, don't let the function do anything else (i.e. stop the crop from ticking into a ripe block state)
                    LabelNode resumeUpdateTick = new LabelNode();
                    insnList.add(new JumpInsnNode(Opcodes.IFEQ, resumeUpdateTick));
                    insnList.add(new InsnNode(Opcodes.RETURN));
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
