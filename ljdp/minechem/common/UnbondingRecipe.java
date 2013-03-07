package ljdp.minechem.common;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class UnbondingRecipe {
	protected ArrayList<ItemStack> outputStacks;
	protected ItemStack inputStack;
	
	public UnbondingRecipe() {
	}
	
	public UnbondingRecipe(ItemStack inputStack, ArrayList outputStacks) {
		this.inputStack = inputStack;
		this.outputStacks = outputStacks;
	}
	
	public UnbondingRecipe(ArrayList outputStacks) {
		this.outputStacks = outputStacks;
	}
	
	public ArrayList<ItemStack> getOutput() {
		ArrayList<ItemStack> output = new ArrayList<ItemStack>();
		for(ItemStack itemstack : outputStacks) {
			output.add(itemstack.copy());
		}
		return output;
	}

}
