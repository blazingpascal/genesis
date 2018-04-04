package pop.plplan.algorithms;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * A class implementing various forward planning algorithm.
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

public class FWPlanner extends AbstractPlanner {
	// NOTE :
	// * Pour IndependentActionsWorld, si on active l'algo 1, il faut faire :
	//   wasSeenEnabled = false, si on veut être certain d'obtenir une solution.
	//   Ceci est dû au fait qu'avec les persistent set, on ne vérifie qu'un 
	//   certain nombre de transitions dans un état. Si on active wasSeen, 
	//   la deuxième fois qu'on rencontrera l'état on le rejettera. Donc, 
	//   on ne vérifiera pas toutes les transitions.
	// * Pour RejectedStateWorld, l'algo 1 fonctionne mal, car
	//   il y a des actions irréversibles. Si elles sont choisies vers le
	//    début de la recherche, on ne trouvera pas de solution.
	// * J'assume que la clé des états est unique pour les états! Si nos états
	//   ne contienne pas plus de quinze propositions environ, la clé devrait
	// normalement toujours être unique.
	
	
	// TOUJOURS UTILISER SLEEP AVEC WAS SEEN

	/** ***** Options de configuration ****** */
	private static final int PROFONDEUR_PAR_ITERATIONS = 1;

	private int algoPersistentSet = 0;

	private boolean sleepSetEnabled = false;

	private boolean wasSeenEnabled = true; // option valide seulement quand on

	private static final int PROFONDEUR_MAXIMUM_TOTAL = 100;

	/** ************************************* */

	private int profondeurLimite;

	private final Random r = new Random();

	private final List<Proposition> goal;
    private final Set<Proposition> facts;

//	int nbEtatsRejeter;
//	int nbEtatsExplorer;
//	int nbTransitions;

//	private HashTableString wasSeenSleep; // map associant la clé de H(s) à son sleep set :
							  // H(s).sleep
    
//    private IntSet wasSeen = new IntOpenHashSet(); // Je l'initialise aussi ici pour les
//      tests unitaires...
    
    private Set wasSeen = new HashSet(); // Je l'initialise aussi ici pour les
//  tests unitaires...

    private Map wasSeenSleep; // map associant la clé de H(s) à son sleep set :
    // H(s).sleep

	private Action[] theplan;

	private List<Set<Proposition>> statesList;
	private List<Action[]> plansList;
	private List<Set<Action>> sleepSetList;
	
	public FWPlanner(Set<Action> ops, Set<Proposition> facts, List<Proposition> goal) {
		super(ops, facts, goal);
		this.goal = goal;
        this.facts = facts;
	}


	protected List<Action> runAlgo() {
		// Initialisation
		profondeurLimite = getPlanningGraph().getLayers().size();
		statesList = new ArrayList<Set<Proposition>>();
		plansList = new ArrayList<Action[]>();
		sleepSetList = new ArrayList<Set<Action>>();
//        wasSeen =  new IntOpenHashSet(500);
        wasSeen =  new HashSet(500);
		wasSeenSleep = new HashMap<String, Map<Action,Action>>(2500);
//		nbEtatsRejeter = 0;
//		nbEtatsExplorer = 0;
//		nbTransitions = 0;
		theplan = null;
//		System.out.println("Nb de couches initial : " + profondeurLimite);

		// Premier essai
		Set<Proposition> s0 = facts;
		extract(1, s0, new HashSet<Action>(), new Action[0]);
		if (theplan != null)
			return  Arrays.asList(theplan);
		
		int niv = profondeurLimite;

		// Essais subséquents
		while (theplan == null) {
//			if(checkTimesUp()) 
//			{
//				theplan = null;
//				return null;
//			}
			profondeurLimite += PROFONDEUR_PAR_ITERATIONS;
			for (int i = 0; i < PROFONDEUR_PAR_ITERATIONS; i++) {
				getPlanningGraph().expandGraph();
			}
			
			List<Set<Proposition>> statesToExploreOld = statesList;
			List<Action[]> plansToExploreOld = plansList;
			List<Set<Action>> sleepSetToExploreOld = sleepSetList;
			int size = statesToExploreOld.size();
			
			statesList = new ArrayList<Set<Proposition>>(size * 2);  
			plansList = new ArrayList<Action[]>(size * 2);
			sleepSetList = new ArrayList<Set<Action>>(size * 2);
			
			for (int i = 0; i < size; i++) {
//				if(checkTimesUp()) {
//					theplan = null;
//					return null;
//				}
				extract(niv, statesToExploreOld.get(i), 
						sleepSetToExploreOld.get(i), 
						plansToExploreOld.get(i));	
			}

			if (++niv > PROFONDEUR_MAXIMUM_TOTAL) {
				System.out.println("BOUM !   niveau : " + niv);
				return null;
			}
		}
		return Arrays.asList(theplan);
	}


	// Algorithme de recherche de solution inspiré de fig 4.1, de la thèse de
	// Godefroid
	public void extract(int niveau, Set<Proposition> s, Set<Action> sleep, Action[] plan) {
		if (theplan != null) {
			return;
		}
		
		int key = calculateWasSeenKey(s);

		if (!sleepSetEnabled && wasSeenEnabled && wasSeen.contains(key)) {
			//nbEtatsRejeter++;
			return;
		}
		
		
		if (isSContainingAllGoalProposition(s)) {
			theplan = plan;
			return;
		}

		if (niveau == profondeurLimite) {
			statesList.add(s);
			plansList.add(plan);
			sleepSetList.add(sleep);
			return;
		}
		//nbEtatsExplorer++;

		Layer layer = getPlanningGraph().getLayerAt(niveau);

		// Calcul de l'ensemble T des actions applicables
		Set<Action> T = null;

		if (sleepSetEnabled) {
			if (wasSeenEnabled && wasSeen.contains(key)) {
				Set<Action> previousSleep = (Set<Action>)wasSeenSleep.get(key);
				
				T = new HashSet<Action>();
                Set<Action> newSleep = new HashSet<Action>();
//                System.out.println(previousSleep);
//                System.out.println(previousSleep.values());
				Iterator<Action> iter = previousSleep.iterator();
				while(iter.hasNext()){
					Action act = iter.next();
					if (sleep.contains(act)) {
						newSleep.add(act); 
					} else {
						T.add(act);
					}
				}
				sleep = newSleep;

				wasSeenSleep.put(key, newSleep);

			} else { // lignes 6 a 9 (fig 5.2)
				wasSeen.add(key);
				wasSeenSleep.put(key, sleep);

				Set<Action> enabled = calculateEnabled(s, layer.getActsWithoutNoop(), sleep);
				if (enabled.size() == 0){
					return;
				}
				
				T = persistentSet(layer, enabled);
			}
		} else {
			// LE CAS SANS SLEEP SETS
			if (wasSeenEnabled) {
				wasSeen.add(key);
			}

			Set<Action> enabled = calculateEnabled(s, layer.getActsWithoutNoop());
			if (enabled.size() == 0)
				return;
			T = persistentSet(layer, enabled);
		}

		// Essayer chacune des actions
		//nbTransitions  += T.size();
		Iterator<Action> iter = T.iterator();
		while(iter.hasNext() && theplan == null){
			Action aT = iter.next();
			if (sleepSetEnabled) {
				// ligne 17, 19, fig 5.2
				Set<Action> succesorSleepSet = calculateSucessorSleepSet(sleep, aT, layer);
				sleep.add(aT);
				extract(niveau + 1, sucessor(s, aT), succesorSleepSet , concat(plan, aT));
			} else {
				extract(niveau + 1, sucessor(s, aT), null, concat(plan, aT));
			}
		}
	}

	public Set<Action>  calculateEnabled(Set<Proposition> s, Set<Action> actions) {
		Iterator<Action> iter = actions.iterator();
		Set<Action>  enabled = new HashSet<Action>();
		while(iter.hasNext()){
			Action act = (Action) iter.next();
			if (act.isApplicableForPropositions(s))
				enabled.add(act);
		}
		return enabled;
	}
	
	public Set<Action> calculateEnabled(Set<Proposition> s, Set<Action>  actions, Set<Action> sleep) {
		Iterator<Action> iter = actions.iterator();
		Set<Action> enabled = new HashSet<Action>();
		while(iter.hasNext()){
			Action act = iter.next();
			if (!sleep.contains(act) && act.isApplicableForPropositions(s))
				enabled.add(act);
		}
		return enabled;
	}

	//	******************* ENSEMBLE 'sleep' *********************/

	// implantation de la ligne 17, fig 5.2
	public Set<Action> calculateSucessorSleepSet(Set<Action> sleepS, Action t, Layer layer) {
		if(sleepS.isEmpty()){
			return new HashSet<Action>();
		}
        MutexAction mutex = layer.getMuActs().get(t);
		if(mutex == null){
			return new HashSet<Action>(sleepS);
        }
        Set<Action> newSleep = new HashSet<Action>();
		Iterator<Action> iter = sleepS.iterator();
		while(iter.hasNext()){
			Action tprime = iter.next();
			if (!mutex.containsNode(tprime)) { // est indépendant
				newSleep.add(tprime);
			}
		}
		return newSleep;
	}

	//******************* ENSEMBLE PERSISTANTS *********************/

	public Set<Action> persistentSet(Layer layer, Set<Action> enabled) {
		switch (algoPersistentSet) {
		case 0:
			return enabled; // Algo 0 : ensemble trivial
		case 1:
			return persistentSetAlg1(layer, enabled);
		default:
			return null;
		}
	}

	//******************* Algorithme 1 : conflicting transitions *****/
	public Set<Action> persistentSetAlg1(Layer layer, Set<Action> enabled) {

		Set<Action> T = new HashSet<Action>(enabled.size()); 
		
		// (1) Ajouter une des actions possibles à T
		Action actInitial = chooseRandomValueFrom(enabled);
		T.add(actInitial);

	// (2) Ajouter toutes les actions mutex avec une dans T, et répêter.
	boolean hasChange = true;
	do {
		hasChange = false;
		Iterator<Action> iter = enabled.iterator();
		while(iter.hasNext()){
			Action tprime = iter.next();
			if (!T.contains(tprime) &&
				actionIsMutexWithAtLeastOneOf(tprime, T, layer)) {
				T.add(tprime);
				hasChange = true;
			}
		}
	} while (hasChange);
	
	return T;
}

	public boolean actionIsMutexWithAtLeastOneOf(Action a, Set<Action> actions,
			Layer layer) {
        MutexAction mutex = layer.getMuActs().get(a);
		return mutex != null
				&& mutex.containsAtLeastOneOf(actions.iterator());
	}

	// fonction pour choisir aléatoirement un élément d'une Map
	public Action chooseRandomValueFrom(Set<Action> map) {
		int index = r.nextInt(map.size());
		Iterator<Action> iter = map.iterator();
		for (int i = 0; i < index; i++) {
			iter.next();
		}
		return iter.next();
	}
	//*******************************************************************/

	public HashSet<Proposition> sucessor(Set<Proposition> s, Action a) {
        HashSet<Proposition> sSucc = new HashSet<Proposition>();
		//effets négatifs
		Iterator<Proposition> iter = s.iterator();
		while(iter.hasNext()){
            Proposition prop = iter.next();
			if (!a.getNegEffectSet().contains(prop)) {
				sSucc.add(prop);
			}
		}
		//effets positifs
		iter = a.getPosEffectSet().iterator();
		while(iter.hasNext()){
			Proposition prop = iter.next();
			if (!s.contains(prop)) {
				sSucc.add(prop);
			}
		}
		return sSucc;
	}

	public boolean isSContainingAllGoalProposition(Set<Proposition> s) {
		for(int i = goal.size() - 1; i >= 0; i--){
			if (!s.contains(goal.get(i))) {
				return false;
			}
		}
		return true;
	}

	public int calculateWasSeenKey(Set<Proposition> s) {
        int size = s.size();
        byte[] state = new byte[size];
        Iterator<Proposition> iter = s.iterator();
        while (iter.hasNext()) {
            state[--size] = iter.next().getName();
        }
        Arrays.sort(state);
        return Arrays.hashCode(state);
	}

	/* (non-Javadoc)
	 * @see plplan.algorithms.AbstractPlanner#getStartMsg()
	 */
	protected String getStartMsg() {
		String sleepSetEnabledStr = "No";
		String algoTypeStr = Integer.toString(algoPersistentSet);
		if(sleepSetEnabled) sleepSetEnabledStr = "Yes";
		return "FWSearch - " +
			   "SleepSet : " + sleepSetEnabledStr + 
			   "AlgoType : " + algoTypeStr +
			   "StateMatching : " + wasSeenEnabled;
	}

	protected String getEndMsg() {
		return "";
	}
	
	public void setSleepSetEnabled(boolean valeur){
		sleepSetEnabled = valeur;
	}
	
	public void setWasSeenEnabled(boolean valeur){
		wasSeenEnabled = valeur;
	}
	
	public void addToWasSeen(Set<Proposition> s) {
		wasSeen.add(calculateWasSeenKey(s));
	}
	
	public void setAlgorithmePersistentSet(int val) {
		algoPersistentSet = val;
	}
	
	public Action[] concat(Action[] first, Action b) {
		Action [] actions = new Action[first.length + 1];
		System.arraycopy(first, 0, actions, 0, first.length);
		actions[first.length] = b;
		return actions;
	}
	
//	public List<Action> arrayToList(Action[] array){
//		if(array == null) // Nécessaire ?
//			return null;
//		List<Action> list = new ArrayList<Action>(array.length);
//		for (int i = 0; i < array.length; i++) {
//			list.add(array[i]);
//		}
//        
//		return list;
//	}
    
//    public void sortState(){
//        Collections.sort();
//        Object[] a = list.toArray();
//        Arrays.sort(a);
//        ListIterator<T> i = list.listIterator();
//        for (int j=0; j<a.length; j++) {
//            i.next();
//            i.set((T)a[j]);
//        }
//        
////        for()
//    }
}


