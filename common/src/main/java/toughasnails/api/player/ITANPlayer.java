package toughasnails.api.player;

import toughasnails.api.temperature.ITemperature;
import toughasnails.api.thirst.IThirst;

public interface ITANPlayer
{
    ITemperature getTemperatureData();
    IThirst getThirstData();
}
