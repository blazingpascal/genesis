package model.person;

import java.util.Random;

public class CareerRole extends ARole {
	public static final CareerRole Workaholic = new CareerRole("Workaholic", 1, 1, 1);
	public static final CareerRole Slacker = new CareerRole("Slacker", 0, 0, 1);
	public static final CareerRole JackOfAllTrades = new CareerRole("Jack of All Trades", 1, 1f, 0.5f);
	public static final CareerRole AverageEmployee = new CareerRole("Average Employee", 1, 0.5f, 0.5f);
	public static final CareerRole JobSecuritySeeker = new CareerRole("Job Security Seeker", 1, 0.5f, 1f);

	private static final CareerRole[] opts = new CareerRole[5];

	static {
		opts[0] = Workaholic;
		opts[1] = Slacker;
		opts[2] = JackOfAllTrades;
		opts[3] = AverageEmployee;
		opts[4] = JobSecuritySeeker;
	}

	private final float tenacity;
	private final float focus;

	CareerRole(String title, float strength, float tenacity, float focus) {
		super(title, strength);
		this.tenacity = tenacity;
		this.focus = focus;
	}

	@Override
	ARole merge(ARole r2) {
		return r2.mergeCaR(this);
	}

	public static CareerRole getRandomRole(Random r) {
		return opts[r.nextInt(opts.length)];
	}

	@Override
	ARole mergeCaR(CareerRole careerRole) {
		float tenacity = (this.tenacity + careerRole.tenacity) / 2;
		float focus = (this.focus + careerRole.focus) / 2;
		float strength = (this.strength + careerRole.strength) / 2;
		String title = calculateTitle(tenacity, focus);
		return new CareerRole(title, strength, tenacity, focus);
	}

	private static String calculateTitle(float tenacity, float focus) {
		return String.format("%s tenacious and %s focused", ARole.adverbModifier(tenacity),
				ARole.adverbModifier(focus));
	}

	public Double tenacity() {
		return (double) this.tenacity;
	}
	
	public Double focus(){
		return (double) this.focus;
	}

  @Override
  public double computeCareerProgressionModifier() {
      return (this.tenacity - 0.5) * .4;
  }

  @Override
  public double getCareerFocus() {
      return this.focus;
  }

  @Override
  public double getCareerTenacity() {
      return this.tenacity;
  }
}
