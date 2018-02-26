package model.genetics.subtypes;

import java.util.Random;

import model.genetics.AScalingRecessiveGeneticTrait;

public final class EarlobeAttachmentTrait extends AScalingRecessiveGeneticTrait<EarlobeAttachmentTrait>{

	public static final EarlobeAttachmentTrait UNATTACHED = new EarlobeAttachmentTrait("Unattached", 1); 
	public static final EarlobeAttachmentTrait ATTACHED = new EarlobeAttachmentTrait("Attached", -1); 
	
	public EarlobeAttachmentTrait(EarlobeAttachmentTrait maternalHistory, EarlobeAttachmentTrait paternalHistory,
			Random r) {
		super(maternalHistory, paternalHistory, r);
	}
	
	protected EarlobeAttachmentTrait(String name, double recessiveValue){
		super(name, recessiveValue);
	}

	@Override
	protected void setHistoryToSelf() {
		this.maternalHistory = this;
		this.paternalHistory = this;
	}
}
