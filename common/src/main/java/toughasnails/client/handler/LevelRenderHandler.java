/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import glitchcore.event.client.LevelRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.block.entity.ThermoregulatorBlockEntity;
import toughasnails.temperature.AreaFill;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LevelRenderHandler
{
    public static boolean enableDebug = false;

    public static void onLevelRender(LevelRenderEvent event) {
        Player player = Minecraft.getInstance().player;

        if (!enableDebug || player == null)
            return;

        Vec3 cameraPos = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lines());

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        getConnectedBlocks(player).forEach(block -> block.render(poseStack, vertexConsumer));
        poseStack.popPose();
    }

    private static final Set<ConnectedBlock> connectedBlocks = new HashSet<>();

    private static Set<ConnectedBlock> getConnectedBlocks(Player player)
    {
        Level level = player.level();

        // The optimisation is good enough for me, and this isn't really a feature for a broad audience
        // If that changes though, the below code is an option.
        //if (connectedBlocks.isEmpty() || level.getGameTime() % 20L == 0L)
        //{
            // Clear old connectedBlocks
            connectedBlocks.clear();
            populateConnectedBlocks(player);
        //}

        return connectedBlocks;
    }

    private static void populateConnectedBlocks(Player player)
    {
        Level level = player.level();
        BlockPos playerPos = player.blockPosition();

        // If the player's position is obstructed (for example, when mounted or inside a block), use the position above instead
        if (!level.isEmptyBlock(playerPos))
            playerPos = playerPos.above();

        Set<BlockPos> passablePositions = new HashSet<>();
        Set<BlockPos> heatingPositions = new HashSet<>();
        Set<BlockPos> coolingPositions = new HashSet<>();
        Set<BlockPos> thermoregulatorPositions = new HashSet<>();
        Set<BlockPos> blockingPositions = new HashSet<>();
        AreaFill.fill(level, playerPos, new AreaFill.PositionChecker() {
            @Override
            public void onSolid(Level level, AreaFill.FillPos pos)
            {
                BlockState state = level.getBlockState(pos.pos());

                if (TemperatureHelper.isHeatingBlock(state))
                {
                    heatingPositions.add(pos.pos());
                }
                else if (TemperatureHelper.isCoolingBlock(state))
                {
                    coolingPositions.add(pos.pos());
                }
                else blockingPositions.add(pos.pos());
            }

            @Override
            public void onPassable(Level level, AreaFill.FillPos pos)
            {
                passablePositions.add(pos.pos());
            }
        });

        // Add blocks from thermoregulators
        for (BlockPos pos : TemperatureHelper.getTemperatureData(player).getNearbyThermoregulators())
        {
            ThermoregulatorBlockEntity blockEntity = (ThermoregulatorBlockEntity)level.getBlockEntity(pos);

            if (blockEntity == null)
                continue;

            thermoregulatorPositions.addAll(blockEntity.getFilledBlocks());
        }

        connectBlocks(passablePositions, 170, 170, 170, 255); // Grey
        connectBlocks(heatingPositions, 255, 170, 0, 255); // Orange
        connectBlocks(coolingPositions, 85, 255, 255, 255); // Blue
        connectBlocks(thermoregulatorPositions, 255, 85, 255, 255); // Purple
        connectBlocks(blockingPositions, 255, 85, 85, 255); // Red
    }

    private static void connectBlocks(Set<BlockPos> positions, int r, int g, int b, int a)
    {
        // TODO: This could probably be optimised so that blocks opposite each other can set each other's faces, but this is good enough for now
        for (BlockPos pos : positions)
        {
            Set<Direction> connectedFaces = new HashSet<>();

            for (Direction dir : Direction.values())
            {
                if (positions.contains(pos.relative(dir)))
                    connectedFaces.add(dir);
            }

            if (!connectedFaces.containsAll(Arrays.stream(Direction.values()).toList()))
                connectedBlocks.add(new ConnectedBlock(pos, connectedFaces, (float)r / 255.0F, (float)g / 255.0F, (float)b / 255.0F, (float)a / 255.0F));
        }
    }

    private static class ConnectedBlock
    {
        private final BlockPos pos;
        private final Set<Direction> connectedFaces;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final float minX;
        private final float minY;
        private final float minZ;
        private final float maxX;
        private final float maxY;
        private final float maxZ;

        private ConnectedBlock(BlockPos pos, Set<Direction> connectedFaces, float r, float g, float b, float a)
        {
            this.pos = pos;
            this.connectedFaces = connectedFaces;

            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;

            // Offset by 0.0001 to prevent Z-fighting with the selection box
            this.minX = (float)pos.getX() + 0.0001F;
            this.minY = (float)pos.getY() + 0.0001F;
            this.minZ = (float)pos.getZ() + 0.0001F;
            this.maxX = (float)pos.getX() + 1 - 0.0001F;
            this.maxY = (float)pos.getY() + 1 - 0.0001F;
            this.maxZ = (float)pos.getZ() + 1 - 0.0001F;
        }

        public void render(PoseStack poseStack, VertexConsumer vertexConsumer)
        {
            Matrix4f poseMatrix = poseStack.last().pose();
            Matrix3f normalMatrix = poseStack.last().normal();

            // North face (towards -Z)
            drawLine(vertexConsumer, poseMatrix, normalMatrix, minX, minY, minZ, maxX, minY, minZ, Direction.NORTH, Direction.DOWN);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, minX, minY, minZ, minX, maxY, minZ, Direction.NORTH, Direction.WEST);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, maxX, minY, minZ, maxX, maxY, minZ, Direction.NORTH, Direction.EAST);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, maxX, maxY, minZ, minX, maxY, minZ, Direction.NORTH, Direction.UP);

            // South face (towards +Z)
            drawLine(vertexConsumer, poseMatrix, normalMatrix, minX, maxY, maxZ, minX, minY, maxZ, Direction.SOUTH, Direction.WEST);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, minX, minY, maxZ, maxX, minY, maxZ, Direction.SOUTH, Direction.DOWN);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, minX, maxY, maxZ, maxX, maxY, maxZ, Direction.SOUTH, Direction.UP);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, maxX, minY, maxZ, maxX, maxY, maxZ, Direction.SOUTH, Direction.EAST);

            // West face (towards -X)
            drawLine(vertexConsumer, poseMatrix, normalMatrix, minX, minY, minZ, minX, minY, maxZ, Direction.WEST, Direction.DOWN);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, minX, maxY, minZ, minX, maxY, maxZ, Direction.WEST, Direction.UP);

            // East face (towards +X)
            drawLine(vertexConsumer, poseMatrix, normalMatrix, maxX, minY, maxZ, maxX, minY, minZ, Direction.EAST, Direction.DOWN);
            drawLine(vertexConsumer, poseMatrix, normalMatrix, maxX, maxY, minZ, maxX, maxY, maxZ, Direction.EAST, Direction.UP);
        }

        public Set<Direction> getConnectedFaces()
        {
            return this.connectedFaces;
        }

        private void drawLine(VertexConsumer vertexConsumer, Matrix4f poseMatrix, Matrix3f normalMatrix, float fromX, float fromY, float fromZ, float toX, float toY, float toZ, Direction... relevantFaces)
        {
            // Don't draw this line if it forms part of a connected face
            if (Arrays.stream(relevantFaces).anyMatch(this.connectedFaces::contains))
                return;

            float normalX = Math.signum(fromX - toX);
            float normalY = Math.signum(fromY - toY);
            float normalZ = Math.signum(fromZ - toZ);

            vertexConsumer.vertex(poseMatrix, fromX, fromY, fromZ).color(r, g, b, a).normal(normalMatrix, normalX, normalY, normalZ).endVertex();
            vertexConsumer.vertex(poseMatrix, toX, toY, toZ).color(r, g, b, a).normal(normalMatrix, normalX, normalY, normalZ).endVertex();
        }
    }
}
