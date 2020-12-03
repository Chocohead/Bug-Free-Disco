package com.chocohead.smoothen.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.Material;

import com.chocohead.smoothen.BlockExtras;

@Mixin(Block.class)
class BlockMixin {
	@Shadow
	public @Final int id;
	@Shadow
	public @Final Material material;

	@Inject(method = "<init>(ILnet/minecraft/block/Material;)V", at = @At("RETURN"))
	private void onNew(CallbackInfo call) {
		BlockExtras.canBlockGrass[id] = !material.isFluid();
	}
}