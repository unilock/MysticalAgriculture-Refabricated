package com.alex.mysticalagriculture;

import com.alex.cucumber.util.Utils;
import com.alex.mysticalagriculture.client.EssenceVesselColorManager;
import com.alex.mysticalagriculture.client.ModelHandler;
import com.alex.mysticalagriculture.client.blockentity.*;
import com.alex.mysticalagriculture.client.handler.ColorHandler;
import com.alex.mysticalagriculture.client.handler.GuiOverlayHandler;
import com.alex.mysticalagriculture.client.screen.HarvesterScreen;
import com.alex.mysticalagriculture.client.screen.ReprocessorScreen;
import com.alex.mysticalagriculture.client.screen.SoulExtractorScreen;
import com.alex.mysticalagriculture.client.screen.TinkeringTableScreen;
import com.alex.mysticalagriculture.init.ModBlockEntities;
import com.alex.mysticalagriculture.init.ModBlocks;
import com.alex.mysticalagriculture.init.ModContainerTypes;
import com.alex.mysticalagriculture.init.ModItems;
import com.alex.mysticalagriculture.items.ExperienceCapsuleItem;
import com.alex.mysticalagriculture.items.SoulJarItem;
import com.alex.mysticalagriculture.items.tool.EssenceBowItem;
import com.alex.mysticalagriculture.items.tool.EssenceCrossbowItem;
import com.alex.mysticalagriculture.items.tool.EssenceFishingRodItem;
import com.alex.mysticalagriculture.util.RecipeIngredientCache;
import io.github.fabricators_of_create.porting_lib.util.FluidTextUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

import static com.alex.mysticalagriculture.handler.ExperienceCapsuleHandler.EXPERIENCE_CAPSULE_PICKUP;
import static com.alex.mysticalagriculture.util.RecipeIngredientCache.RELOAD_INGREDIENT_CACHE;

public class MysticalAgricultureClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(GuiOverlayHandler::setAltarOverlay);
        HudRenderCallback.EVENT.register(GuiOverlayHandler::setEssenceVesselOverlay);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(EssenceVesselColorManager.INSTANCE);

        ModelHandler.onRegisterAdditionalModels();
        ModelHandler.onModelBakingCompleted();

        ClientPlayNetworking.registerGlobalReceiver(EXPERIENCE_CAPSULE_PICKUP, (client, handler, buf, responseSender) -> client.execute(() -> {
            var player = client.player;

            if (player != null) {
                player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.1F, (Utils.RANDOM.nextFloat() - Utils.RANDOM.nextFloat()) * 0.35F + 0.9F);
            }
        }));

        ClientPlayNetworking.registerGlobalReceiver(RELOAD_INGREDIENT_CACHE, (client, handler, buffer, responseSender) -> {
            var caches = new HashMap<RecipeType<?>, Map<Item, List<Ingredient>>>();
            var types = buffer.readVarInt();

            for (var i = 0; i < types; i++) {
                var type = BuiltInRegistries.RECIPE_TYPE.get(buffer.readResourceLocation());
                var items = buffer.readVarInt();

                caches.put(type, new HashMap<>());

                for (var j = 0; j < items; j++) {
                    var item = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());
                    var ingredients = buffer.readVarInt();

                    for (var k = 0; k < ingredients; k++) {
                        var cache = caches.get(type).computeIfAbsent(item, l -> new ArrayList<>());

                        cache.add(Ingredient.fromNetwork(buffer));
                    }
                }
            }

            var validVesselItems = new HashSet<Item>();
            var items = buffer.readVarInt();

            for (var i = 0; i < items; i++) {
                var item = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());

                validVesselItems.add(item);
            }

            client.execute(() -> {
                RecipeIngredientCache.INSTANCE.setCaches(caches);
                RecipeIngredientCache.INSTANCE.setValidVesselItems(validVesselItems);
            });
        });

        BlockEntityRenderers.register(ModBlockEntities.INFUSION_PEDESTAL, InfusionPedestalRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.INFUSION_ALTAR, InfusionAltarRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.TINKERING_TABLE, TinkeringTableRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.AWAKENING_PEDESTAL, AwakeningPedestalRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.AWAKENING_ALTAR, AwakeningAltarRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.ESSENCE_VESSEL, EssenceVesselRenderer::new);

        ItemProperties.register(ModItems.EXPERIENCE_CAPSULE, new ResourceLocation("fill"), ExperienceCapsuleItem.getFillPropertyGetter());
        ItemProperties.register(ModItems.SOUL_JAR, new ResourceLocation("fill"), SoulJarItem.getFillPropertyGetter());
        ItemProperties.register(ModItems.INFERIUM_BOW, new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.INFERIUM_BOW, new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.INFERIUM_CROSSBOW, new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.INFERIUM_CROSSBOW, new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.INFERIUM_CROSSBOW, new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
        ItemProperties.register(ModItems.INFERIUM_CROSSBOW, new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
        ItemProperties.register(ModItems.INFERIUM_FISHING_ROD, new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
        ItemProperties.register(ModItems.PRUDENTIUM_BOW, new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.PRUDENTIUM_BOW, new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW, new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW, new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW, new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
        ItemProperties.register(ModItems.PRUDENTIUM_CROSSBOW, new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
        ItemProperties.register(ModItems.PRUDENTIUM_FISHING_ROD, new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
        ItemProperties.register(ModItems.TERTIUM_BOW, new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.TERTIUM_BOW, new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.TERTIUM_CROSSBOW, new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.TERTIUM_CROSSBOW, new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.TERTIUM_CROSSBOW, new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
        ItemProperties.register(ModItems.TERTIUM_CROSSBOW, new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
        ItemProperties.register(ModItems.TERTIUM_FISHING_ROD, new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
        ItemProperties.register(ModItems.IMPERIUM_BOW, new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.IMPERIUM_BOW, new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.IMPERIUM_CROSSBOW, new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.IMPERIUM_CROSSBOW, new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.IMPERIUM_CROSSBOW, new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
        ItemProperties.register(ModItems.IMPERIUM_CROSSBOW, new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
        ItemProperties.register(ModItems.IMPERIUM_FISHING_ROD, new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
        ItemProperties.register(ModItems.SUPREMIUM_BOW, new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.SUPREMIUM_BOW, new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW, new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW, new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW, new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
        ItemProperties.register(ModItems.SUPREMIUM_CROSSBOW, new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
        ItemProperties.register(ModItems.SUPREMIUM_FISHING_ROD, new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
        ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_BOW, new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_BOW, new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW, new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
        ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW, new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
        ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW, new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
        ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW, new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
        ItemProperties.register(ModItems.AWAKENED_SUPREMIUM_FISHING_ROD, new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());

        MenuScreens.register(ModContainerTypes.REPROCESSOR, ReprocessorScreen::new);
        MenuScreens.register(ModContainerTypes.TINKERING_TABLE, TinkeringTableScreen::new);
        MenuScreens.register(ModContainerTypes.SOUL_EXTRACTOR, SoulExtractorScreen::new);
        MenuScreens.register(ModContainerTypes.HARVESTER, HarvesterScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutoutMipped(), ModBlocks.INFERIUM_ORE, ModBlocks.DEEPSLATE_INFERIUM_ORE, ModBlocks.PROSPERITY_ORE, ModBlocks.DEEPSLATE_PROSPERITY_ORE, ModBlocks.SOULIUM_ORE);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), ModBlocks.SOUL_GLASS, ModBlocks.WITHERPROOF_GLASS, ModBlocks.ESSENCE_VESSEL);

        ColorHandler.onBlockColors();
        ColorHandler.onItemColors();
    }
}