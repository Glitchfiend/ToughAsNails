/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import toughasnails.glitch.event.Event;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;

public class LevelRenderEvent extends Event
{
  private final Stage stage;
  private final LevelRenderer levelRenderer;
  private final PoseStack poseStack;
  private final Matrix4f projectionMatrix;
  private final int renderTick;
  private final float partialTick;
  private final Camera camera;
  private final Frustum frustum;

  public LevelRenderEvent(Stage stage, LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f projectionMatrix, int renderTick, float partialTick, Camera camera, Frustum frustum)
  {
    this.stage = stage;
    this.levelRenderer = levelRenderer;
    this.poseStack = poseStack;
    this.projectionMatrix = projectionMatrix;
    this.renderTick = renderTick;
    this.partialTick = partialTick;
    this.camera = camera;
    this.frustum = frustum;
  }

  public Stage getStage()
  {
    return this.stage;
  }

  public LevelRenderer getLevelRenderer()
  {
    return this.levelRenderer;
  }

  public PoseStack getPoseStack()
  {
    return this.poseStack;
  }

  public Matrix4f getProjectionMatrix()
  {
    return this.projectionMatrix;
  }

  public int getRenderTick()
  {
    return this.renderTick;
  }

  public float getPartialTick()
  {
    return this.partialTick;
  }

  public Camera getCamera()
  {
    return this.camera;
  }

  public Frustum getFrustum()
  {
    return this.frustum;
  }

  public enum Stage
  {
    AFTER_PARTICLES
  }
}
