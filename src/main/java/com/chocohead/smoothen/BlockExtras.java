package com.chocohead.smoothen;

import net.minecraft.block.Block;

public class BlockExtras {
	public static final boolean[] canBlockGrass = new boolean[Block.BLOCKS.length];
	static {
		canBlockGrass[0] = true;
	}
}