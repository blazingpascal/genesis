package model.genetics;

import java.util.Random;

import model.genetics.subtypes.CleftChinTrait;
import model.genetics.subtypes.DimplesTrait;
import model.genetics.subtypes.EarlobeAttachmentTrait;
import model.genetics.subtypes.EyeColorTrait;
import model.genetics.subtypes.FrecklesTrait;
import model.genetics.subtypes.HairColorTrait;
import model.genetics.subtypes.HairTextureTrait;
import model.genetics.subtypes.HandednessTrait;
import model.genetics.subtypes.SkinColorTrait;
import model.genetics.subtypes.TongueRollingTrait;

public class GeneticsMap {
	private static float MUTATION_CHANCE = 0.05f;
	private HairColorTrait hairColor;
	/*private EyeColorTrait eyeColor;
	private SkinColorTrait skinColor;
	private HairTextureTrait hairTexture;
	private EarlobeAttachmentTrait earlobeAttachment;
	private TongueRollingTrait tongueRolling;
	private DimplesTrait dimplesTrait;
	private WidowsPeakTrait widowsPeak;
	private CleftChinTrait cleftChinTrait;
	private HandednessTrait handedness;
	private FrecklesTrait freckles;*/
	
	public GeneticsMap(HairColorTrait hairColor){
		this.hairColor = hairColor;
	}
	
	public GeneticsMap combine(GeneticsMap map, Random r){
		HairColorTrait hairColorTrait = r.nextDouble() < MUTATION_CHANCE ? HairColorTrait.randomSkewRecess(r) : new HairColorTrait(this.hairColor, map.hairColor, r);
		return new GeneticsMap(hairColorTrait);
	}

	public HairColorTrait getHairColor() {
		return hairColor;
	}

	public static GeneticsMap randomGenes(Random r) {
		return new GeneticsMap(HairColorTrait.random(r));
	}

	public static GeneticsMap randomSkewRecessGenes(Random random) {
		return new GeneticsMap(HairColorTrait.randomSkewRecess(random));
	}
}
