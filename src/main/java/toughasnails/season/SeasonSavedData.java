/*******************************************************************************
 * Copyright 2016, the Biomes O' Plenty Team
 * 
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.season;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import toughasnails.api.season.Season;
import toughasnails.core.ToughAsNails;
import toughasnails.util.DataUtils;
import toughasnails.util.IDataStorable;

public class SeasonSavedData extends WorldSavedData
{
    public static final String DATA_IDENTIFIER = "seasons";
    
    public int seasonCycleTicks;
    
    private boolean bLastSnowyState = false;
    private boolean bLastRainyState = false;
    public List<WeatherJournalEvent> journal = new ArrayList<WeatherJournalEvent>();
//    public int meltTicks;
//    public int snowTicks;
//    public static final int MAX_RAINWINDOW = 24000 * 3;	   // Three minecraft days
    
    public SeasonSavedData()
    {
        this(DATA_IDENTIFIER);
    }
    
    //This specific constructor is required for saving to occur
    public SeasonSavedData(String identifier)
    {
        super(identifier);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) 
    {
        this.seasonCycleTicks = nbt.getInteger("SeasonCycleTicks");
//        this.meltTicks = nbt.getInteger("MeltTicks");
//        this.snowTicks = nbt.getInteger("SnowTicks");
        try {
			this.journal = DataUtils.toListStorable(nbt.getByteArray("WeatherJournal"), WeatherJournalEvent.class);
		} catch (IOException e) {
			ToughAsNails.logger.error("Couldn't retrieve weather journal. Use a clear one.", e);
			this.journal = new ArrayList<WeatherJournalEvent>();
		}
        
        determineLastState();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
    {
        nbt.setInteger("SeasonCycleTicks", this.seasonCycleTicks);
//        nbt.setInteger("MeltTicks", this.meltTicks);
//        nbt.setInteger("SnowTicks", this.snowTicks);
        try {
			nbt.setByteArray("WeatherJournal", DataUtils.toBytebufStorable(journal));
		} catch (IOException e) {
			ToughAsNails.logger.error("Couldn't store weather journal.", e);
		}
        
        return nbt;
    }
    
    private void determineLastState() {
    	int lastSnowyState = -1;
    	int lastRainyState = -1;
    	for( int i = journal.size() - 1; i >= 0; i --) {
    		WeatherJournalEvent je = journal.get(i);
    		WeatherEventType etype = je.getEventType();
    		
    		switch( etype ) {
    		case eventToSnowy:
    			if( lastSnowyState == -1 )
    				lastSnowyState = 1;
    			break;
    		case eventToNonSnowy:
    			if( lastSnowyState == -1 )
    				lastSnowyState = 0;
    			break;
    		case eventStartRaining:
    			if( lastRainyState == -1 )
    				lastRainyState = 1;
    			break;
    		case eventStopRaining:
    			if( lastRainyState == -1 )
    				lastRainyState = 0;
    			break;
    		case eventUnknown:
    			ToughAsNails.logger.warn("Unknown weather journal entry found.");
    		}
    		
    		// Is now fully determined?
    		if( lastSnowyState != -1 &&
    			lastRainyState != -1 )
    			break;
    	}
    	
    	if( lastSnowyState == -1 )
    		bLastSnowyState = false;	// Default: First minecraft day is at spring.
    	if( lastRainyState == -1 )
    		bLastRainyState = false;	// Default: First minecraft day has no rain.
    }
    
    public boolean wasLastRaining() {
    	return bLastRainyState;
    }
    
    public boolean wasLastSnowy() {
    	return bLastSnowyState;
    }
    
    public int getJournalIndexAfterTime(long timeStamp) {
    	// TODO: Use subdivision to find the time point in approx. O(log n) steps.
    	
    	for( int i = 0; i < journal.size(); i ++ ) {
    		if( journal.get(i).getTimeStamp() >= timeStamp )
    			return i;
    	}
    	
    	return -1;
    }
    
    private void addEvent(World w, WeatherEventType eventType) {
    	switch( eventType ) {
    	case eventToSnowy:
    		bLastSnowyState = true;
    		break;
    	case eventToNonSnowy:
    		bLastSnowyState = false;
    		break;
    	case eventStartRaining:
    		bLastRainyState = true;
    		break;
    	case eventStopRaining:
    		bLastRainyState = false;
    		break;
		case eventUnknown:
			ToughAsNails.logger.warn("Unknown weather event added. Ignoring");
			return;
    	}
    	
    	journal.add(new WeatherJournalEvent(w.getTotalWorldTime(), eventType));
    }
    
    public void updateState( World w, Season curSeason ) {
        if( curSeason == Season.WINTER && !wasLastSnowy() )
        	addEvent(w, WeatherEventType.eventToSnowy);
        else if( curSeason != Season.WINTER && wasLastSnowy() )
        	addEvent(w, WeatherEventType.eventToNonSnowy );
        
        if( w.isRaining() && !wasLastRaining() )
        	addEvent(w, WeatherEventType.eventStartRaining);
        else if( !w.isRaining() && wasLastRaining() )
        	addEvent(w, WeatherEventType.eventStopRaining);
    }

/*
    public void shiftSnowWindow(int tickCount, boolean isWinter) {
    	int activeTicks, nonActiveTicks;
    	if( isWinter ) {
    		activeTicks = this.snowTicks;
    		nonActiveTicks = this.meltTicks;
    	}
    	else {
    		nonActiveTicks = this.snowTicks;
    		activeTicks = this.meltTicks;
    	}

    	activeTicks += tickCount;
		if( activeTicks > MAX_RAINWINDOW ) {
			activeTicks = MAX_RAINWINDOW;
			nonActiveTicks = 0;
		}
		else if( activeTicks + nonActiveTicks > MAX_RAINWINDOW ) {
			nonActiveTicks = MAX_RAINWINDOW - activeTicks;
		}

    	if( isWinter ) {
    		this.snowTicks = activeTicks;
    		this.meltTicks = nonActiveTicks;
    	}
    	else {
    		this.snowTicks = nonActiveTicks;
    		this.meltTicks = activeTicks;
    	}
    } */
}
