package com.chocohead.smoothen.util;

import java.lang.reflect.Field;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo.Method;

public class Mixin implements Comparable<Mixin> {
	private final IMixinInfo mixin;
	private final ClassInfo info;
	private ClassNode node;

	public static Mixin create(IMixinInfo mixin) {
		return new Mixin(mixin);
	}

	Mixin(IMixinInfo mixin) {
		this.mixin = mixin;
		try {
			Field info = mixin.getClass().getDeclaredField("info");
			info.setAccessible(true); //We'd like this please
			this.info = (ClassInfo) info.get(mixin);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Error getting class info from " + mixin, e);
		}
	}

	public String getName() {
		return mixin.getClassRef();
	}

	public ClassNode getClassNode() {
		if (node == null) {
			node = mixin.getClassNode(ClassReader.SKIP_CODE);
		}
		
		return node;
	}

	public Set<Method> getMethods() {
		return info.getMethods();
	}

	public boolean hasMethod(String name, String descriptor) {
		return getMethod(name, descriptor) != null;
	}

	public Method getMethod(String name, String descriptor) {
		return info.findMethod(name, descriptor, ClassInfo.INCLUDE_ALL | ClassInfo.INCLUDE_INITIALISERS);
	}

	@Override
	@SuppressWarnings("unchecked") //The underlying type is comparable even if the interface isn't
	public int compareTo(Mixin other) {
		return ((Comparable<IMixinInfo>) mixin).compareTo(other.mixin);
	}

	@Override
	public String toString() {
		return getName();
	}
}