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
    private static final ResourceLocation POTIONS_LOCATION = new ResourceLocation("toughasnails:textures/potions/tanpotionfx.png");
    
    protected TANPotion(boolean isBadEffectIn, int liquidColorIn, int x, int y)
    {
        super(isBadEffectIn, liquidColorIn);
        this.setIconIndex(x, y);
    }

    // We handle status icon rendering ourselves
    @Override
    public boolean hasStatusIcon()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        mc.getTextureManager().bindTexture(POTIONS_LOCATION);
        int iconIndex = this.getStatusIconIndex();
        mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, 0 + iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
    {
        mc.getTextureManager().bindTexture(POTIONS_LOCATION);
        int iconIndex = this.getStatusIconIndex();
        mc.ingameGUI.drawTexturedModalRect(x + 3, y + 3, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
    }
}
