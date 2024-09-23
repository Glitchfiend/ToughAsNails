package toughasnails.fabric.client;

import java.util.function.BiConsumer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.InteractionResult;
import toughasnails.core.ToughAsNails;
import toughasnails.glitch.event.EventManager;
import toughasnails.glitch.event.client.ItemTooltipEvent;
import toughasnails.glitch.event.client.LevelRenderEvent;
import toughasnails.glitch.event.client.RegisterColorsEvent;
import toughasnails.glitch.event.client.RegisterParticleSpritesEvent;
import toughasnails.glitch.event.player.PlayerInteractEvent;

public class ToughAsNailsClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
      EventManager.fire(new ItemTooltipEvent(stack, lines));
    });

    UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
      var event = new PlayerInteractEvent.UseBlock(player, hand, hitResult);
      EventManager.fire(event);

      if (event.isCancelled())
        return event.getCancelResult().getResult();

      return InteractionResult.PASS;
    });

    WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
      EventManager.fire(new LevelRenderEvent(LevelRenderEvent.Stage.AFTER_PARTICLES, context.worldRenderer(), context.matrixStack(), context.projectionMatrix(), context.worldRenderer().ticks, context.tickDelta(), context.camera(), context.frustum()));
    });

    ToughAsNails.setupClient();

    //
    EventManager.fire(new RegisterColorsEvent.Block(ColorProviderRegistry.BLOCK::register));
    EventManager.fire(new RegisterColorsEvent.Item(ColorProviderRegistry.ITEM::register));

    BiConsumer<ParticleType<?>, SpriteParticleRegistration<?>> particleSpriteRegisterFunc = (type, registration) -> {
      ParticleFactoryRegistry.getInstance().register(type, provider -> (ParticleProvider)registration.create(provider));
    };
    EventManager.fire(new RegisterParticleSpritesEvent(particleSpriteRegisterFunc));
  }
}
