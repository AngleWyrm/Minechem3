   // I learned this method from IC2. All credits to the IC2 Team!
   package ljdp.minechem.common.items;
   import net.minecraft.item.EnumToolMaterial;
   import net.minecraft.item.ItemPickaxe;
   import net.minecraft.item.ItemStack;
   import net.minecraft.item.Item;
   import ljdp.minechem.common.ModMinechem;
   
   public class PhotonicInduction extends ItemPickaxe
    {
     public float efficiencyOnProperMaterial;
     1public ItemStack repairID = new ItemStack(Item.ingotIron);
     public PhotonicInduction (int id, int index, EnumToolMaterial enumtoolmaterial, float efficiency)
     {
     super(id, enumtoolmaterial);
     setItemName("minechem.hammer");
     setCreativeTab(ModMinechem.minechemTab);
     this.efficiencyOnProperMaterial = efficiency;
     setIconIndex(index);
     }
   
     public String getTextureFile()
     {
     return ModMinechem.proxy.ICONS_PNG;
     }
   
     public int getItemEnrchantability()
     {
     return 4;
     }

     public boolean getIsRepairable(ItemStack stack1, ItemStack stack2)
     {
     return (stack2 != null) && (stack2.itemID == this.repairID.itemID);
     }
   }