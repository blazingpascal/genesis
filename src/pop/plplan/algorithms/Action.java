package pop.plplan.algorithms;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class that represent an action that can add and/or remove facts in the world.
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
public class Action
{
	private static final int HASH_CAPACITY = 4;
	private Set<Proposition> precondMap = null;
	private Set<Proposition> negEffectMap = null; 
	private Set<Proposition> posEffectMap = null;
	private boolean isNoop = false;
	
	public int[] marks = new int[20];
    
    private final byte name;
	
	public Action(byte name)
	{
        this.name = name;
        precondMap = new HashSet<Proposition>(HASH_CAPACITY);
        negEffectMap = new HashSet<Proposition>(HASH_CAPACITY); 
        posEffectMap = new HashSet<Proposition>(HASH_CAPACITY);
		for(int i = 0; i < marks.length;i++){
            marks[i] = 0;
        }
	}
	
	public Action(byte name, boolean isNoop)
	{
        this.name = name;
        
        precondMap = new HashSet<Proposition>(HASH_CAPACITY);
        negEffectMap = new HashSet<Proposition>(HASH_CAPACITY); 
        posEffectMap = new HashSet<Proposition>(HASH_CAPACITY);
		this.isNoop = isNoop;
		
		for(int i = 0; i < marks.length;i++){
            marks[i] = 0;
        }
	}
	
	public Action(byte name, Set<Proposition> precond, 
            Set<Proposition> posEffect, Set<Proposition> negEffect){
        this.name = name;
        
		precondMap = precond;
		negEffectMap = negEffect;
		posEffectMap = posEffect;
		
		for(int i = 0; i < marks.length;i++){
            marks[i] = 0;
        }
	}
	
	public void resetMark(int index){
		marks[index] = 0;
	}
	
	public boolean isMark(int index){
		return marks[index] > 0;
	}
	
	public Set<Proposition> getNegEffectSet() {
		return negEffectMap;
	}

	public Set<Proposition> getPosEffectSet() {
		return posEffectMap;
	}

	public Set<Proposition> getPrecondMap() {
		return precondMap;
	}

	/**
	 * V�rifie si une action est applicable pour un ensemble de propositions.
	 * @param propositionMap L'ensemble de propositions.
	 * @return Un bool�en.
	 */
	public boolean isApplicableForPropositions(Set<Proposition> propositionMap){
		Iterator<Proposition> iter = precondMap.iterator();
		for (int j = precondMap.size(); j > 0; j--) {
			if(!propositionMap.contains(iter.next())){
				return false;
			}
		}
		return true;
	}
    
    public boolean isApplicableForPropositions(Map<PropositionPGraph,PropositionPGraph> propositionMap){
        Iterator<Proposition> iter = precondMap.iterator();
        for (int j = precondMap.size(); j > 0; j--) {
            if(!propositionMap.containsKey(iter.next())){
                return false;
            }
        }
        return true;
    }
	
	/**
	 * V�rifie si une action est applicable pour un ensemble de propositions.
	 * @param propositionMap L'ensemble de propositions.
	 * @return Un bool�en.
	 */
	public boolean isApplicableForPropositions(List<Proposition> propList){
		Iterator<Proposition> iter = precondMap.iterator();
		for (int j = precondMap.size(); j > 0; j--) {
			if(!propList.contains(iter.next())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * M�thode qui indique si cette action a des propositions mutex par rapport a muPi-1
	 * @param prevMuProps La map de muPi-1.
	 * @return Un bool�een.
	 */
	public boolean hasMutexPreconditions(Map<Proposition, MutexProp> prevMuProps){ 
		Iterator<Proposition> iter = getPrecondMap().iterator();
		for (int j = getPrecondMap().size(); j > 0; j--) {
            MutexProp mutex = prevMuProps.get(iter.next());
			if(mutex != null && mutex.containsAtLeastOneOf(getPrecondMap().iterator())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * V�rifie si cette action est ind�pendante d'une autre.
	 * D�finition d'ind�pendance voir p119. Def 6.1.
	 * @param actionB L'action avec laquelle on compare.
	 * @return Un bool�en.
	 */
	public boolean isIndependentOf(Action actionB){
		Action actionA = this;
		// Il faut v�rifier pour A (this)
		Iterator<Proposition> iterA = actionA.negEffectMap.iterator();
		for (int j = actionA.negEffectMap.size(); j > 0; j--) {
			Proposition negEffectA = iterA.next();
			if(actionB.precondMap.contains(negEffectA) ||
			   actionB.posEffectMap.contains(negEffectA))
			{
				return false;
			}
		}
		
		// Ensuite on v�rife pour B
		Iterator<Proposition> iterB = actionB.negEffectMap.iterator();
		for (int j = actionB.negEffectMap.size(); j > 0; j--) {
			Proposition negEffectB = iterB.next();
			if(actionA.precondMap.contains(negEffectB) ||
			   actionA.posEffectMap.contains(negEffectB))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean isDependentOf(Action actionB){
		return !isIndependentOf(actionB);
	}

	public boolean isNoop(){
		return isNoop;
	}
    
    //---------------------- M�thodes standards

    /**
     * @return
     */
    public byte getName() {
        return name;
    }
    
    // Red�finition de equals
    public boolean equals(Object o){
//        if(o == null || !(o instanceof Action))
//            return false;
        return ((Action) o).getName() == name;
    } 
        
    // Red�finition de hashcode
    public int hashCode(){
        return name;
    }
    
    //Red�finition de toString()
    public String toString(){
        return "A" + name;
    }
}