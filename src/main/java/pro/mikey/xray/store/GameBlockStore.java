package pro.mikey.xray.store;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import pro.mikey.xray.utils.BlockData;
import pro.mikey.xray.xray.Controller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Objects;

public class GameBlockStore {

    private ArrayList<BlockWithItemStack> store = new ArrayList<>();

    /**
     * This method is used to fill the store as we do not intend to update this after
     * it has been populated, it's a singleton by nature but we still need some
     * amount of control over when it is populated.
     */
    public void populate()
    {
        // Avoid doing the logic again unless repopulate is called
        if( this.store.size() != 0 )
            return;

        for ( Item item : ForgeRegistries.ITEMS ) {
            if( !(item instanceof net.minecraft.world.item.BlockItem) )
                continue;

            Block block = Block.byItem(item);
            if ( item == Items.AIR || block == Blocks.AIR || Controller.blackList.contains(block) )
                continue; // avoids troubles

            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
            String modId = key.getNamespace();
            String blockName = Component.translatable(block.getDescriptionId()).toString();
            ItemStack itemStack = new ItemStack(item);
            store.add(new BlockWithItemStack(block, itemStack, key, blockName, modId));
        }
    }

    public void repopulate()
    {
        this.store.clear();
        this.populate();
    }

    public ArrayList<BlockWithItemStack> getStore() {
        return this.store;
    }

    public static final class BlockWithItemStack {
       private String blockName;
       private String modId;
       private Block block;
       private ItemStack itemStack;
       private ResourceLocation key;
        public BlockWithItemStack(Block block, ItemStack itemStack, ResourceLocation key,String blockName, String modId) {
            this.blockName = blockName;
            this.modId = modId;
            this.block = block;
            this.itemStack = itemStack;
            this.key = key;
        }
        public String getI18nNmae(){
            return this.itemStack.getHoverName().getString();
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public String getBlockName(){
            return this.blockName;
        }

        public String getModId() {
            return modId;
        }

        public Block getBlock() {
            return block;
        }

        public ResourceLocation getKey() {
            return key;
        }
    }
}
