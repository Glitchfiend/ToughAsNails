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

//Melons and pumpkins
public class BlockStemTransformer implements IClassTransformer
{
    private static final String[] VALID_HASHES = new String[] { "6a28b8cb3a448cb0b9fa7f5c5d7df8d9" };
    
    private static final String[] UPDATE_TICK_NAMES = new String[] { "updateTick", "func_180650_b", "b" };
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
    	if (transformedName.equals("net.minecraft.block.BlockStem"))
        {
            return transformBlockStem(basicClass, !transformedName.equals(name), "BlockStem", VALID_HASHES);
        }
        return basicClass;
    }
    
    private byte[] transformBlockStem (byte[] bytes, boolean obfuscatedClass, String shortClassName, String[] validHashes)
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
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "toughasnails/season/SeasonASMHelper", "onUpdateTick", ObfHelper.createMethodDescriptor(obfuscatedClass, "V", "net/minecraft/block/Block", "net/minecraft/world/World", "net/minecraft/util/math/BlockPos"), false));
            
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