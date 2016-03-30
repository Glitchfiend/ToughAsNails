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
        EARLY_SPRING(SPRING),
        MID_SPRING(SPRING),
        LATE_SPRING(SPRING),
        EARLY_SUMMER(SUMMER),
        MID_SUMMER(SUMMER),
        LATE_SUMMER(SUMMER),
        EARLY_AUTUMN(AUTUMN),
        MID_AUTUMN(AUTUMN),
        LATE_AUTUMN(AUTUMN),
        EARLY_WINTER(WINTER),
        MID_WINTER(WINTER),
        LATE_WINTER(WINTER);
        
        private Season season;
        
        private SubSeason(Season season)
        {
            this.season = season;
        }
        
        public Season getSeason()
        {
            return this.season;
        }
    }
}
