package pop.plplan.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Instance of this class represent layer of the planning graph.
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
public class Layer {

	private Map<PropositionPGraph,PropositionPGraph> props; // clé : Proposition, Objet : Proposition
	private Map<Proposition,MutexProp> muProps; // clé : Proposition, Objet : Mutex
	private Set<Action> acts; // clé : Action, Objet : Action
	private Set<Action> actsWithoutNoop; // clé : Action, Objet : Action
	private Map<Action,MutexAction> muActs; // clé : Action, Objet : Mutex
	private Map<List<Proposition>, List<Proposition>> noGoodTable; 
	
	public Layer(int nbOps) {
		
		int mapSize = (int)(nbOps * 1.33); 
		props = new HashMap<PropositionPGraph,PropositionPGraph>(mapSize);
		muProps = new HashMap<Proposition,MutexProp>(mapSize * 2);
		acts = new HashSet<Action>(mapSize * 2);
		actsWithoutNoop = new HashSet<Action>(mapSize);
		muActs = new HashMap<Action,MutexAction>(mapSize * 2);
		noGoodTable = new HashMap<List<Proposition>, List<Proposition>>(mapSize / 4);
	}
	
	public Map<PropositionPGraph,PropositionPGraph> getProps() {
		return props;
	}
	
	public void setProps(Map<PropositionPGraph,PropositionPGraph> props) {
		this.props = props;
	}
	
	public Set<Action> getActs() {
		return acts;
	}
	
	public Set<Action> getActsWithoutNoop(){
		return actsWithoutNoop;
	}
	
	public Map<Proposition,MutexProp> getMuProps() {
		return muProps;
	}
	
	public Map<Action,MutexAction> getMuActs() {
		return muActs;
	}
	
	public Map<List<Proposition>, List<Proposition>> getNoGoodTable() {
		return noGoodTable;
	}
	
    
    public void addNodeMutex(Map<Proposition,MutexProp> muMap, Proposition propA, Proposition propB)
    {
        MutexProp muA = muMap.get(propA);
        MutexProp muB = muMap.get(propB);
        
        if(muA == null)
        {
            muA = new MutexProp(propA);
            muMap.put(propA, muA);  
            muA.add(propB);
        } 
        else if(!muA.containsNode(propB)) {
            muA.add(propB);
        }
        
        if(muB == null) 
        {
            muB = new MutexProp(propB);
            muMap.put(propB, muB);
            muB.add(propA);
        } 
        else if(!muB.containsNode(propA)) {
            muB.add(propA);
        }
    }
    
    public void addNodeMutex(Map<Action,MutexAction> muMap, Action actionA, Action actionB)
    {
        MutexAction muA = muMap.get(actionA);
        MutexAction muB = muMap.get(actionB);
        
        if(muA == null)
        {
            muA = new MutexAction(actionA);
            muMap.put(actionA, muA);  
            muA.add(actionB);
        } 
        else if(!muA.containsNode(actionB)) {
            muA.add(actionB);
        }
        
        if(muB == null) 
        {
            muB = new MutexAction(actionB);
            muMap.put(actionB, muB);
            muB.add(actionA);
        } 
        else if(!muB.containsNode(actionA)) {
            muB.add(actionA);
        }
    }
	
	public String toString(){
		String temp = "[LAYER : Actions : ";
		Iterator<Action> iterActs = getActs().iterator();
		Iterator<PropositionPGraph> iterProps = getProps().values().iterator();
		Iterator<MutexAction> iterMuActs = getMuActs().values().iterator();
		Iterator<MutexProp> iterMuProps = getMuProps().values().iterator();
	    while (iterActs.hasNext()) {
			temp = temp + iterActs.next().getName() + " ";
		}
	    temp = temp + "\n         Props   : ";
	    while (iterProps.hasNext()) {
			temp = temp + iterProps.next().getName() + " ";
		}
	    temp = temp + "\n         muProps : ";
	    while (iterMuProps.hasNext()) {
			temp = temp + iterMuProps.next() + " ";
		}
	    temp = temp + "\n         muActs : ";
	    while (iterMuActs.hasNext()) {
			temp = temp + iterMuActs.next() + " ";
		}
	    temp = temp + "\n";
		return temp;
	}

	/**
	 * @param act
	 */
	public void markExclusive(Action act, int index) {
        MutexAction mutex = muActs.get(act);
		if(mutex != null) mutex.markExclusive(index);		
	}

	/**
	 * @param act
	 */
	public void unmarkExclusive(Action act, int index) {
        MutexAction mutex = muActs.get(act);
		if(mutex != null) mutex.unmarkExclusive(index);
	}
}
