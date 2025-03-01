package com.alex.mysticalagriculture.init;

import com.alex.mysticalagriculture.blockentities.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static BlockEntityType<EssenceFurnaceBlockEntity.Inferium> INFERIUM_FURNACE;
    public static BlockEntityType<EssenceFurnaceBlockEntity.Prudentium> PRUDENTIUM_FURNACE;
    public static BlockEntityType<EssenceFurnaceBlockEntity.Tertium> TERTIUM_FURNACE;
    public static BlockEntityType<EssenceFurnaceBlockEntity.Imperium> IMPERIUM_FURNACE;
    public static BlockEntityType<EssenceFurnaceBlockEntity.Supremium> SUPREMIUM_FURNACE;
    public static BlockEntityType<EssenceFurnaceBlockEntity.AwakenedSupremium> AWAKENED_SUPREMIUM_FURNACE;
    public static BlockEntityType<InfusionPedestalBlockEntity> INFUSION_PEDESTAL;
    public static BlockEntityType<InfusionAltarBlockEntity> INFUSION_ALTAR;
    public static BlockEntityType<AwakeningPedestalBlockEntity> AWAKENING_PEDESTAL;
    public static BlockEntityType<AwakeningAltarBlockEntity> AWAKENING_ALTAR;
    public static BlockEntityType<EssenceVesselBlockEntity> ESSENCE_VESSEL;
    public static BlockEntityType<TinkeringTableBlockEntity> TINKERING_TABLE;
    public static BlockEntityType<ReprocessorBlockEntity> REPROCESSOR;
    public static BlockEntityType<SoulExtractorBlockEntity> SOUL_EXTRACTOR;
    public static BlockEntityType<HarvesterBlockEntity> HARVESTER;

    public static void registerBlockEntities() {
        INFERIUM_FURNACE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:inferium_furnace", BlockEntityType.Builder.of(EssenceFurnaceBlockEntity.Inferium::new, ModBlocks.INFERIUM_FURNACE).build(null));
        PRUDENTIUM_FURNACE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:prudentium_furnace", BlockEntityType.Builder.of(EssenceFurnaceBlockEntity.Prudentium::new, ModBlocks.PRUDENTIUM_FURNACE).build(null));
        TERTIUM_FURNACE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:tertium_furnace", BlockEntityType.Builder.of(EssenceFurnaceBlockEntity.Tertium::new, ModBlocks.TERTIUM_FURNACE).build(null));
        IMPERIUM_FURNACE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:imperium_furnace", BlockEntityType.Builder.of(EssenceFurnaceBlockEntity.Imperium::new, ModBlocks.IMPERIUM_FURNACE).build(null));
        SUPREMIUM_FURNACE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:supremium_furnace", BlockEntityType.Builder.of(EssenceFurnaceBlockEntity.Supremium::new, ModBlocks.SUPREMIUM_FURNACE).build(null));
        AWAKENED_SUPREMIUM_FURNACE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:awakened_supremium_furnace", BlockEntityType.Builder.of(EssenceFurnaceBlockEntity.AwakenedSupremium::new, ModBlocks.AWAKENED_SUPREMIUM_FURNACE).build(null));
        INFUSION_PEDESTAL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:infusion_pedestal", BlockEntityType.Builder.of(InfusionPedestalBlockEntity::new, ModBlocks.INFUSION_PEDESTAL).build(null));
        INFUSION_ALTAR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:infusion_altar_block", BlockEntityType.Builder.of(InfusionAltarBlockEntity::new, ModBlocks.INFUSION_ALTAR).build(null));
        AWAKENING_PEDESTAL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:awakening_pedestal", BlockEntityType.Builder.of(AwakeningPedestalBlockEntity::new, ModBlocks.AWAKENING_PEDESTAL).build(null));
        AWAKENING_ALTAR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:awakening_altar_block", BlockEntityType.Builder.of(AwakeningAltarBlockEntity::new, ModBlocks.AWAKENING_ALTAR).build(null));
        ESSENCE_VESSEL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:essence_vessel", BlockEntityType.Builder.of(EssenceVesselBlockEntity::new, ModBlocks.ESSENCE_VESSEL).build(null));
        TINKERING_TABLE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:tinkering_table", BlockEntityType.Builder.of(TinkeringTableBlockEntity::new, ModBlocks.TINKERING_TABLE).build(null));
        REPROCESSOR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:seed_reprocessor", BlockEntityType.Builder.of(ReprocessorBlockEntity::new, ModBlocks.REPROCESSOR).build(null));
        SOUL_EXTRACTOR = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:soul_extractor", BlockEntityType.Builder.of(SoulExtractorBlockEntity::new, ModBlocks.SOUL_EXTRACTOR).build(null));
        HARVESTER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, "mysticalagriculture:harvester", BlockEntityType.Builder.of(HarvesterBlockEntity::new, ModBlocks.HARVESTER).build(null));
    }
}
