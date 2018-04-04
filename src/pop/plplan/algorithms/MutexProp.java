/*
 * Created on 2004-07-18
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package pop.plplan.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represent mutex  to be part of a planning graph (see planning graph theory in
 * graphplan articles for more information).
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
class MutexProp {

    private final Proposition node;
    private final Map<Proposition,Proposition> mutexNodes = new HashMap<Proposition,Proposition>();
    private final ArrayList<Proposition> mutexList = new ArrayList<Proposition>();
    private final int hashCode;

    public MutexProp(Proposition node) {
        this.node = node;
        hashCode = getNode().hashCode();
    }

    public Proposition getNode() {
        return node;
    }
    
    public int getNbMutexPair(){
        return mutexNodes.size();
    }
    
    // Redéfinitions de hashcode
    public int hashCode() {
        return hashCode;
    }

    public boolean containsAtLeastOneOf(Iterator<Proposition> nodeItr) {
        while (nodeItr.hasNext()) {
            if(mutexNodes.containsKey(nodeItr.next())) return true;
        }
        return false;
    }
    
    public boolean containsAll(Iterator<Proposition> nodeItr) {
        while (nodeItr.hasNext()) {
            if(!mutexNodes.containsKey(nodeItr.next())) return false;
        }
        return true;
    }
    
    /**
     * Vérifie si un noeud est présent dans la Map mutexNodes
     * @return
     */
    public boolean containsNode(Proposition node) {
        return mutexNodes.containsKey(node);
    }

    public void add(Proposition mutexNode) {
        mutexNodes.put(mutexNode, mutexNode);
        mutexList.add(mutexNode);
    }

    public void addAll(Iterator<Proposition> iter) {
        while (iter.hasNext()) {
            Proposition node = iter.next();
            mutexNodes.put(node, node); 
            mutexList.add(node);
        }
    }
    
    public Iterator<Proposition> getIterator(){
        return mutexNodes.keySet().iterator();
    }
    
    public String toString(){
        return "MUTEX[" + node  + " : " + mutexNodes +  "]";
    }

}
