/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import static toughasnails.api.achievement.TANAchievements.*;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import toughasnails.api.TANBlocks;
import toughasnails.api.item.TANItems;

public class ModAchievements 
{
    public static final AchievementPage achievementPage = new AchievementPage("Tough As Nails");
    
    public static void init()
    {
        AchievementPage.registerAchievementPage(achievementPage);
        
        addAchievements();
    }
    
    private static void addAchievements()
    {
    	thirst_quencher = addAchievement("achievement.thirst_quencher", "thirst_quencher", 0, 0, new ItemStack(TANItems.canteen), null);
    	thirst_ender = addAchievement("achievement.thirst_ender", "thirst_ender", -2, -2, new ItemStack(TANItems.fruit_juice), thirst_quencher);
    	
        campfire_song = addAchievement("achievement.campfire_song", "campfire_song", 2, 1, new ItemStack(TANBlocks.campfire), thirst_quencher);
        life_or_death = addAchievement("achievement.life_or_death", "life_or_death", 1, 3, new ItemStack(TANItems.lifeblood_crystal), campfire_song);
        
        into_ice = addAchievement("achievement.into_ice", "into_ice", -3, 1, new ItemStack(TANItems.freeze_rod), thirst_quencher);
        hot_or_cold = addAchievement("achievement.hot_or_cold", "hot_or_cold", -4, -1, new ItemStack(TANItems.thermometer), into_ice);
        
        that_time_of_year = addAchievement("achievement.that_time_of_year", "that_time_of_year", 2, -1, new ItemStack(TANItems.season_clock), campfire_song);
        year_one = addAchievement("achievement.year_one", "year_one", 0, -3, new ItemStack(Items.CLOCK), that_time_of_year).setSpecial();
    }
    
    private static Achievement addAchievement(String unlocalizedName, String identifier, int column, int row, ItemStack iconStack, Achievement parent)
    {
        Achievement achievement = new Achievement(unlocalizedName, identifier, column, row, iconStack, parent);
        achievement.registerStat();
        achievementPage.getAchievements().add(achievement);
        return achievement;
    }
}
