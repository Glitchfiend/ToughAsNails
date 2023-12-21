/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

public abstract class TickEvent extends Event
{
    private final Phase phase;

    public TickEvent(Phase phase)
    {
        this.phase = phase;
    }

    public Phase getPhase()
    {
        return this.phase;
    }

    public enum Phase
    {
        START, END;
    }

    public static class Client extends TickEvent
    {
        public Client(Phase phase)
        {
            super(phase);
        }
    }
}
