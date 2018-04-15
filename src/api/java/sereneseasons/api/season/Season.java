/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package sereneseasons.api.season;

public enum Season 
{
    SPRING, SUMMER, AUTUMN, WINTER;

    public enum SubSeason implements ISeasonColorProvider
    {
        EARLY_SPRING(SPRING, 0x778087, 0.85F, 0x6F818F, 0.85F),
        MID_SPRING(SPRING, 0x6F818F, 0x5F849F),
        LATE_SPRING(SPRING, 0x678297, 0x3F89BF),
        EARLY_SUMMER(SUMMER, 0x73808B, 0x5F849F),
        MID_SUMMER(SUMMER, 0xFFFFFF, 0xFFFFFF),
        LATE_SUMMER(SUMMER, 0x877777, 0x9F5F5F),
        EARLY_AUTUMN(AUTUMN, 0x8F6F6F, 0xC44040),
        MID_AUTUMN(AUTUMN, 0x9F5F5F, 0xEF2121),
        LATE_AUTUMN(AUTUMN, 0xAF4F4F, 0.85F, 0xDB3030, 0.85F),
        EARLY_WINTER(WINTER, 0xAF4F4F, 0.60F, 0xDB3030, 0.60F),
        MID_WINTER(WINTER, 0xAF4F4F, 0.45F, 0xDB3030, 0.45F),
        LATE_WINTER(WINTER, 0x8E8181, 0.60F, 0xA57070, 0.60F);
        
        private Season season;
        private int grassOverlay;
        private float grassSaturationMultiplier;
        private int foliageOverlay;
        private float foliageSaturationMultiplier;
        
        SubSeason(Season season, int grassColour, float grassSaturation, int foliageColour, float foliageSaturation)
        {
            this.season = season;
            this.grassOverlay = grassColour;
            this.grassSaturationMultiplier = grassSaturation;
            this.foliageOverlay = foliageColour;
            this.foliageSaturationMultiplier = foliageSaturation; 
        }
        
        SubSeason(Season season, int grassColour, int foliageColour)
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
        
        public float getGrassSaturationMultiplier()
        {
            return this.grassSaturationMultiplier;
        }
        
        public int getFoliageOverlay()
        {
            return this.foliageOverlay;
        }
        
        public float getFoliageSaturationMultiplier()
        {
            return this.foliageSaturationMultiplier;
        }
    }

    public enum TropicalSeason implements ISeasonColorProvider
    {
        EARLY_DRY(0xFFFFFF, 0xFFFFFF),
        MID_DRY(0xA58668, 0.8F, 0xB7867C, 0.95F),
        LATE_DRY(0x8E7B6D, 0.9F, 0xA08B86, 0.975F),
        EARLY_WET(0x758C8A, 0x728C91),
        MID_WET(0x548384, 0x2498AE),
        LATE_WET(0x658989, 0x4E8893);

        private int grassOverlay;
        private float grassSaturationMultiplier;
        private int foliageOverlay;
        private float foliageSaturationMultiplier;

        TropicalSeason(int grassColour, float grassSaturation, int foliageColour, float foliageSaturation)
        {
            this.grassOverlay = grassColour;
            this.grassSaturationMultiplier = grassSaturation;
            this.foliageOverlay = foliageColour;
            this.foliageSaturationMultiplier = foliageSaturation;
        }

        TropicalSeason(int grassColour, int foliageColour)
        {
            this(grassColour, -1, foliageColour, -1);
        }

        public int getGrassOverlay()
        {
            return this.grassOverlay;
        }

        public float getGrassSaturationMultiplier()
        {
            return this.grassSaturationMultiplier;
        }

        public int getFoliageOverlay()
        {
            return this.foliageOverlay;
        }

        public float getFoliageSaturationMultiplier()
        {
            return this.foliageSaturationMultiplier;
        }
    }
}
