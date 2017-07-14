package toughasnails.config;

public class CropGrowConfigEntry {
	private int minLiving;
	private int minOptimal;
	private int maxOptimal;
	private int maxLiving;
	private float nonOptimalChance;

	public CropGrowConfigEntry(int minLiving, int minOptimal, int maxOptimal,
			int maxLiving, float nonOptimalChance) {
		this.minLiving = minLiving;
		this.minOptimal = minOptimal;
		this.maxOptimal = maxOptimal;
		this.maxLiving = maxLiving;
		this.nonOptimalChance = nonOptimalChance;
	}

	public int getMinLiving() {
		return this.minLiving;
	}

	public int getMinOptimal() {
		return this.minOptimal;
	}

	public int getMaxOptimal() {
		return this.maxOptimal;
	}

	public int getMaxLiving() {
		return this.maxLiving;
	}

	public float getNonOptimalChance() {
		return this.nonOptimalChance;
	}
}