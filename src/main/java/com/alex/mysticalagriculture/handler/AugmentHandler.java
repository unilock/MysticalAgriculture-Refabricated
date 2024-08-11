package com.alex.mysticalagriculture.handler;

import com.alex.mysticalagriculture.api.lib.AbilityCache;
import com.alex.mysticalagriculture.api.util.AugmentUtils;
import io.github.fabricators_of_create.porting_lib.entity.events.LivingEntityEvents;
import net.minecraft.world.entity.player.Player;

public class AugmentHandler {
    public static final AbilityCache ABILITY_CACHE = new AbilityCache();

    public static void onLivingFall(LivingEntityEvents.Fall.FallEvent event)
    {
        if (event.getEntity() instanceof Player player) {
            var world = player.getCommandSenderWorld();

            AugmentUtils.getArmorAugments(player).forEach(a -> {
                a.onPlayerFall(world, player, event);
            });
        }
    }
}
