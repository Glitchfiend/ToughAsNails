package toughasnails.season;

public enum WeatherEventType {
	eventUnknown(0),
	eventToSnowy(1),
	eventToNonSnowy(2),
	eventStartRaining(3),
	eventStopRaining(4);
	
	private int code;
	
	WeatherEventType(int code) {
		this.code = code;
	}
	
	public static WeatherEventType fromCode(int code) {
		for( WeatherEventType etype : values() ) {
			if( etype.code == code )
				return etype;
		}
		return eventUnknown;
	}
	
	public int getCode() {
		return code;
	}
}
