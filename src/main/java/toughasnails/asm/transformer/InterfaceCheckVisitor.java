package toughasnails.asm.transformer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class InterfaceCheckVisitor extends ClassVisitor {
	public boolean isInterfaceFound;
	public String searchInterface;

	public InterfaceCheckVisitor(String searchInterface) {
		super(Opcodes.ASM5);
		this.isInterfaceFound = false;
		this.searchInterface = searchInterface;
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		for (String iface : interfaces) {
			if (iface.equals(searchInterface)) {
				isInterfaceFound = true;
				return;
			}
		}
	}
}
