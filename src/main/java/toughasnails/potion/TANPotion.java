/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.potion;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TANPotion extends Potion
{
    private static final ResourceLocation POTIONS_LOCATION = new ResourceLocation("toughasnails:textures/potions/TANPotionFX.png");
    
    private final Predicate<PotionEffect> EQUALS_CURRENT_POTION = new Predicate<PotionEffect>()
    {
        @Override
        public boolean apply(PotionEffect input)
        {
            return input.getPotion() == TANPotion.this;
        }
    };
    
    private int iconIndex;
    
    protected TANPotion(boolean isBadEffectIn, int liquidColorIn, int x, int y)
    {
        super(isBadEffectIn, liquidColorIn);
        this.iconIndex = x + y * 8;
    }

    @Override
    public int getStatusIconIndex()
    {
        //Purposely use an index where there is nothing to render
        return -1;
    }
    
    //Why yes, we do have a status icon with a negative index. Is that a problem?
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon()
    {
        return true;
    }
    
    //Handle our own rendering, because apparently Vanilla doesn't really allow for potion effects
    //with custom texture sheets
    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_188408_i()
    {
        //Emulate the ordering of potion effect icons when rendering normally
        Collection<PotionEffect> activePotions = Ordering.natural().reverse().sortedCopy(Minecraft.getMinecraft().thePlayer.getActivePotionEffects());
        PotionEffect currentEffect = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(this);
        int index = Iterables.indexOf(activePotions, EQUALS_CURRENT_POTION); //Index of the icon for this potion effect
        
        GuiIngame ingameGui = Minecraft.getMinecraft().ingameGUI;
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int scaledWidth = resolution.getScaledWidth();
        scaledWidth = scaledWidth - 25 * (index + 1);

        int effectHeight = 27; //Always render on the second row
        int effectIconIndex = iconIndex; //Don't use the icon index provided by the potion, it's purposefully wrong to ignore Vanilla rendering
        float alpha = 1.0F;
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 1); //Translate in front of the existing background
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); //Reset colouring for the background
        ingameGui.drawTexturedModalRect(scaledWidth, effectHeight, 141, 166, 24, 24);
        Minecraft.getMinecraft().getTextureManager().bindTexture(POTIONS_LOCATION); //Bind the potions texture sheet ready for drawing 

        if (currentEffect.getDuration() <= 200)
        {
            int elapsedSeconds = 10 - currentEffect.getDuration() / 20;
            alpha = MathHelper.clamp_float((float)currentEffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)currentEffect.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp_float((float)elapsedSeconds / 10.0F * 0.25F, 0.0F, 0.25F);
        }

        //Flash semi-transparent as the duration gets low
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        ingameGui.drawTexturedModalRect(scaledWidth + 3, effectHeight + 3, effectIconIndex % 8 * 18, 198 + effectIconIndex / 8 * 18, 18, 18);
        GlStateManager.popMatrix();
        
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiContainer.inventoryBackground);
        
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        mc.getTextureManager().bindTexture(POTIONS_LOCATION);
        int effectIconIndex = iconIndex;
        //Draw the icon with its real icon index
        mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, 0 + effectIconIndex % 8 * 18, 198 + effectIconIndex / 8 * 18, 18, 18);
        mc.getTextureManager().bindTexture(GuiContainer.inventoryBackground);
    }
}
