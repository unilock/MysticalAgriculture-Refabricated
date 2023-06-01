package com.alex.mysticalagriculture.items;

import com.alex.mysticalagriculture.api.tinkering.Augment;
import com.alex.mysticalagriculture.api.tinkering.AugmentProvider;
import com.alex.mysticalagriculture.api.tinkering.AugmentType;
import com.alex.mysticalagriculture.lib.ModTooltips;
import com.alex.mysticalagriculture.cucumber.iface.Enableable;
import com.alex.mysticalagriculture.cucumber.item.BaseItem;
import com.alex.mysticalagriculture.cucumber.lib.Colors;
import com.alex.mysticalagriculture.cucumber.util.Localizable;
import com.google.common.base.Function;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class AugmentItem extends BaseItem implements AugmentProvider, Enableable {
    private final Augment augment;

    public AugmentItem(Augment augment, Function<Settings, Settings> settings) {
        super(settings);
        this.augment = augment;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Localizable.of("item.mysticalagriculture.augment").args(this.augment.getDisplayName()).build();
    }

    @Override
    public Text getName() {
        return this.getName(ItemStack.EMPTY);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return this.augment.hasEffect();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(ModTooltips.getTooltipForTier(this.augment.getTier()));
        tooltip.add(Text.literal(Colors.GRAY + this.augment.getAugmentTypes()
                .stream()
                .map(AugmentType::getDisplayName)
                .map(Text::getString)
                .collect(Collectors.joining(", "))
        ));
    }

    @Override
    public Augment getAugment() {
        return this.augment;
    }

    @Override
    public boolean isEnabled() {
        return this.augment.isEnabled();
    }
}
