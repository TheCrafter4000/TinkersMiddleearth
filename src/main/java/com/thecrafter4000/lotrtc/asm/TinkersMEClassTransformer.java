package com.thecrafter4000.lotrtc.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class TinkersMEClassTransformer implements IClassTransformer {

	public static Map<String, BiConsumer<ClassNode, Boolean>> classes = new HashMap<>();

	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		boolean isObfuscated = !name.equals(transformedName);
		if(classes.containsKey(transformedName)) {
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(basicClass);
			classReader.accept(classNode, 0);
			System.out.println("TinkersME: Transforming " + transformedName);
			try {
				classes.get(transformedName).accept(classNode, isObfuscated);
				classes.remove(transformedName);
			} catch(Exception e) {
				e.printStackTrace();
				return basicClass;
			}
			ClassWriter classWriter = new ClassWriter(1);
			classNode.accept(classWriter);
			basicClass = classWriter.toByteArray();

//			try {
//				FileUtils.writeByteArrayToFile(new File("asm/" + transformedName + ".class"), basicClass);
//			} catch (IOException e) {
//				System.err.printf("Error writing to \"asm\\%s.class\": %s", transformedName, e.getMessage());
//			}
		}
		return basicClass;
	}

	static{
		// "tconstruct.tools.gui.GuiButtonTool.background" and "tconstruct.tools.gui.GuiButtonStencil.background" are static, making it impossible to have a second texture file for my own stuff
		BiConsumer<ClassNode, Boolean> fixButtons = (node, isObf) -> {
			for (FieldNode field : node.fields) {
				if(field.name.equals("background")) {
					field.access = Opcodes.ACC_PUBLIC;
				}
			}

			for (MethodNode method : node.methods) {
				if(method.name.equals("<init>") || method.name.equals("drawButton")) {
					AbstractInsnNode[] instructions = method.instructions.toArray();
					for(int i = 0; i < instructions.length; i++) {
						AbstractInsnNode insn = instructions[i];
						if(insn.getOpcode() == Opcodes.PUTSTATIC || insn.getOpcode() == Opcodes.GETSTATIC) {
							FieldInsnNode fnode = (FieldInsnNode) insn;
							if(fnode.name.equals("background")) {
								if(insn.getOpcode() == Opcodes.GETSTATIC) {
									method.instructions.insertBefore(fnode, new VarInsnNode(Opcodes.ALOAD, 0));
								}else{
									method.instructions.insertBefore(instructions[i-5], new VarInsnNode(Opcodes.ALOAD, 0));
								}
								method.instructions.set(fnode, new FieldInsnNode(fnode.getOpcode()+2, fnode.owner, fnode.name, fnode.desc));
							}
						}
					}
				}
			}
		};

		BiConsumer<ClassNode, Boolean> changeSmelteryBlockCheck = (node, isObf) -> {
			ASMUtils.publicizeMethods(node, methodNode -> methodNode.name.equals("validBlockID") || methodNode.name.equals("validTankID"));
		};

		// Passing the wrong texture file to the buttons...
		// See tconstruct.tools.gui.ToolStationGui.java:107
		// See tconstruct.tools.gui.ToolForgeGui.java:36
		// See tconstruct.tools.gui.ToolForgeGui.java:43
		// "new GuiButtonTool(0, ..., element.buttonIconY, REPAIR.domain, element.texture, element);"
		BiConsumer<ClassNode, Boolean> fixTextureDomain = (node, isObf) -> {
			MethodNode method = ASMUtils.resolveMethod(node, "initGui", "()V");
			if(method == null) {
				System.err.println(node.name + ": Error finding method initGui()V; UI might be broken!");
				return;
			}
			int varOperand = "tconstruct/tools/gui/ToolStationGui".equals(node.name) ? 4 : 5;

			// Due to lazy copy-pasting by TC, we are forced to change "repair.domain" into "element.domain" multiple times
			// "repair.domain" is correct the first time tho, so we skip the first hit
			int c = 0;
			AbstractInsnNode[] instructions = method.instructions.toArray();
			for(int i = 0; i < instructions.length; i++) {
				AbstractInsnNode insn = instructions[i];
				// Looking for
				//   ALOAD 1
				//   GETFIELD tconstruct/library/client/ToolGuiElement.domain : Ljava/lang/String;
				if(insn.getOpcode() == Opcodes.GETFIELD) {
					FieldInsnNode insn1 = (FieldInsnNode) insn;
					if(
					  insn1.owner.equals("tconstruct/library/client/ToolGuiElement") &&
					  insn1.name.equals("domain") &&
					  insn1.desc.equals("Ljava/lang/String;")
					) {
						if(c++ == 0) continue; // Skip first encounter
						VarInsnNode load = (VarInsnNode) instructions[i-1];
						load.var = varOperand;
						// Changing it into
						//   ALOAD <varOperand>
						//   GETFIELD tconstruct/library/client/ToolGuiElement.domain : Ljava/lang/String;
						// where <varOperand> points to "element"
					}
				}
			}

			int expectedHits = "tconstruct/tools/gui/ToolStationGui".equals(node.name) ? 2 : 3;
			if(c != expectedHits) System.out.printf("%s: Found domain field %d times, expected %d; UI might be broken!\n", node.name, c, expectedHits);
		};

		// Overwrites slot drawing in tconstruct.tools.gui.ToolStationGui.drawGuiContainerBackgroundLayer
		BiConsumer<ClassNode, Boolean> overwriteSlotDrawing = (node, isObf) -> {
			MethodNode method = ASMUtils.resolveMethod(node, "drawGuiContainerBackgroundLayer", "(FII)V");
			if(method == null) {
				System.err.println(node.name + ": Error finding method drawGuiContainerBackgroundLayer(FII)V; UI might be broken!");
				return;
			}

			// Looking for "GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);" as markers
			int start = -1, stop = -1;
			int c = 0;
			AbstractInsnNode[] instructions = method.instructions.toArray();
			for(int i = 0; i < instructions.length; i++) {
				AbstractInsnNode insn = instructions[i];
				if(insn.getOpcode() == Opcodes.INVOKESTATIC) {
					MethodInsnNode insn1 = (MethodInsnNode) insn;
					if(
						insn1.owner.equals("org/lwjgl/opengl/GL11") &&
						insn1.name.equals("glColor4f") &&
						insn1.desc.equals("(FFFF)V")
					) {
						switch(c++) {
							case 0: continue;
							case 1: { start = i - 4; continue; }
							case 2: stop = i - 5;
						}
					}
				}
			}

			if(c != 3) System.out.printf("%s: Found glColor4f call %d times, expected 3; UI might be broken!\n", node.name, c);
			if(start < 0 || stop < 0) return;

			// Remove all code between the two glColor4f calls. The first will be removed, the second won't.
			AbstractInsnNode head = method.instructions.get(start - 1);
			for (int i = start; i <= stop; i++) {
				method.instructions.remove(instructions[i]);
			}

			InsnList callOverwrite = new InsnList();
			callOverwrite.add(new VarInsnNode(Opcodes.ALOAD, 0)); // Load this
			callOverwrite.add(new VarInsnNode(Opcodes.ILOAD, 4)); // Load cornerX

			// Get this.guiTop
			callOverwrite.add(new VarInsnNode(Opcodes.ALOAD, 0));
			callOverwrite.add(new FieldInsnNode(Opcodes.GETFIELD, "tconstruct/tools/gui/ToolStationGui", "guiTop", "I"));

			// Get this.customTextureData (this field is injected later on)
			callOverwrite.add(new VarInsnNode(Opcodes.ALOAD, 0));
			callOverwrite.add(new FieldInsnNode(Opcodes.GETFIELD, "tconstruct/tools/gui/ToolStationGui", "customTextureData", "[Z"));

			// Call ExtendedToolGuiElement.drawCustomSlots(this, cornerX, this.guiTop, this.customTextureData)
			callOverwrite.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/thecrafter4000/lotrtc/client/ExtendedToolGuiElement", "drawCustomSlots", "(Ltconstruct/tools/gui/ToolStationGui;II[Z)V", false));

			method.instructions.insert(head, callOverwrite);
		};

		// Injects custom code into tconstruct.tools.gui.ToolStationGui.actionPerformed and tconstruct.tools.gui.ToolForgeGui.actionPerformed
		BiConsumer<ClassNode, Boolean> setCustomTextureData = (node, isObf) -> {
			MethodNode method = ASMUtils.resolveMethod(node, "actionPerformed", "(Lnet/minecraft/client/gui/GuiButton;)V");
			if(method == null) {
				System.err.println(node.name + ": Error finding method actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V; UI might be broken!");
				return;
			}

			InsnList callFunc = new InsnList();
			callFunc.add(new VarInsnNode(Opcodes.ALOAD, 0)); // Loads "this" for the PUTFIELD call
			callFunc.add(new VarInsnNode(Opcodes.ALOAD, 1)); // Loads the function argument
			callFunc.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/thecrafter4000/lotrtc/client/ExtendedToolGuiElement", "getCustomTextureData", "(Lnet/minecraft/client/gui/GuiButton;)[Z", false));
			callFunc.add(new FieldInsnNode(Opcodes.PUTFIELD, "tconstruct/tools/gui/ToolStationGui", "customTextureData", "[Z"));
			method.instructions.insert(callFunc);
		};

		// Injects custom code into tconstruct.tools.gui.ToolStationGui.resetGui and tconstruct.tools.gui.ToolForgeGui.resetGui
		BiConsumer<ClassNode, Boolean> resetCustomTextureData = (node, isObf) -> {
			MethodNode method = ASMUtils.resolveMethod(node, "resetGui", "()V");
			if(method == null) {
				System.err.println(node.name + ": Error finding method resetGui()V; UI might be broken!");
				return;
			}

			InsnList callFunc = new InsnList();
			callFunc.add(new VarInsnNode(Opcodes.ALOAD, 0)); // Loads "this" for the PUTFIELD call
			callFunc.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/thecrafter4000/lotrtc/client/ExtendedToolGuiElement", "emptyTextureData", "[Z"));
			callFunc.add(new FieldInsnNode(Opcodes.PUTFIELD, "tconstruct/tools/gui/ToolStationGui", "customTextureData", "[Z"));
			method.instructions.insert(callFunc);
		};

		// Injects the "public boolean[] customTextureData" field
		BiConsumer<ClassNode, Boolean> addCustomTextureDataField = (node, isObf) -> {
			node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "customTextureData", "[Z", null, null));
		};

		classes.put("tconstruct.tools.gui.GuiButtonTool", fixButtons);
		classes.put("tconstruct.tools.gui.GuiButtonStencil", fixButtons);
		classes.put("tconstruct.tools.gui.ToolStationGui", (node, isObf) -> {
			addCustomTextureDataField.accept(node, isObf);
			fixTextureDomain.accept(node, isObf);
			overwriteSlotDrawing.accept(node, isObf);
			resetCustomTextureData.accept(node, isObf);
			setCustomTextureData.accept(node, isObf);
		});
		classes.put("tconstruct.tools.gui.ToolForgeGui", (node, isObf) -> {
			fixTextureDomain.accept(node, isObf);
			resetCustomTextureData.accept(node, isObf);
			setCustomTextureData.accept(node, isObf);
		});
		classes.put("tconstruct.smeltery.logic.SmelteryLogic", changeSmelteryBlockCheck);
	}
}