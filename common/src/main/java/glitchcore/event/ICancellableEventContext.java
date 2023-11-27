/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event;

interface ICancellableEventContext
{
    void setCancelled(boolean value);

    boolean isCancelled();
}
