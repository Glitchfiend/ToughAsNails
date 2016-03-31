/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.api.season;

public enum Season 
{
    SPRING, SUMMER, AUTUMN, WINTER;
    
    public static enum SubSeason
    {
        EARLY_SPRING(SPRING, 0x778087, 85, 0x6F818F, 85),
        MID_SPRING(SPRING, 0x6F818F, 0x5F849F),
        LATE_SPRING(SPRING, 0x678297, 0x3F89BF),
        EARLY_SUMMER(SUMMER, 0x73808B, 0x5F849F),
        MID_SUMMER(SUMMER, 0xFFFFFF, 0xFFFFFF),
        LATE_SUMMER(SUMMER, 0x877777, 0x9F5F5F),
        EARLY_AUTUMN(AUTUMN, 0x8F6F6F, 0xB74747),
        MID_AUTUMN(AUTUMN, 0x9F5F5F, 0xCF2F2F),
        LATE_AUTUMN(AUTUMN, 0xAF4F4F, 85, 0xBF3F3F, 85),
        EARLY_WINTER(WINTER, 0x9F5F5F, 60, 0xA75757, 60),
        MID_WINTER(WINTER, 0x8F6F6F, 45, 0x9F5F5F, 45),
        LATE_WINTER(WINTER, 0xFFFFFF, 60, 0x8F6F6F, 60);
        
        private Season season;
        private int grassOverlay;
        private int grassSaturation;
        private int foliageOverlay;
        private int foliageSaturation;
        
        private SubSeason(Season season, int grassColour, int grassSaturation, int foliageColour, int foliageSaturation)
        {
            this.season = season;
            this.grassOverlay = grassColour;
            this.grassSaturation = grassSaturation;
            this.foliageOverlay = foliageColour;
            this.foliageSaturation = foliageSaturation; 
        }
        
        private SubSeason(Season season, int grassColour, int foliageColour)
        {
            this(season, grassColour, -1, foliageColour, -1);
        }
        
        public Season getSeason()
        {
            return this.season;
        }
        
        public int getGrassOverlay()
        {
            return this.grassOverlay;
        }
        
        public int getGrassSaturation()
        {
            return this.grassSaturation;
        }
        
        public int getFoliageOverlay()
        {
            return this.foliageOverlay;
        }
        
        public int getFoliageSaturation()
        {
            return this.foliageSaturation;
        }
    }
}
