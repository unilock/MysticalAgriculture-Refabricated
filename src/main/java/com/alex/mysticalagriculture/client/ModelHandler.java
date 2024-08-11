package com.alex.mysticalagriculture.client;

import com.alex.mysticalagriculture.MysticalAgriculture;
import com.alex.mysticalagriculture.api.crop.CropTextures;
import com.alex.mysticalagriculture.config.ModConfigs;
import com.alex.mysticalagriculture.registry.CropRegistry;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Objects;
import java.util.stream.IntStream;

public class ModelHandler {
    private static final ResourceLocation MISSING_NO = new ResourceLocation("minecraft", "missingno");

    public static void onRegisterAdditionalModels() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            if (!ModConfigs.ANIMATED_GROWTH_ACCELERATORS.get()) {
                for (var type : new String[] { "block", "item" }) {
                    for (var tier : new String[] { "inferium", "prudentium", "tertium", "imperium", "supremium" }) {
                        out.accept(new ResourceLocation(MysticalAgriculture.MOD_ID, String.format("%s/%s_growth_accelerator_static", type, tier)));
                    }
                }
            }
            for (int i = 0; i < 8; i++) {
                out.accept(new ResourceLocation(MysticalAgriculture.MOD_ID, "block/mystical_resource_crop_" + i));
                out.accept(new ResourceLocation(MysticalAgriculture.MOD_ID, "block/mystical_mob_crop_" + i));
            }

            for (var type : CropRegistry.getInstance().getTypes()) {
                out.accept(new ResourceLocation(CropTextures.FLOWER_INGOT_BLANK + "_" + type.getName()));
                out.accept(new ResourceLocation(CropTextures.FLOWER_ROCK_BLANK + "_" + type.getName()));
                out.accept(new ResourceLocation(CropTextures.FLOWER_DUST_BLANK + "_" + type.getName()));
                out.accept(new ResourceLocation(CropTextures.FLOWER_FACE_BLANK + "_" + type.getName()));
            }

            out.accept(CropTextures.ESSENCE_INGOT_BLANK);
            out.accept(CropTextures.ESSENCE_ROCK_BLANK);
            out.accept(CropTextures.ESSENCE_DUST_BLANK);
            out.accept(CropTextures.ESSENCE_GEM_BLANK);
            out.accept(CropTextures.ESSENCE_TALL_GEM_BLANK);
            out.accept(CropTextures.ESSENCE_DIAMOND_BLANK);
            out.accept(CropTextures.ESSENCE_QUARTZ_BLANK);
            out.accept(CropTextures.ESSENCE_FLAME_BLANK);
            out.accept(CropTextures.ESSENCE_ROD_BLANK);

            out.accept(CropTextures.SEED_BLANK);
        });
    }

    // TODO: this may not work well
    //       (ResourceLocation and ModelResourceLocation may be reversed?)
    public static void onModelBakingCompleted() {
        ModelLoadingPlugin.register(ctx -> {
            if (!ModConfigs.ANIMATED_GROWTH_ACCELERATORS.get()) {
                for (var tier : new String[] { "inferium", "prudentium", "tertium", "imperium", "supremium" }) {
                    var loc = tier+"_growth_accelerator";
                    ctx.modifyModelAfterBake().register((model, context) -> {
                        if (Objects.equals(context.id(), new ResourceLocation(MysticalAgriculture.MOD_ID, "block/" + loc + "_static"))) {
                            return context.baker().bake(new ModelResourceLocation(MysticalAgriculture.MOD_ID, loc, ""), context.settings());
                        }
                        if (Objects.equals(context.id(), new ResourceLocation(MysticalAgriculture.MOD_ID, "item/" + loc + "_static"))) {
                            return context.baker().bake(new ModelResourceLocation(MysticalAgriculture.MOD_ID, loc, "inventory"), context.settings());
                        }
                        return model;
                    });
                }
            }

            if (!ModConfigs.ANIMATED_GROWTH_ACCELERATORS.get()) {
                for (var tier : new String[] { "inferium", "prudentium", "tertium", "imperium", "supremium" }) {
                    var loc = tier+"_growth_accelerator";
                    ctx.modifyModelAfterBake().register((model, context) -> {
                        if (Objects.equals(context.id(), new ResourceLocation(MysticalAgriculture.MOD_ID, "block/" + loc + "_static"))) {
                            return context.baker().bake(new ModelResourceLocation(MysticalAgriculture.MOD_ID, loc, ""), context.settings());
                        }
                        if (Objects.equals(context.id(), new ResourceLocation(MysticalAgriculture.MOD_ID, "item/" + loc + "_static"))) {
                            return context.baker().bake(new ModelResourceLocation(MysticalAgriculture.MOD_ID, loc, "inventory"), context.settings());
                        }
                        return model;
                    });
                }
            }

            ctx.modifyModelAfterBake().register((model, context) -> {
                var cropModels = new HashMap<ResourceLocation, ResourceLocation[]>();

                for (var cropType : CropRegistry.getInstance().getTypes()) {
                    cropModels.put(cropType.getId(), IntStream.range(0, 7)
                            .mapToObj(i -> new ResourceLocation(cropType.getStemModel() + "_" + i))
                            .toArray(ResourceLocation[]::new));
                }

                for (var crop : CropRegistry.getInstance().getCrops()) {
                    var textures = crop.getTextures();
                    var crops = crop.getCropBlock();
                    var cropId = BuiltInRegistries.BLOCK.getKey(crops);

                    if (cropId != null) {
                        for (int i = 0; i < 7; i++) {
                            var location = new ModelResourceLocation(cropId, "age=" + i);

                            if (model == null || model.getParticleIcon().contents().name().equals(MISSING_NO)) {
                                if (Objects.equals(context.id(), location)) {
                                    var type = crop.getType().getId();
                                    return context.baker().bake(cropModels.get(type)[i], context.settings());
                                }
                            }
                        }

                        var location = new ModelResourceLocation(cropId, "age=7");

                        if (model == null || model.getParticleIcon().contents().name().equals(MISSING_NO)) {
                            if (Objects.equals(context.id(), location)) {
                                var flower = textures.getFlowerTexture();
                                var type = crop.getType().getId();
                                var path = new ResourceLocation(type.getNamespace(), flower.getPath() + "_" + type.getPath());

                                return context.baker().bake(path, context.settings()); // TODO: this one may not be reversed?
                            }
                        }
                    }

                    var essence = crop.getEssenceItem();
                    var essenceId = BuiltInRegistries.ITEM.getKey(essence);

                    if (essenceId != null) {
                        var location = new ModelResourceLocation(essenceId, "inventory");

                        if (model == null || model.getParticleIcon().contents().name().equals(MISSING_NO)) {
                            if (Objects.equals(context.id(), location)) {
                                var texture = textures.getEssenceTexture();

                                return context.baker().bake(texture, context.settings()); // TODO: this one may not be reversed?
                            }
                        }
                    }

                    var seeds = crop.getSeedsItem();
                    var seedsId = BuiltInRegistries.ITEM.getKey(seeds);

                    if (seedsId != null) {
                        var location = new ModelResourceLocation(seedsId, "inventory");

                        if (model == null || model.getParticleIcon().contents().name().equals(MISSING_NO)) {
                            if (Objects.equals(context.id(), location)) {
                                var texture = textures.getSeedTexture();

                                return context.baker().bake(texture, context.settings()); // TODO: this one may not be reversed?
                            }
                        }
                    }
                }
                return model;
            });
        });
    }
}
