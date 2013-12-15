package tan.core;

import net.minecraft.util.DamageSource;

public class TANDamageSource extends DamageSource
{
    public static DamageSource dehydration = new TANDamageSource("dehydration").setDamageBypassesArmor();
    public static DamageSource hyperthermia = new TANDamageSource("hyperthermia").setDamageBypassesArmor();
    public static DamageSource hypothermia = new TANDamageSource("hypothermia").setDamageBypassesArmor();
    
    protected TANDamageSource(String name)
    {
        super(name);
    }
}
