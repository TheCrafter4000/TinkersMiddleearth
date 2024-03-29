package com.thecrafter4000.lotrtc.asm;

import com.google.common.collect.ImmutableList;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static org.objectweb.asm.Opcodes.*;

/**
 * Utilities for ASM operations.
 *
 * @author TheCrafter4000
 */
public class ASMUtils {

	/**
	 * Creates a new class with ASM.
	 *
	 * @param name The fully qualified name of the new class. You may use dots or slashes as
	 * separator.
	 * @param superclass The superclass of the new class. Must not be null.
	 * @param access The access opcode of the class
	 * @param interfaces All interfaces that are to be implemented by the new class. Can be null or
	 * empty.
	 * @param constructor The default constructor of the new class. Must not be null.
	 * @param methods All methods that should be implemented by the new class. May be empty.
	 * @param fields All fields the new class should have. May be empty.
	 * @param consumer A list of operations that can be performed on the class before defining it.
	 * Can be null or empty.
	 * @param <T> The superclass
	 * @return The new class. Can be accessed via the default launch classloader.
	 */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> Class<? extends T> buildClass(String name, Class<T> superclass, int access,
	                                                Class<?>[] interfaces, MethodNode constructor,
	                                                MethodNode[] methods, FieldNode[] fields,
	                                                Consumer<ClassNode>... consumer) {
		ClassNode node = new ClassNode();
		String[] interfaceStrings = convert(ASMUtils::getInternalName, String.class, interfaces);

		node.visit(V1_8, access, getInternalName(name), null, getInternalName(superclass),
		           interfaceStrings);

		node.methods.add(constructor);
		node.methods.addAll(ImmutableList.copyOf(methods));
		node.fields.addAll(ImmutableList.copyOf(fields));

		for (Consumer<ClassNode> con : ensureNotNull(Consumer.class, consumer)) {
			con.accept(node);
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);

		return (Class<? extends T>) DynamicClassLoader.INSTANCE
						.defineClass(name.replace('/', '.'), cw.toByteArray());
	}

	/**
	 * Retrieves the default constructor of the superclass and generates a default constructor for
	 * subclasses.
	 *
	 * @param superclass The superclass
	 * @param conArgs The arguments of the superclass constructor. The returned constructor will
	 * have the same arguments.
	 * @return The subclass constructor
	 */
	public static MethodNode generateDefaultConstructor(Class<?> superclass, Class<?>... conArgs) {
		if (superclass == null) {
			throw new RuntimeException("Superclass must not be null!");
		}

		Class<?>[] args = ensureNotNull(Class.class, conArgs);
		String desc = genMethodDesc(void.class, args);

		/* Resolving constructor, since it must be available for later call in subclass */
		Constructor con;
		try {
			con = superclass.getConstructor(args);
		} catch (Exception e) {
			throw new RuntimeException(
							"Could not resolve constructor " + superclass.getName() + ".<init>"
							+ desc, e);
		}
		String[] exceptions = convert(ASMUtils::getInternalName, String.class, con.getExceptionTypes());

		/* Build constructor. */
		MethodNode method = new MethodNode(ACC_PUBLIC, "<init>", desc, null, exceptions);

		/* Loading variables */
		method.instructions.add(new VarInsnNode(ALOAD, 0)); // Loads 'this'
		int varOff = 0;
		for (Class<?> param : args) {
			method.instructions.add(
							new VarInsnNode(adjustOpcode(param, ILOAD),
							                (varOff += getTypeSize(param))));
		}

		method.instructions.add(new MethodInsnNode(INVOKESPECIAL, getInternalName(superclass),
		                                           "<init>", desc, false));
		method.instructions.add(new InsnNode(RETURN));
		method.visitEnd();

		return method;
	}

	/**
	 * Adjusts the given opcode to the types type, e.g. returns {@code Opcodes#ALOAD} if the class
	 * represents an object and the given opcode is {@code Opcodes#ILOAD}. Wrapper method for {@link
	 * Type#getOpcode(int)}.
	 */
	public static int adjustOpcode(Class<?> clazz, int opcode) {
		return Type.getType(clazz).getOpcode(opcode);
	}

	/**
	 * Returns whether this type needs 0(void), 1(int) or 2(long) bytes of space. Wrapper method for
	 * {@link Type#getSize()}.
	 */
	public static int getTypeSize(Class<?> clazz) {
		return Type.getType(clazz).getSize();
	}

	/**
	 * Returns the internal name of this class, used in ASM code.
	 */
	public static String getInternalName(Class clazz) {
		return getInternalName(clazz.getName());
	}

	/**
	 * Returns the internal name of this class, used in ASM code.
	 */
	public static String getInternalName(String name) {
		return name.replace('.', '/');
	}

	/**
	 * Generates a description string for the given class, used in ASM code.
	 */
	public static String getDescription(Class clazz) {
		if (clazz == null) {
			return "";
		}

		return Type.getType(clazz).getDescriptor();
	}

	/**
	 * Generates a method description string for the given arguments and return type.
	 */
	public static String genMethodDesc(Class returnType, Class... args) {
		StringBuilder builder = new StringBuilder().append('(');

		for (Class arg : args) {
			builder.append(getDescription(arg));
		}

		builder.append(')');
		builder.append(getDescription(returnType));
		return builder.toString();
	}

	/**
	 * Gets a method
	 *
	 * @param clazz the class to search in
	 * @param name The name of the method.
	 * @param desc The descriptor.
	 * @return The corresponding {@link MethodNode}
	 */
	public static MethodNode resolveMethod(ClassNode clazz, String name, String desc) {
		for (MethodNode method : clazz.methods) {
			String mName = method.name;
			String mDesc = method.desc;

			if (mName.equals(name) && mDesc.equals(desc)) {
				return method;
			}
		}
		return null;
	}

	/**
	 * Gets a field
	 *
	 * @param clazz the class to search in
	 * @param name The name of the field.
	 * @param desc The descriptor of this field.
	 * @return The corresponding {@link FieldNode}
	 */
	public static FieldNode resolveField(ClassNode clazz, String name, String desc) {
		for (FieldNode node : clazz.fields) {
			String mName = node.name;
			String mDesc = node.desc;

			if (mName.equals(name) && mDesc.equals(desc)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Copy a subset of methods from one class to another
	 * @param name Name of the source class
	 * @param overwrite If true, all methods that are added will be removed in the destination
	 * @return True if success
	 */
	public static boolean applyOverlay(String name, ClassNode destination, Predicate<MethodNode> methods, boolean overwrite) {
		String internalName = ASMUtils.getInternalName(name);
		InputStream rawClassData = null;
		try {
			// Note: The system class loader returns null sometimes for some reason
			rawClassData = Launch.classLoader.getResourceAsStream(internalName + ".class");
			if(rawClassData == null) {
				System.err.println("Failed to load class \"" + internalName + "\"!");
				return false;
			}

			// Resolves the supplied class into a ClassNode
			ClassReader r = new ClassReader(rawClassData);
			ClassNode source = new ClassNode();
			r.accept(source, 0);

			// Applies the predicate to all methods already present and deletes them if they match
			if(overwrite) destination.methods.removeIf(methods);

			for (MethodNode n : source.methods) {
				if (methods.test(n)) {
					// Redirect certain method/field calls to not point at the overlay class
					for (ListIterator<AbstractInsnNode> it = n.instructions.iterator(); it.hasNext(); ) {
						AbstractInsnNode node = it.next();
						switch(node.getType()) {
							case AbstractInsnNode.FIELD_INSN: {
								FieldInsnNode fn = (FieldInsnNode) node;
								if(fn.owner.equals(internalName)) fn.owner = destination.name;
								break;
							}
							case AbstractInsnNode.METHOD_INSN: {
								MethodInsnNode mn = (MethodInsnNode) node;
								if(mn.owner.equals(internalName)) mn.owner = destination.name;
								break;
							}
							case AbstractInsnNode.FRAME: {
								FrameNode fn = (FrameNode) node;
								UnaryOperator<Object> op = (obj) -> {
									if (internalName.equals(obj)) return destination.name;
									return obj;
								};
								if(fn.local != null) fn.local.replaceAll(op);
								if(fn.stack != null) fn.stack.replaceAll(op);
								break;
							}
						}
					}
					if(n.localVariables != null) {
						for(LocalVariableNode node : n.localVariables) {
							if(node.name.equals("this")) node.desc = "L" + destination.name + ";";
						}
					}
					destination.methods.add(n);
				}
			}
			return true;
		} catch(IOException e) {
			System.err.println("Failed to copy methods for \"" + internalName + "\": " + e.getMessage());
			e.printStackTrace();
		} finally {
			if(rawClassData != null) {
				try {
					rawClassData.close();
				} catch (IOException e) {
					System.err.println("Failed to close file \"" + internalName + "\": " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static Predicate<MethodNode> matchNames(String... names) {
		List<String> list = Arrays.asList(names);
		return node -> {
			return list.contains(node.name);
		};
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <V, T> T[] convert(Function<V, T> converter, Class<T> clazz, V... elements) {
		int size = elements == null ? 0 : elements.length;

		T[] array = (T[]) Array.newInstance(clazz, size);
		for (int i = 0; i < size; i++) {
			array[i] = converter.apply(elements[i]);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static <T> T[] ensureNotNull(Class<T> clazz, T... array) {
		return array == null ? (T[]) Array.newInstance(clazz, 0) : array;
	}


	/**
	 * Internal classloader to load classes from byte arrays. Uses {@link Launch#classLoader} as
	 * parent.
	 *
	 * @author TheCrafter4000
	 */
	public static class DynamicClassLoader extends ClassLoader {
		public static final DynamicClassLoader INSTANCE = new DynamicClassLoader();

		private DynamicClassLoader() {
			super(Launch.classLoader);
		}

		public Class<?> defineClass(String name, byte[] content) {
			return defineClass(name, content, 0, content.length);
		}
	}
}
