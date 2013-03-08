   // I learned this method from IC2. All credits to the IC2 Team!
   // public static final EnumToolMaterial IRON
   package ljdp.minechem.common;
   import net.minecraft.item.EnumToolMaterial;
   import net.minecraft.item.ItemPickaxe;
   import net.minecraft.item.ItemStack;
   import ljdp.minechem.common.ModMinechem;
   
   public class PhotonicInduction extends ItemPickaxe
   {
     private final ItemStack repairMaterial;
     public float efficiencyOnProperMaterial;
   
     public PhotonicInduction (int id, int index, EnumToolMaterial enumtoolmaterial, float efficiency, ItemStack repairMaterial)
     {
     super(id, enumtoolmaterial);
     this.efficiencyOnProperMaterial = efficiency;
     setIconIndex(index);
     this.repairMaterial = repairMaterial;
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
     return (stack2 != null) && (stack2.itemID == this.repairMaterial.itemID);
     }
   }