package model.genetics.subtypes;

import java.util.Random;

import model.genetics.AScalingRecessiveGeneticTrait;

public class DimplesTrait extends AScalingRecessiveGeneticTrait<DimplesTrait>{

	public static final DimplesTrait DIMPLES = new DimplesTrait("dimples",1);
	public static final DimplesTrait NO_DIMPLES = new DimplesTrait("no dimples", -1);
	
	protected DimplesTrait(String name, double recessiveValue) {
		super(name, recessiveValue);
	}
	
	public DimplesTrait(DimplesTrait maternal, DimplesTrait paternal, Random r){
		super(maternal, paternal, r);
	}

	@Override
	protected void setHistoryToSelf() {
		this.maternalHistory = this;
		this.paternalHistory = this;
	}
	
	public static DimplesTrait random(Random r){
		if(r.nextBoolean()) return DIMPLES;
		return NO_DIMPLES;
	}
	
	public static DimplesTrait randomSkewRecessive(Random r){
		for(int i = 0; i < 3; i++){
			if(r.nextBoolean()) return NO_DIMPLES;
		}
		return DIMPLES;
	}

}
