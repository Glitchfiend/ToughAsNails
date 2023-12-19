/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

public abstract class RenderGuiEvent extends Event
{
    private final Type type;

    public RenderGuiEvent(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        return this.type;
    }

    public static class Pre extends RenderGuiEvent
    {
        public Pre(Type type)
        {
            super(type);
        }
    }

    public enum Type
    {
        FOOD;
    }
}
