package com.chocohead.smoothen.mixin;

import org.lwjgl.opengl.GL11;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_192;

@Mixin(class_192.class)
class GameRendererMixin {
	@Inject(method = "method_511", at = {@At(value = "NEW", target = "net/minecraft/class_179"), 
			@At(value = "INVOKE", target = "Lnet/minecraft/world/ClientWorldManager;method_459(Lnet/minecraft/entity/living/player/Player;ID)I", ordinal = 1)})
	private void smoothShade(CallbackInfo call) {
		GL11.glShadeModel(GL11.GL_SMOOTH);
	}

	//The class_216.method_557() call leaks flat back so it isn't necessary to set it here
	@Inject(method = "method_511", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ClientWorldManager;method_454(ID)V", shift = Shift.BY, by = 2))
	private void flatShade(CallbackInfo call) {
		GL11.glShadeModel(GL11.GL_FLAT);
	}
}