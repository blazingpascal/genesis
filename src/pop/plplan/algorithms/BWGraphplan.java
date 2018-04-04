package pop.plplan.algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class implementing the GRAPHPLAN algorithm.
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
public class BWGraphplan extends AbstractPlanner {
	
	public BWGraphplan(Set<Action> ops, Set<Proposition> facts, List<Proposition>  goal)
	{
		super(ops, facts, goal);
	}
	
	protected List<List<Action>> runAlgo() {
		
		PlanningGraph pGraph = getPlanningGraph();
		
        List<List<Action>> plan = extract(pGraph, getGoal(), pGraph.getNbLayers() - 1);
		
		//resetMarks(pGraph);
		
		int n = 0;
		while(plan == null)
		{
			//if(checkTimesUp()) return null;
			pGraph.expandGraph();
			plan = extract(pGraph, getGoal(), pGraph.getNbLayers() - 1);
			//resetMarks(pGraph);
			
			if(plan == null && pGraph.isFixedPointLevel())
			{
				int nbNoGood = pGraph.getNoGoodTableAt(pGraph.getFixedPointLevel()).size();
				if(n == nbNoGood) return null;
				else n = nbNoGood;
			}
		}

		return removeNoop(plan);
	}
	
	/**
	 * @param pGraph
	 */
	private void resetMarks(PlanningGraph pGraph) {
		for(int i = 0; i < pGraph.getLayers().size(); i++)
		{
			Iterator<Action> iter = pGraph.getLayerAt(i).getActs().iterator();
			while (iter.hasNext()) {
				iter.next().resetMark(i);
			}
		}
	}

	public List<List<Action>> extract(PlanningGraph pGraph, List<Proposition> goal, int i) {
		if(i == 0) return new ArrayList<List<Action>>(0);
		if(pGraph.getNoGoodTableAt(i).containsKey(goal)) return null;	
        List<List<Action>> planAti = search(pGraph, goal, new ArrayList<Action>(), i);
		if(planAti != null) return planAti;
		//if(isTimesUp()) return null;
		pGraph.getNoGoodTableAt(i).put(goal, goal);
		
		return null;
	}

	/**
	 * @param graph
	 * @param goal
	 * @param plan2
	 * @param i
	 * @return
	 */
	public List<List<Action>> search(PlanningGraph graph, List<Proposition> goal, ArrayList<Action> planAti, int i) {
		//if(checkTimesUp()) return null;
		if(goal.size() == 0)
		{
			List<Proposition> preconds = getPreconds(planAti);
            List<List<Action>> P = extract(graph, preconds, i - 1);
			if(P == null) return null;
			P.add(planAti);
			return P;
		}
		else
		{
			for (int k = goal.size() - 1; k >= 0 ; k--) {
				Proposition p = goal.get(k);
				
                List<Action> resolvers = getResolvers(graph.getLayerAt(i),
												   p,
												   planAti,
												   i);
				if(resolvers.size() == 0) return null; 
				Iterator<Action> actIter = resolvers.iterator();
				for (int j = resolvers.size(); j > 0; j--) {
					Action act =  actIter.next();
					
                    List<Proposition>  newGoal = goalSubSet(goal, act.getPosEffectSet());
					planAti.add(act);
					/*graph.getLayerAt(i).markExclusive(act, i);
					if(!goalStillPossible(graph.getLayerAt(i), newGoal, i))
					{
						planAti.remove(act);
						graph.getLayerAt(i).unmarkExclusive(act, i);
						continue;
					}*/
                    List<List<Action>> plan = search(graph, newGoal, planAti, i);
					if(plan == null)
					{
						planAti.remove(act);
						//graph.getLayerAt(i).unmarkExclusive(act, i);
					}
					else return plan;
				}				
			}
		}
		return null;
	}

//	/**
//	 * @param goal
//	 * @return
//	 */
//	private boolean goalStillPossible(Layer layer, List goal, int index) {
//		if(goal.size() == 0) return true;
//		
//		for(int i = goal.size() - 1; i >= 0 ; i--) {
//			Proposition p = (Proposition)layer.getProps().get(goal.get(i));
//			Iterator<Action> aIter = p.getPosInMap().values().iterator();
//			for(int j = p.getPosInMap().size(); j > 0 ; j--) {
//				if(!aIter.next().isMark(index)) return true;
//			}
//		}
//		return false;
//	}

	/**
	 * @param graph
	 * @param planAti
	 * @return
	 */
	private List<List<Action>> removeNoop(List<List<Action>> plan) {
        List<List<Action>> newPlan = new ArrayList<List<Action>>(plan.size());
		for (int i = 0; i < plan.size(); i++) {
            List<Action> list = plan.get(i);
            List<Action> newList = new ArrayList<Action>(list.size());
			newPlan.add(newList);
			for(int j = 0; j < list.size(); j++)
			{
				Action act = list.get(j);
				if(!act.isNoop()) newList.add(act);
			}
		}
		return newPlan;
	}

	/**
	 * @param goal
	 * @param p
	 * @param posEffectMap
	 * @return
	 */
	public List<Proposition>  goalSubSet(List<Proposition> goal, Set<Proposition> posEffectMap) {
        List<Proposition> newGoal = new ArrayList<Proposition>(goal.size());		
		for (int j = goal.size() - 1; j >= 0; j--) {
			Proposition prop = goal.get(j);
			if(!posEffectMap.contains(prop)) newGoal.add(prop);
		}
		return newGoal;
	}

	/**
	 * @param acts
	 * @param p
	 * @param plan
	 * @return
	 */
	public List<Action> getResolvers(Layer layer, Proposition p, ArrayList<Action> plan, int index) {
        List<Action> resolvers = new ArrayList<Action>();
		PropositionPGraph p2 = layer.getProps().get(p);
		Map<Action, MutexAction> muAi = layer.getMuActs();
		
		Iterator<Action> aIter = p2.getPosInMap().values().iterator();
		for(int i = p2.getPosInMap().size(); i > 0; i--){
			Action a = aIter.next();
			boolean isMutex = false;
			Iterator<Action> bIter = plan.iterator();
			while(bIter.hasNext() && !isMutex) {
                MutexAction mutex = muAi.get(bIter.next());
				isMutex =  mutex != null && mutex.containsNode(a);
			}

			if(!isMutex) 
			{
				if(a.isNoop()) resolvers.add(0, a);
				else resolvers.add(resolvers.size(), a);
			}
			//if(!(a.isMark(index))) resolvers.add(a);
		}
		return resolvers;
	}

	/**
	 * @param plan
	 * @return
	 */
	public List<Proposition> getPreconds(ArrayList<Action> plan)
	{
		List<Proposition> preconds = new ArrayList<Proposition>(plan.size() * 2);
		for (int i = plan.size() - 1; i >= 0; i--) {
			preconds.addAll(plan.get(i).getPrecondMap());
		}
		return preconds;
	}

	/* (non-Javadoc)
	 * @see plplan.algorithms.AbstractPlanner#getStartMsg()
	 */
	protected String getStartMsg() {
		return "BWSearch";
	}

	/* (non-Javadoc)
	 * @see plplan.algorithms.AbstractPlanner#getEndMsg()
	 */
	protected String getEndMsg() {
		return "";
	}
}
