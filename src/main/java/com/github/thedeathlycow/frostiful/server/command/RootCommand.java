package com.github.thedeathlycow.frostiful.server.command;

import com.github.thedeathlycow.frostiful.entity.component.FrostWandRootComponent;
import com.github.thedeathlycow.frostiful.registry.FComponents;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RootCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        var rootTarget =
                argument("targets", EntityArgumentType.entities())
                        .then(
                                argument("duration", IntegerArgumentType.integer(0))
                                        .executes(
                                                (context) -> {
                                                    return runRoot(context.getSource(),
                                                            EntityArgumentType.getEntities(context, "targets"),
                                                            IntegerArgumentType.getInteger(context, "duration"));
                                                }
                                        )
                        );


        dispatcher.register(
                literal("root").requires(src -> src.hasPermissionLevel(2))
                        .then(
                                rootTarget
                        )
        );
    }

    private static int runRoot(ServerCommandSource source, Collection<? extends Entity> targets, int duration) throws CommandSyntaxException {

        int sum = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                FrostWandRootComponent component = FComponents.FROST_WAND_ROOT_COMPONENT.get(livingEntity);
                component.setRootedTicks(duration);
                sum += duration;
            }
        }

        String key = "commands.frostiful.root.set.success." + (targets.size() == 1 ? "single" : "multiple");
        Text msg;
        if (targets.size() == 1) {
            msg = Text.translatable(key, targets.iterator().next().getDisplayName(), duration);
        } else {
            msg = Text.translatable(key, targets.size(), duration);
        }

        source.sendFeedback(() -> msg, true);

        return sum;
    }
}
