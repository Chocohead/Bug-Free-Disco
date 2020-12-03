package com.chocohead.smoothen;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo.Method;
import org.spongepowered.asm.util.Annotations;

import com.chocohead.smoothen.util.Mixin;

public class SlotPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return Collections.emptyList();
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {	
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		Mixin thisMixin = Mixin.create(mixinInfo);
		Map<String, int[]> shifts = new HashMap<>();

		on: for (Method method : thisMixin.getMethods()) {
			for (MethodNode actualMethod : thisMixin.getClassNode().methods) {
				if (actualMethod.name.equals(method.getOriginalName()) && actualMethod.desc.equals(method.getOriginalDesc())) {
					if (actualMethod.invisibleParameterAnnotations != null) {
						Type[] arguments = Type.getArgumentTypes(actualMethod.desc);
						String callback = CallbackInfo.getCallInfoClassName(Type.getReturnType(actualMethod.desc));
						int end = arguments.length;

						int[] shiftMap = new int[end];
						Arrays.fill(shiftMap, -1);

						boolean found = false;
						for (int i = 0; i < end; i++) {
							String argType = arguments[i].getInternalName(); //Only allow shifting parameter types rather than locals too
							if (callback.equals(argType)) break;

							AnnotationNode slotNode = Annotations.getInvisibleParameter(actualMethod, Slot.class, i);
							if (slotNode != null) {
								int slot = Annotations.getValue(slotNode);

								shiftMap[i] = slot;
								found = true;
							}
						}

						if (found) {
							int[] existing = shifts.put(method.getName().concat(method.getDesc()), shiftMap);
							if (existing != null) throw new IllegalStateException("Two callbacks have the same signature: " + mixinClassName + method);
						}
					}

					continue on;
				}
			}

			throw new IllegalStateException("Couldn't find " + mixinClassName + '#' + method);
		}

		if (!shifts.isEmpty()) {
			targetClassName = targetClassName.replace('.', '/');

			for (MethodNode method : targetClass.methods) {
				for (AbstractInsnNode insn : method.instructions) {
					if (insn.getType() == AbstractInsnNode.METHOD_INSN) {
						MethodInsnNode methodInsn = (MethodInsnNode) insn;
						if (!targetClassName.equals(methodInsn.owner)) continue;

						int[] shiftMap = shifts.get(methodInsn.name.concat(methodInsn.desc));
						if (shiftMap != null) {
							Type returnType = Type.getReturnType(method.desc);
							String callback = CallbackInfo.getCallInfoClassName(returnType);
							Type[] arguments = Type.getArgumentTypes(methodInsn.desc);

							AbstractInsnNode localStart = insn;
							int locals = 0;

							for (int i = arguments.length - 1; i >= 0; i--, locals++) {
								if (callback.equals(arguments[i].getInternalName())) break;

								if (localStart.getType() != AbstractInsnNode.VAR_INSN) {
									throw new IllegalStateException("Unexpected opcode during local loading: " + localStart.getType());
								}
								localStart = localStart.getPrevious();
							}

							AbstractInsnNode argsEnd = localStart.getPrevious();
							switch (argsEnd.getType()) {
							case AbstractInsnNode.METHOD_INSN: {
								MethodInsnNode methodInsnAlso = (MethodInsnNode) argsEnd;

								if (!callback.equals(methodInsnAlso.owner) || !"<init>".equals(methodInsnAlso.name)) {
									throw new IllegalStateException("Unexpected method call before local loading: " + methodInsnAlso.owner + '#' + methodInsnAlso.name + methodInsnAlso.desc);
								}

								if (Type.getArgumentTypes(methodInsnAlso.desc).length > 2 && (argsEnd = argsEnd.getPrevious()).getType() != AbstractInsnNode.VAR_INSN) {
									throw new IllegalStateException("Unexpected opcode before callback creation: " + argsEnd.getType());
								}

								for (int i = 0; i <= 4; i++) argsEnd = argsEnd.getPrevious();
								break;
							}

							case AbstractInsnNode.VAR_INSN:	
								break; //Reusing an existing callback which is fine

							default:
								throw new IllegalStateException("Unexpected opcode before local loading: " + argsEnd.getType());
							}

							for (int arg = arguments.length - 1 - locals - 1; arg >= 0; arg--, argsEnd = argsEnd.getPrevious()) {
								if (argsEnd.getType() != AbstractInsnNode.VAR_INSN) throw new IllegalStateException("Didn't find argument var load: " + argsEnd.getType());
								VarInsnNode varInsn = (VarInsnNode) argsEnd;

								int move = shiftMap[arg];
								if (move >= 0) varInsn.var = move;
							}
						}
					}
				}
			}
		}
	}
}