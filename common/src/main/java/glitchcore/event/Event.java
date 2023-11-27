/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

import java.util.Arrays;
import java.util.function.Consumer;

public class Event<T extends IEventContext>
{
    Consumer<T>[] listeners = new Consumer[0];

    public static <T extends IEventContext> Event<T> create()
    {
        return new Event<>();
    }

    public void addListener(Consumer<T> listener)
    {
        int len = this.listeners.length;
        this.listeners = Arrays.copyOf(this.listeners, len + 1);
        this.listeners[len] = listener;
    }

    public boolean hasListeners()
    {
        return this.listeners.length > 0;
    }

    public void fire(T context)
    {
        for (var listener : this.listeners)
        {
            listener.accept(context);

            if (listener instanceof ICancellableEventContext && ((ICancellableEventContext)listener).isCancelled())
                break;
        }
    }
}
