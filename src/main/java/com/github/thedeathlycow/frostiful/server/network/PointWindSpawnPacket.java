package com.github.thedeathlycow.frostiful.server.network;

import com.github.thedeathlycow.frostiful.Frostiful;
import com.github.thedeathlycow.frostiful.util.FPacketCodecs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public record PointWindSpawnPacket(
        Vec3d position
) implements CustomPayload {

    public static final CustomPayload.Id<PointWindSpawnPacket> PACKET_ID = new CustomPayload.Id<>(
            Frostiful.id("point_wind_spawn")
    );

    public static final PacketCodec<RegistryByteBuf, PointWindSpawnPacket> PACKET_CODEC = FPacketCodecs.VEC3D
            .xmap(PointWindSpawnPacket::new, PointWindSpawnPacket::position)
            .cast();

    public static void sendToNearbyPlayersFromServer(ServerWorld world, BlockPos spawnPos, Vec3d center) {
        var packet = new PointWindSpawnPacket(center);
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, spawnPos)) {
            ServerPlayNetworking.send(player, packet);
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
