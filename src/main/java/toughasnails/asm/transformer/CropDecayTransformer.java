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
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.Lists;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.classloading.FMLForgePlugin;
import toughasnails.asm.ASMHelper;
import toughasnails.asm.ObfHelper;

public class CropDecayTransformer implements IClassTransformer {
	private static final String[] VALID_HASHES = new String[] {
			// BlockCrops
			"3d74307bb515539176e7a84967b10a28",
			"b835f0bbb24031fee6ad804d8c48d2dc",
			// BlockStem
			"6a28b8cb3a448cb0b9fa7f5c5d7df8d9" };

	private static final String[] UPDATE_TICK_NAMES = new String[] {
			"updateTick", "func_180650_b", "b" };

	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		if (transformedName.equals("net.minecraft.block.BlockCrops")
				|| transformedName.equals("net.minecraft.block.BlockStem")) {

			// This is a vanilla crop; let's implement the interface and inject
			// the crop decay hook
			return transformToDecay(basicClass, !FMLForgePlugin.RUNTIME_DEOBF,
					transformedName, true);
		} else {
			// Check if some crop implements the interface, and if it does then
			// inject the crop decay hook
			ClassReader classReader = new ClassReader(basicClass);
			InterfaceCheckVisitor visitor = new InterfaceCheckVisitor(
					"toughasnails/api/season/IDecayableCrop");
			classReader.accept(visitor, ClassReader.SKIP_CODE);
			if (visitor.isInterfaceFound) {
				return transformToDecay(basicClass,
						!FMLForgePlugin.RUNTIME_DEOBF, transformedName, false);
			}
		}

		return basicClass;
	}

	private byte[] transformToDecay(byte[] bytes, boolean obfuscatedClass,
			String name, boolean isVanilla) {
		// Decode the class from bytes
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		if (isVanilla) {
			// Check this class is unmodified
			ASMHelper.verifyClassHash(name, bytes, VALID_HASHES);
			// Vanilla crops need the interface added
			classNode.interfaces.add("toughasnails/api/season/IDecayableCrop");
		}

		List<String> successfulTransformations = Lists.newArrayList();

		// Iterate over the methods in the class
		for (MethodNode methodNode : classNode.methods) {
			if (ASMHelper.methodEquals(methodNode, UPDATE_TICK_NAMES,
					ObfHelper.createMethodDescriptor(obfuscatedClass, "V",
							"net/minecraft/world/World",
							"net/minecraft/util/math/BlockPos",
							"net/minecraft/block/state/IBlockState",
							"java/util/Random"))) {
				InsnList insnList = new InsnList();

				// Get the current season
				insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
				insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
				insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
				insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
						"toughasnails/season/SeasonASMHelper", "onUpdateTick",
						ObfHelper.createMethodDescriptor(obfuscatedClass, "V",
								"net/minecraft/block/Block",
								"net/minecraft/world/World",
								"net/minecraft/util/math/BlockPos"),
						false));

				// Insert our new instructions before returning
				methodNode.instructions
						.insertBefore(
								methodNode.instructions
										.get(methodNode.instructions
												.indexOf(methodNode.instructions
														.getLast())
												- 1),
								insnList);

				successfulTransformations
						.add(methodNode.name + " " + methodNode.desc);
			}
		}

		if (isVanilla) {
			// The vanilla method does not exist? What is this sorcery?!?
			if (successfulTransformations.size() != 1)
				throw new RuntimeException("An error occurred transforming "
						+ name + ". Applied transformations: "
						+ successfulTransformations.toString());

			// Implement shouldDecay() method, which simply returns true. The
			// method allows subclasses to override behavior.
			MethodNode decayMethod = new MethodNode(Opcodes.ACC_PUBLIC,
					"shouldDecay", "()Z", null, null);
			InsnList decayInsns = new InsnList();
			decayInsns.add(new LdcInsnNode(new Integer(1)));
			decayInsns.add(new InsnNode(Opcodes.IRETURN));
			decayMethod.instructions.add(decayInsns);
			classNode.methods.add(decayMethod);
		}

		// Encode the altered class back into bytes
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		bytes = writer.toByteArray();

		return bytes;
	}
}