package com.github.thedeathlycow.frostiful.client;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.config.FrostifulConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class FrozenHeartsOverlay {

    public static final Identifier HEART_OVERLAY_TEXTURE = Frostiful.id("textures/gui/cold_heart_overlay.png");

    private static final int TEXTURE_WIDTH = 18;

    private static final int TEXTURE_HEIGHT = 10;

    public static void afterHealthBar(
            DrawContext context,
            PlayerEntity player,
            Vector2i[] heartPositions,
            int displayHealth,
            int maxDisplayHealth
    ) {
        FrostifulConfig config = Frostiful.getConfig();
        if (!config.clientConfig.doColdHeartOverlay() || player.thermoo$isWarm()) {
            return;
        }

        int frozenHealthPoints = getNumColdPoints(player, maxDisplayHealth);
        int frozenHealthHearts = getNumColdHeartsFromPoints(frozenHealthPoints);
        for (int i = 0; i < frozenHealthHearts; i++) {
            Vector2i pos = heartPositions[i];
            if (pos == null) {
                continue;
            }
            // is half heart if this is the last heart being rendered and we have an odd
            // number of frozen health points
            boolean isHalfHeart = i + 1 >= frozenHealthHearts && (frozenHealthPoints & 1) == 1; // is odd check

            int u = isHalfHeart ? 9 : 0;
            context.drawTexture(
                    HEART_OVERLAY_TEXTURE,
                    pos.x, pos.y,
                    u, 0,
                    9, 10,
                    TEXTURE_WIDTH, TEXTURE_HEIGHT
            );
        }
    }

    public static void afterMountHealthBar(
            DrawContext context,
            PlayerEntity player,
            LivingEntity mount,
            Vector2i[] mountHeartPositions,
            int displayMountHealth,
            int maxDisplayMountHealth
    ) {
        FrostifulConfig config = Frostiful.getConfig();
        if (!config.clientConfig.doColdHeartOverlay() || mount.thermoo$isWarm()) {
            return;
        }

        int frozenHealthPoints = getNumColdPoints(mount, maxDisplayMountHealth);
        int frozenHealthHearts = getNumColdHeartsFromPoints(frozenHealthPoints);
        for (int i = 0; i < frozenHealthHearts; i++) {
            Vector2i pos = mountHeartPositions[i];
            if (pos == null) {
                continue;
            }
            boolean isHalfHeart = i + 1 >= frozenHealthHearts && (frozenHealthPoints & 1) == 1; // is odd check

            if (isHalfHeart) {
                // flips the half heart around, since animal hearts are backwards
                context.drawTexture(
                        HEART_OVERLAY_TEXTURE,
                        pos.x + 4, pos.y,
                        4, 0,
                        5, 10,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            } else {
                context.drawTexture(
                        HEART_OVERLAY_TEXTURE,
                        pos.x, pos.y,
                        0, 0,
                        9, 10,
                        TEXTURE_WIDTH, TEXTURE_HEIGHT
                );
            }
        }
    }

    private static int getNumColdPoints(@NotNull LivingEntity entity, int maxDisplayHealth) {
        float freezingProgress = -entity.thermoo$getTemperatureScale();
        return Math.round(freezingProgress * maxDisplayHealth);
    }

    private static int getNumColdHeartsFromPoints(int frozenHealthPoints) {
        // number of whole hearts
        return MathHelper.ceil(frozenHealthPoints / 2.0f);
    }

    private FrozenHeartsOverlay() {

    }
}
