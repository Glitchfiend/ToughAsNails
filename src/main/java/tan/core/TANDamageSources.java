package tan.core;

import net.minecraft.util.DamageSource;

public class TANDamageSources extends DamageSource
{
    public static DamageSource dehydration = new TANDamageSources("dehydration").setDamageBypassesArmor();
    
    protected TANDamageSources(String name)
    {
        super(name);
    }

}
