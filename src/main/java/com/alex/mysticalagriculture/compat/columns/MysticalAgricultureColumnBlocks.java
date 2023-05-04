package com.alex.mysticalagriculture.compat.columns;

import com.alex.mysticalagriculture.init.Blocks;
import io.github.haykam821.columns.block.ColumnBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.alex.mysticalagriculture.MysticalAgriculture.MOD_ID;

public final class MysticalAgricultureColumnBlocks {
    private MysticalAgricultureColumnBlocks() {
        return;
    }

    private static void registerColumnBlockAndItem(String path, Block base) {
        Identifier id = new Identifier(MOD_ID, path);

        Block block = new ColumnBlock(FabricBlockSettings.copy(base));
        Registry.register(Registries.BLOCK, id, block);

        Item item = new BlockItem(block, new Item.Settings());
        Registry.register(Registries.ITEM, id, item);
    }

    public static void init() {
        registerColumnBlockAndItem("soulstone_cobble_column", Blocks.SOULSTONE_COBBLE);
        registerColumnBlockAndItem("soulstone_bricks_column", Blocks.SOULSTONE_BRICKS);
    }
}
