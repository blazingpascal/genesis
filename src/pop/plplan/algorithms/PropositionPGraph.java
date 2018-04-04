package pop.plplan.algorithms;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represent a node of a planning graph wich is a proposition.
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
public class PropositionPGraph extends Proposition{
	
	/* TODO optimize HashMap size*/ 
	private final int HASH_CAPACITY = 15;
	private Map<Action, Action> negIn = new HashMap<Action, Action>(HASH_CAPACITY);
	private Map<Action,Action> posIn = new HashMap<Action,Action>(HASH_CAPACITY);
	private Map<Action,Action> out = new HashMap<Action,Action>(HASH_CAPACITY);	
    
	public PropositionPGraph(byte name){
        super(name);
	}

	void setNegIn(Map<Action, Action> mapNegIn){
		negIn = mapNegIn;
	}
	
	public void setPosIn(Map<Action,Action> mapPosIn){
		posIn = mapPosIn;
	}
	
	public void setOut(Map<Action,Action> mapOut){
		out = mapOut;
	}
	
	/**
	 * Méthode qui copie cette proposition sans ses arcs.
	 * 
	 * @return
	 */
	public PropositionPGraph duplicateProposition(){
		return new PropositionPGraph(getName());
	}
    
    public PropositionPGraph duplicatePropositionPGraph(){
        return new PropositionPGraph(getName());
    }
	
	/**
	 * @return
	 */
	public Map<Action, Action> getNegInMap() {
		return negIn;
	}

	/**
	 * @return
	 */
	public Map<Action,Action> getOutMap() {
		return out;
	}

	/**
	 * @return
	 */
	public Map<Action,Action> getPosInMap() {
		return posIn;
	}
}
