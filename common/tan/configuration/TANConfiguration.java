package tan.configuration;

import java.io.File;

public class TANConfiguration
{
    public static File temperatureConfigFile;
    
    public static void init(String configpath)
    {
        temperatureConfigFile = new File(configpath + "temperature.cfg");
        
        TANConfigurationTemperature.init(temperatureConfigFile);
    }
}
