   // I learned this method from IC2. All credits to the IC2 Team!
   // public static final EnumToolMaterial IRON
   package ljdp.minechem.common.items;
   import net.minecraft.item.EnumToolMaterial;
   import net.minecraft.item.ItemPickaxe;
   import net.minecraft.item.ItemStack;
   import net.minecraft.item.Item;
   import ljdp.minechem.common.ModMinechem;
   
   public class PhotonicInduction extends ItemPickaxe
   {
     public float efficiencyOnProperMaterial;
	 public ItemStack repairID = new ItemStack(Item.ingotIron);
     public PhotonicInduction (int id, int index, EnumToolMaterial enumtoolmaterial, float efficiency)
     {
     super(id, enumtoolmaterial);
     this.efficiencyOnProperMaterial = efficiency;
     setIconIndex(index);
     this.repairMaterial = repairID;
     }
   
     public String getTextureFile()
     {
     return ModMinechem.proxy.ITEMS_PNG;
     }
   
     public int getItemEnrchantability()
     {
     return 8;
     }
   
     public boolean getIsRepairable(ItemStack stack1, ItemStack stack2)
     {
     return (stack2 != null) && (stack2.itemID == this.repairID.itemID);
     }
   }