package com.alex.mysticalagriculture.items;

import com.alex.mysticalagriculture.zucchini.item.BaseReusableItem;
import net.minecraft.item.ItemStack;

public class InfusionCrystalItem extends BaseReusableItem {

    public InfusionCrystalItem() {
        super(1000);
    }

    @Override
    public int getMaxDamage() {
        return /*ModConfigs.INFUSION_CRYSTAL_USES.get()*/1000 - 1;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return false;
    }
}
