package com.chocohead.smoothen.mixin;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.class_193;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tesselator;
import net.minecraft.world.World;

import com.chocohead.smoothen.BlockExtras;
import com.chocohead.smoothen.Slot;

@Mixin(class_193.class)
class BlockRendererMixin {
	@Shadow
	private World field_761;
	@Unique
	private boolean enableAO;
	@Unique
	private float colourRedTopLeft;
	@Unique
	private float colourRedBottomLeft;
	@Unique
	private float colourRedBottomRight;
	@Unique
	private float colourRedTopRight;
	@Unique
	private float colourGreenTopLeft;
	@Unique
	private float colourGreenBottomLeft;
	@Unique
	private float colourGreenBottomRight;
	@Unique
	private float colourGreenTopRight;
	@Unique
	private float colourBlueTopLeft;
	@Unique
	private float colourBlueBottomLeft;
	@Unique
	private float colourBlueBottomRight;
	@Unique
	private float colourBlueTopRight;
	@Unique
	private boolean aoGrassXYZPPC;
	@Unique
	private boolean aoGrassXYZPNC;
	@Unique
	private boolean aoGrassXYZPCP;
	@Unique
	private boolean aoGrassXYZPCN;
	@Unique
	private boolean aoGrassXYZNPC;
	@Unique
	private boolean aoGrassXYZNNC;
	@Unique
	private boolean aoGrassXYZNCN;
	@Unique
	private boolean aoGrassXYZNCP;
	@Unique
	private boolean aoGrassXYZCPP;
	@Unique
	private boolean aoGrassXYZCPN;
	@Unique
	private boolean aoGrassXYZCNP;
	@Unique
	private boolean aoGrassXYZCNN;

	@Inject(method = "method_517", at = @At(value = "FIELD", target = "Lnet/minecraft/class_193;field_761:Lnet/minecraft/world/World;", opcode = Opcodes.GETFIELD, ordinal = 1))
	private void onBlockRender(@Slot(2) Block block, @Slot(3) int x, @Slot(4) int y, @Slot(5) int z, CallbackInfoReturnable<Boolean> call) {
		enableAO = true;

	    aoGrassXYZPPC = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x + 1, y + 1, z)];
	    aoGrassXYZPNC = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x + 1, y - 1, z)];
	    aoGrassXYZPCP = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x + 1, y, z + 1)];
	    aoGrassXYZPCN = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x + 1, y, z - 1)];
	    aoGrassXYZNPC = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x - 1, y + 1, z)];
	    aoGrassXYZNNC = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x - 1, y - 1, z)];
	    aoGrassXYZNCN = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x - 1, y, z - 1)];
	    aoGrassXYZNCP = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x - 1, y, z + 1)];
	    aoGrassXYZCPP = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x, y + 1, z + 1)];
	    aoGrassXYZCPN = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x, y + 1, z - 1)];
	    aoGrassXYZCNP = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x, y - 1, z + 1)];
	    aoGrassXYZCNN = BlockExtras.canBlockGrass[field_761.getBlockAtPosition(x, y - 1, z - 1)];
	}

	@Inject(method = "method_517", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_193;method_515(Lnet/minecraft/block/Block;DDDI)V", ordinal = 0))
	private void prepareBottom(@Slot(2) Block block, @Slot(3) int x, @Slot(4) int y, @Slot(5) int z, CallbackInfoReturnable<Boolean> call) {
		float aoLightValueYNeg = block.method_125(field_761, x, --y, z);

		float topLeftLight, bottomLeftLight, bottomRightLight, topRightLight;
		if (enableAO) {
			float aoLightValueScratchXYNN = block.method_125(field_761, x - 1, y, z);
			float aoLightValueScratchYZNN = block.method_125(field_761, x, y, z - 1);
			float aoLightValueScratchYZNP = block.method_125(field_761, x, y, z + 1);
			float aoLightValueScratchXYPN = block.method_125(field_761, x + 1, y, z);

			float aoLightValueScratchXYZNNN;
			if (aoGrassXYZCNN || aoGrassXYZNNC) {
				aoLightValueScratchXYZNNN = block.method_125(field_761, x - 1, y, z - 1);
			} else {
				aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
			}

			float aoLightValueScratchXYZNNP;
			if (aoGrassXYZCNP || aoGrassXYZNNC) {
				aoLightValueScratchXYZNNP = block.method_125(field_761, x - 1, y, z + 1);
			} else {
				aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
			}

			float aoLightValueScratchXYZPNN;
			if (aoGrassXYZCNN || aoGrassXYZPNC) {
				aoLightValueScratchXYZPNN = block.method_125(field_761, x + 1, y, z - 1);
			} else {
				aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
			}

			float aoLightValueScratchXYZPNP;
			if (aoGrassXYZCNP || aoGrassXYZPNC) {
				aoLightValueScratchXYZPNP = block.method_125(field_761, x + 1, y, z + 1);
			} else {
				aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
			}

			topLeftLight = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + aoLightValueYNeg) / 4;
			topRightLight = (aoLightValueScratchYZNP + aoLightValueYNeg + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4;
			bottomRightLight = (aoLightValueYNeg + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4;
			bottomLeftLight = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + aoLightValueYNeg + aoLightValueScratchYZNN) / 4;
		} else {
			topLeftLight = bottomLeftLight = bottomRightLight = topRightLight = aoLightValueYNeg;
		}

		setColours(0.5F, topLeftLight, bottomLeftLight, bottomRightLight, topRightLight);
	}

	@Inject(method = "method_517", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_193;method_520(Lnet/minecraft/block/Block;DDDI)V", ordinal = 0))
	private void prepareTop(@Slot(2) Block block, @Slot(3) int x, @Slot(4) int y, @Slot(5) int z, CallbackInfoReturnable<Boolean> call) {
		float aoLightValueYPos = block.method_125(field_761, x, ++y, z);

		float topLeftLight, bottomLeftLight, bottomRightLight, topRightLight;
		if (enableAO) {
			float aoLightValueScratchXYNP = block.method_125(field_761, x - 1, y, z);
			float aoLightValueScratchXYPP = block.method_125(field_761, x + 1, y, z);
			float aoLightValueScratchYZPN = block.method_125(field_761, x, y, z - 1);
			float aoLightValueScratchYZPP = block.method_125(field_761, x, y, z + 1);

			float aoLightValueScratchXYZNPN;
			if (aoGrassXYZCPN || aoGrassXYZNPC) {
				aoLightValueScratchXYZNPN = block.method_125(field_761, x - 1, y, z - 1);
			} else {
				aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
			}

			float aoLightValueScratchXYZPPN;
			if (aoGrassXYZCPN || aoGrassXYZPPC) {
				aoLightValueScratchXYZPPN = block.method_125(field_761, x + 1, y, z - 1);
			} else {
				aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
			}

			float aoLightValueScratchXYZNPP;
			if (aoGrassXYZCPP || aoGrassXYZNPC) {
				aoLightValueScratchXYZNPP = block.method_125(field_761, x - 1, y, z + 1);
			} else {
				aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
			}

			float aoLightValueScratchXYZPPP;
			if (aoGrassXYZCPP || aoGrassXYZPPC) {
				aoLightValueScratchXYZPPP = block.method_125(field_761, x + 1, y, z + 1);
			} else {
				aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
			}

			topRightLight = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + aoLightValueYPos) / 4;
			topLeftLight = (aoLightValueScratchYZPP + aoLightValueYPos + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4;
			bottomLeftLight = (aoLightValueYPos + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4;
			bottomRightLight = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + aoLightValueYPos + aoLightValueScratchYZPN) / 4;
		} else {
			topLeftLight = bottomLeftLight = bottomRightLight = topRightLight = aoLightValueYPos;
		}

		setColours(1F, topLeftLight, bottomLeftLight, bottomRightLight, topRightLight);
	}

	@Inject(method = "method_517", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_193;method_522(Lnet/minecraft/block/Block;DDDI)V", ordinal = 0))
	private void prepareNorth(@Slot(2) Block block, @Slot(3) int x, @Slot(4) int y, @Slot(5) int z, CallbackInfoReturnable<Boolean> call) {
		float aoLightValueZNeg = block.method_125(field_761, x, y, --z);

		float topLeftLight, bottomLeftLight, bottomRightLight, topRightLight;
		if (enableAO) {
			float aoLightValueScratchXZNN = block.method_125(field_761, x - 1, y, z);
			float aoLightValueScratchYZNN = block.method_125(field_761, x, y - 1, z);
			float aoLightValueScratchYZPN = block.method_125(field_761, x, y + 1, z);
			float aoLightValueScratchXZPN = block.method_125(field_761, x + 1, y, z);

			float aoLightValueScratchXYZNNN;
			if (aoGrassXYZNCN || aoGrassXYZCNN) {
				aoLightValueScratchXYZNNN = block.method_125(field_761, x - 1, y - 1, z);
			} else {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
			}

			float aoLightValueScratchXYZNPN;
			if (aoGrassXYZNCN || aoGrassXYZCPN) {
				aoLightValueScratchXYZNPN = block.method_125(field_761, x - 1, y + 1, z);
			} else {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
			}

			float aoLightValueScratchXYZPNN;
			if (aoGrassXYZPCN || aoGrassXYZCNN) {
				aoLightValueScratchXYZPNN = block.method_125(field_761, x + 1, y - 1, z);
			} else {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
			}

			float aoLightValueScratchXYZPPN;
			if (aoGrassXYZPCN || aoGrassXYZCPN) {
				aoLightValueScratchXYZPPN = block.method_125(field_761, x + 1, y + 1, z);
			} else {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
			}

			topLeftLight = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + aoLightValueZNeg + aoLightValueScratchYZPN) / 4;
			bottomLeftLight = (aoLightValueZNeg + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4;
			bottomRightLight = (aoLightValueScratchYZNN + aoLightValueZNeg + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4;
			topRightLight = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + aoLightValueZNeg) / 4;
		} else {
			topLeftLight = bottomLeftLight = bottomRightLight = topRightLight = aoLightValueZNeg;
		}

		setColours(0.8F, topLeftLight, bottomLeftLight, bottomRightLight, topRightLight);
	}

	@Inject(method = "method_517", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_193;method_523(Lnet/minecraft/block/Block;DDDI)V", ordinal = 0))
	private void prepareSouth(@Slot(2) Block block, @Slot(3) int x, @Slot(4) int y, @Slot(5) int z, CallbackInfoReturnable<Boolean> call) {
		float aoLightValueZPos = block.method_125(field_761, x, y, ++z);

		float topLeftLight, bottomLeftLight, bottomRightLight, topRightLight;
		if (enableAO) {
			float aoLightValueScratchXZNP = block.method_125(field_761, x - 1, y, z);
			float aoLightValueScratchXZPP = block.method_125(field_761, x + 1, y, z);
			float aoLightValueScratchYZNP = block.method_125(field_761, x, y - 1, z);
			float aoLightValueScratchYZPP = block.method_125(field_761, x, y + 1, z);

			float aoLightValueScratchXYZNNP;
			if (aoGrassXYZNCP || aoGrassXYZCNP) {
				aoLightValueScratchXYZNNP = block.method_125(field_761, x - 1, y - 1, z);
			} else {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
			}

			float aoLightValueScratchXYZNPP;
			if (aoGrassXYZNCP || aoGrassXYZCPP) {
				aoLightValueScratchXYZNPP = block.method_125(field_761, x - 1, y + 1, z);
			} else {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
			}

			float aoLightValueScratchXYZPNP;
			if (aoGrassXYZPCP || aoGrassXYZCNP) {
				aoLightValueScratchXYZPNP = block.method_125(field_761, x + 1, y - 1, z);
			} else {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
			}

			float aoLightValueScratchXYZPPP;
			if (aoGrassXYZPCP || aoGrassXYZCPP) {
				aoLightValueScratchXYZPPP = block.method_125(field_761, x + 1, y + 1, z);
			} else {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
			}

			topLeftLight = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + aoLightValueZPos + aoLightValueScratchYZPP) / 4;
			topRightLight = (aoLightValueZPos + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4;
			bottomRightLight = (aoLightValueScratchYZNP + aoLightValueZPos + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4;
			bottomLeftLight = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + aoLightValueZPos) / 4;
		} else {
			topLeftLight = bottomLeftLight = bottomRightLight = topRightLight = aoLightValueZPos;
		}

		setColours(0.8F, topLeftLight, bottomLeftLight, bottomRightLight, topRightLight);
	}

	@Inject(method = "method_517", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_193;method_524(Lnet/minecraft/block/Block;DDDI)V", ordinal = 0))
	private void prepareEast(@Slot(2) Block block, @Slot(3) int x, @Slot(4) int y, @Slot(5) int z, CallbackInfoReturnable<Boolean> call) {
		float aoLightValueXNeg = block.method_125(field_761, --x, y, z);

		float topLeftLight, bottomLeftLight, bottomRightLight, topRightLight;
		if (enableAO) {
			float aoLightValueScratchXYNN = block.method_125(field_761, x, y - 1, z);
			float aoLightValueScratchXZNN = block.method_125(field_761, x, y, z - 1);
			float aoLightValueScratchXZNP = block.method_125(field_761, x, y, z + 1);
			float aoLightValueScratchXYNP = block.method_125(field_761, x, y + 1, z);

			float aoLightValueScratchXYZNNN;
			if (aoGrassXYZNCN || aoGrassXYZNNC) {
				aoLightValueScratchXYZNNN = block.method_125(field_761, x, y - 1, z - 1);
			} else {
				aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
			}

			float aoLightValueScratchXYZNNP;
			if (aoGrassXYZNCP || aoGrassXYZNNC) {
				aoLightValueScratchXYZNNP = block.method_125(field_761, x, y - 1, z + 1);
			} else {
				aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
			}

			float aoLightValueScratchXYZNPN;
			if (aoGrassXYZNCN || aoGrassXYZNPC) {
				aoLightValueScratchXYZNPN = block.method_125(field_761, x, y + 1, z - 1);
			} else {
				aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
			}

			float aoLightValueScratchXYZNPP;
			if (aoGrassXYZNCP || aoGrassXYZNPC) {
				aoLightValueScratchXYZNPP = block.method_125(field_761, x, y + 1, z + 1);
			} else {
				aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
			}

			topRightLight = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + aoLightValueXNeg + aoLightValueScratchXZNP) / 4;
			topLeftLight = (aoLightValueXNeg + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4;
			bottomLeftLight = (aoLightValueScratchXZNN + aoLightValueXNeg + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4;
			bottomRightLight = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + aoLightValueXNeg) / 4;
		} else {
			topLeftLight = bottomLeftLight = bottomRightLight = topRightLight = aoLightValueXNeg;
		}

		setColours(0.6F, topLeftLight, bottomLeftLight, bottomRightLight, topRightLight);
	}

	@Inject(method = "method_517", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_193;method_525(Lnet/minecraft/block/Block;DDDI)V", ordinal = 0))
	private void prepareWest(@Slot(2) Block block, @Slot(3) int x, @Slot(4) int y, @Slot(5) int z, CallbackInfoReturnable<Boolean> call) {
		float aoLightValueXPos = block.method_125(field_761, ++x, y, z);

		float topLeftLight, bottomLeftLight, bottomRightLight, topRightLight;
		if (enableAO) {
			float aoLightValueScratchXYPN = block.method_125(field_761, x, y - 1, z);
			float aoLightValueScratchXZPN = block.method_125(field_761, x, y, z - 1);
			float aoLightValueScratchXZPP = block.method_125(field_761, x, y, z + 1);
			float aoLightValueScratchXYPP = block.method_125(field_761, x, y + 1, z);

			float aoLightValueScratchXYZPNN;
			if (aoGrassXYZPNC || aoGrassXYZPCN) {
				aoLightValueScratchXYZPNN = block.method_125(field_761, x, y - 1, z - 1);
			} else {
				aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
			}

			float aoLightValueScratchXYZPNP;
			if (aoGrassXYZPNC || aoGrassXYZPCP) {
				aoLightValueScratchXYZPNP = block.method_125(field_761, x, y - 1, z + 1);
			} else {
				aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
			}

			float aoLightValueScratchXYZPPN;
			if (aoGrassXYZPPC || aoGrassXYZPCN) {
				aoLightValueScratchXYZPPN = block.method_125(field_761, x, y + 1, z - 1);
			} else {
				aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
			}

			float aoLightValueScratchXYZPPP;
			if (aoGrassXYZPPC || aoGrassXYZPCP) {
				aoLightValueScratchXYZPPP = block.method_125(field_761, x, y + 1, z + 1);
			} else {
				aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
			}

			topLeftLight = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + aoLightValueXPos + aoLightValueScratchXZPP) / 4;
			topRightLight = (aoLightValueXPos + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4;
			bottomRightLight = (aoLightValueScratchXZPN + aoLightValueXPos + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4;
			bottomLeftLight = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + aoLightValueXPos) / 4;
		} else {
			topLeftLight = bottomLeftLight = bottomRightLight = topRightLight = aoLightValueXPos;
		}

		setColours(0.6F, topLeftLight, bottomLeftLight, bottomRightLight, topRightLight);
	}

	@Unique
	private void setColours(float initialShade, float topLeftLight, float bottomLeftLight, float bottomRightLight, float topRightLight) {
		colourRedTopLeft = colourRedBottomLeft = colourRedBottomRight = colourRedTopRight = initialShade;
		colourGreenTopLeft = colourGreenBottomLeft = colourGreenBottomRight = colourGreenTopRight = initialShade;
		colourBlueTopLeft = colourBlueBottomLeft = colourBlueBottomRight = colourBlueTopRight = initialShade;
		colourRedTopLeft *= topLeftLight;
		colourGreenTopLeft *= topLeftLight;
		colourBlueTopLeft *= topLeftLight;
		colourRedBottomLeft *= bottomLeftLight;
		colourGreenBottomLeft *= bottomLeftLight;
		colourBlueBottomLeft *= bottomLeftLight;
		colourRedBottomRight *= bottomRightLight;
		colourGreenBottomRight *= bottomRightLight;
		colourBlueBottomRight *= bottomRightLight;
		colourRedTopRight *= topRightLight;
		colourGreenTopRight *= topRightLight;
		colourBlueTopRight *= topRightLight;
	}

	@Inject(method = {"method_515", "method_520", "method_522", "method_523", "method_524", "method_525"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tesselator;method_481(DDDDD)V", ordinal = 0))
	private void colourTopLeft(CallbackInfo call) {
		if (enableAO) Tesselator.instance.method_482(colourRedTopLeft, colourGreenTopLeft, colourBlueTopLeft);
	}

	@Inject(method = {"method_515", "method_520", "method_522", "method_523", "method_524", "method_525"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tesselator;method_481(DDDDD)V", ordinal = 1))
	private void colourBottomLeft(CallbackInfo call) {
		if (enableAO) Tesselator.instance.method_482(colourRedBottomLeft, colourGreenBottomLeft, colourBlueBottomLeft);
	}

	@Inject(method = {"method_515", "method_520", "method_522", "method_523", "method_524", "method_525"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tesselator;method_481(DDDDD)V", ordinal = 2))
	private void colourBottomRight(CallbackInfo call) {
		if (enableAO) Tesselator.instance.method_482(colourRedBottomRight, colourGreenBottomRight, colourBlueBottomRight);
	}

	@Inject(method = {"method_515", "method_520", "method_522", "method_523", "method_524", "method_525"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tesselator;method_481(DDDDD)V", ordinal = 3))
	private void colourTopRight(CallbackInfo call) {
		if (enableAO) Tesselator.instance.method_482(colourRedTopRight, colourGreenTopRight, colourBlueTopRight);
	}

	@Inject(method = "method_517", at = @At(value = "RETURN", ordinal = 0))
	private void onBlockRender(CallbackInfoReturnable<Boolean> call) {
		enableAO = false;
	}
}