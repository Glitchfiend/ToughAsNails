/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package toughasnails.glitch.event.client;

import toughasnails.glitch.event.Event;

public abstract class InputEvent extends Event
{
  // See InputConstants
  public static class Key extends InputEvent
  {
    private final int key;
    private final int scanCode;
    private final int action;
    private final int modifiers;
    private boolean handledDebugKey;

    public Key(int key, int scanCode, int action, int modifiers, boolean handledDebugKey)
    {
      this.key = key;
      this.scanCode = scanCode;
      this.action = action;
      this.modifiers = modifiers;
      this.handledDebugKey = handledDebugKey;
    }

    public int getKey()
    {
      return this.key;
    }

    public int getScanCode()
    {
      return this.scanCode;
    }

    public int getAction()
    {
      return this.action;
    }

    public int getModifiers()
    {
      return this.modifiers;
    }

    public boolean getHandledDebugKey()
    {
      return this.handledDebugKey;
    }

    public void setHandledDebugKey(boolean value)
    {
      this.handledDebugKey = value;
    }
  }
}
