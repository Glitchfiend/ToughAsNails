/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 *
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/
package toughasnails.init;

import net.minecraftforge.common.capabilities.CapabilityManager;
import toughasnails.api.capability.IThirst;
import toughasnails.thirst.ThirstData;
import toughasnails.thirst.ThirstStorage;

public class ModCapabilities
{
    public static void init()
    {
        CapabilityManager.INSTANCE.register(IThirst.class, new ThirstStorage(), () -> new ThirstData());
    }
}
