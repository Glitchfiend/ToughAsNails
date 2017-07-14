/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.asm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import com.google.common.collect.Lists;

public class ASMHelper {
	public static final Logger LOGGER = LogManager
			.getLogger("ToughAsNails Transformer");

	public static boolean methodEquals(MethodNode methodNode, String[] names,
			String desc) {
		boolean nameMatches = false;

		for (String name : names) {
			if (methodNode.name.equals(name)) {
				nameMatches = true;
				break;
			}
		}

		return nameMatches && methodNode.desc.equals(desc);
	}

	public static void clearNextInstructions(MethodNode methodNode,
			AbstractInsnNode insnNode) {
		Iterator<AbstractInsnNode> iterator = methodNode.instructions
				.iterator(methodNode.instructions.indexOf(insnNode));

		while (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	public static MethodInsnNode getUniqueMethodInsnNode(MethodNode methodNode,
			int opcode, String owner, String[] names, String desc) {
		List<MethodInsnNode> matchedMethodNodes = matchMethodInsnNodes(
				methodNode, opcode, owner, names, desc);

		if (matchedMethodNodes.isEmpty())
			throw new RuntimeException(
					"No method instruction node found matching " + owner + " "
							+ names[0] + " " + desc);
		if (matchedMethodNodes.size() > 1)
			LOGGER.warn("Too many matched instructions were found in "
					+ methodNode.name + " for " + owner + " " + names[0] + " "
					+ desc + ". Crashes or bugs may occur!");

		return matchedMethodNodes.get(matchedMethodNodes.size() - 1);
	}

	public static List<MethodInsnNode> matchMethodInsnNodes(
			MethodNode methodNode, int opcode, String owner, String[] names,
			String desc) {
		ArrayList<MethodInsnNode> matches = Lists.newArrayList();
		ArrayList<String> validMethodNames = Lists.newArrayList(names);

		for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
			if (insnNode instanceof MethodInsnNode
					&& insnNode.getOpcode() == opcode) {
				MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;

				if (methodInsnNode.owner.equals(owner)
						& validMethodNames.contains(methodInsnNode.name)
						&& methodInsnNode.desc.equals(desc)) {
					matches.add(methodInsnNode);
				}
			}
		}

		return matches;
	}

	public static void verifyClassHash(String className, byte[] bytes,
			String... expectedHashes) {
		String currentHash = DigestUtils.md5Hex(bytes);

		if (!Lists.newArrayList(expectedHashes).contains(currentHash)) {
			String error = String.format(
					"Unexpected hash %s detected for class %s. Crashes or bugs may occur!",
					currentHash, className);
			LOGGER.error(error);
		} else {
			LOGGER.info(String.format("Valid hash %s found for class %s.",
					currentHash, className));
		}
	}

	private static Printer printer = new Textifier();
	private static TraceMethodVisitor methodVisitor = new TraceMethodVisitor(
			printer);

	public static void printMethod(MethodNode methodNode) {
		for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
			insnNode.accept(methodVisitor);
			StringWriter stringWriter = new StringWriter();
			printer.print(new PrintWriter(stringWriter));
			printer.getText().clear();

			LOGGER.info(stringWriter.toString().replace("\n", ""));
		}
	}
}
