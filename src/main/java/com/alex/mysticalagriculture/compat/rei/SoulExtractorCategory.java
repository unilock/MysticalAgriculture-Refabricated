package com.alex.mysticalagriculture.compat.rei;

import com.alex.mysticalagriculture.crafting.recipe.ReprocessorRecipe;
import com.alex.mysticalagriculture.crafting.recipe.SoulExtractionRecipe;
import com.alex.mysticalagriculture.init.Blocks;
import com.alex.mysticalagriculture.zucchini.util.Localizable;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Arrow;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alex.mysticalagriculture.MysticalAgriculture.MOD_ID;

public class SoulExtractorCategory implements DisplayCategory<SoulExtractorCategory.RecipeDisplay> {

    public static CategoryIdentifier<SoulExtractorCategory.RecipeDisplay> SOUL_EXTRACTOR = CategoryIdentifier.of(new Identifier(MOD_ID, "soul_extractor"));

    @Override
    public CategoryIdentifier<? extends RecipeDisplay> getCategoryIdentifier() {
        return SOUL_EXTRACTOR;
    }

    @Override
    public Text getTitle() {
        return Localizable.of("rei.category.mysticalagriculture.soul_extractor").build();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.SOUL_EXTRACTOR);
    }

    @Override
    public List<Widget> setupDisplay(RecipeDisplay recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createCategoryBase(bounds));

        Slot input = Widgets.createSlot(new Point(bounds.x + 0 + 4 + 1, bounds.y + 4 + 4 + 1));
        input.entries(recipeDisplay.getInputEntries().get(0));
        widgets.add(input);

        Arrow arrow = Widgets.createArrow(new Point(bounds.x + 24 + 4, bounds.y + 4 + 4));
        arrow.animationDurationMS(10000);
        widgets.add(arrow);

        Widget outputBackground = Widgets.createResultSlotBackground(new Point(bounds.x + 56 + 4 + 5, bounds.y + 0 + 4 + 5));
        widgets.add(outputBackground);

        Slot output = Widgets.createSlot(new Point(bounds.x + 56 + 4 + 5, bounds.y + 0 + 4 + 5));
        output.entries(recipeDisplay.getOutputEntries().get(0));
        output.disableBackground();
        widgets.add(output);


        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 26 + 8;
    }

    @Override
    public int getDisplayWidth(RecipeDisplay display) {
        return 82 + 8;
    }

    record RecipeDisplay(SoulExtractionRecipe recipe) implements Display {
        @Override
        public List<EntryIngredient> getInputEntries() {
            return EntryIngredients.ofIngredients(recipe.getIngredients());
        }

        @Override
        public List<EntryIngredient> getOutputEntries() {
            return Collections.singletonList(EntryIngredients.of(recipe.getOutput(BasicDisplay.registryAccess())));
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return SOUL_EXTRACTOR;
        }
    }
}
