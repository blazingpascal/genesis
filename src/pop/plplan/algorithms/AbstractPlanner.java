
package pop.plplan.algorithms;

import java.util.List;
import java.util.Set;

/**
 * A class that represent an abstract planner.
 * <p>
 * PLPLAN
 * Authors : Philippe Fournier-Viger and Ludovic lebel
 * <p>
 * This work is licensed under the Creative Commons Attribution 2.5 License. To
 * view a copy of this license, visit
 * http://creativecommons.org/licenses/by/2.5/ or send a letter to Creative
 * Commons, 543 Howard Street, 5th Floor, San Francisco, California, 94105, USA.
 * <p>
 * If you use PLPLAN, we ask you to mention our names and our webpage URL in your work. 
 * The PLPLAN software is copyrighted by Philippe Fournier-Viger and Ludovic Lebel (2005). 
 * Please read carefully the license to know what you can do and cannot do with this software. 
 * You can contact Philippe Fournier-Viger for special permissions. 
 * <p>
 * This sofware is provided "as is", without warranty of any kind. 
 * The user takes the entire risk as to the quality and performance of the software. 
 * The authors accept no responsibility for any problem the user encounters using this software.
 * <p>
 * @author Philippe Fournier-Viger and Ludovic Lebel
 */
public abstract class AbstractPlanner {

	private PlanningGraph pGraph = null;
	private String planningOutput;
	private long startPlanning = 0;
	private Set<Action>  ops;
	private Set<Proposition> facts; 
	private List<Proposition>  goal;
	
	private long timeLimit = 160000;
	
	AbstractPlanner(Set<Action> ops, Set< Proposition> facts, List<Proposition>  goal)
	{
		pGraph = new PlanningGraph(ops,facts);
		this.ops = ops;
		this.facts = facts;
		this.goal = goal;
	}
	
	private boolean createPlanningGraph()
	{
		while (!getPlanningGraph().isPiContainingAllGoalProposition(goal)) {
			if (getPlanningGraph().isFixedPointLevel()) 
				return false;
			else
				getPlanningGraph().expandGraph();
		}
		
		return true;
	}
	
	public List run()
	{
		boolean planExist = createPlanningGraph();
		if(!planExist) 
		{
			planningOutput = "Aucune solution! niveau : " +
							 getPlanningGraph().getFixedPointLevel();
			return null;
		}
		
		msgStartPlanning();
		startPlanning = System.currentTimeMillis();
		List plan = runAlgo();
		msgEndPlanning(plan);
		
		return plan;
	}
	
	protected abstract List runAlgo();
	
	public PlanningGraph getPlanningGraph()
	{
		return pGraph;
	}
	
	public String getPlanningOutput()
	{
		return planningOutput;
	}
	
	public void msgStartPlanning()
	{
		planningOutput = "Planning with " + getStartMsg() + "\n";
		planningOutput += "Init state : " + facts.toString() + "\n";
		planningOutput += "Goal : " + goal.toString() + "\n";
		
	}
	
	public void msgEndPlanning(List plan)
	{
		long endPlanning = System.currentTimeMillis();
		planningOutput += "--Result--\n";
		planningOutput += "Exec time : " + (endPlanning - startPlanning) + "\n";
		if(plan != null)
			planningOutput += "Solution : " + plan.toString() + "\n";
		else
			planningOutput += "Solution : Inexistant!!! \n";
		planningOutput += getEndMsg();
	}
	
	protected abstract String getStartMsg();
	protected abstract String getEndMsg();
	
	public Set<Action> getOps()
	{
		return ops;
	}
	
	public Set<Proposition> getFacts()
	{
		return facts;
	}
	
	public List<Proposition>  getGoal()
	{
		return goal;
	}

}
