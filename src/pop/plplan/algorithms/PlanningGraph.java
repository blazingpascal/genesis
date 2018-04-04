package pop.plplan.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The class that represent planning graphs (see graphplan theory for more information on
 * planning graphs).
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
public class PlanningGraph {

    //public final static String PREFIX_ACTION_NOOP = "noop";

    private List<Layer> layers = new ArrayList<Layer>(); // List d'objet Layer
    private Set<Action> opMap;
    private Set<Proposition> factMap;
    private int fixedPointLevel = -1;

    public PlanningGraph(Set<Action> ops, Set<Proposition> facts) {
        opMap = new HashSet<Action>(ops); // important de faire new sinon on corrompt
                                  // avec les noops la map d'opérateur de world
                                  // reader
        factMap = facts;

        // La première couche contient l'ensemble des faits
        Layer p0 = new Layer(opMap.size());
        
        // on copie les faits  de Proposition à PropositionPGraph
        Map<PropositionPGraph,PropositionPGraph> factsPGraph = new HashMap<PropositionPGraph,PropositionPGraph>();
        Iterator<Proposition> iter =  facts.iterator();
        while (iter.hasNext()) {
            PropositionPGraph temp = iter.next().duplicatePropositionPGraph();
            factsPGraph.put(temp,temp);
        }
        p0.setProps(factsPGraph);
            

        getLayers().add(p0);
    }

    /**
     * Méthode récursive de création du planning graph. Construit une couche
     * Ai/Pi par récursion. Algorithme p124 fig 6.5 de automated planning theory &
     * practice
     * 
     * @param prevPropstLayer
     *            La couche de proposition précédente.
     */
    public void expandGraph() {

        Layer newLayer = new Layer(opMap.size());
        Layer prevLayer = ((Layer) layers.get(layers.size() - 1));

        //(1) Construire Ai et Pi
        Set<Action> Ai = newLayer.getActs();
        Set<Action> AiWithoutNoop = newLayer.getActsWithoutNoop();
        Map<PropositionPGraph, PropositionPGraph> Pi = newLayer.getProps();

        // Ajout des action noop possibles
        createNoopActions(prevLayer);

        // On trouve toutes les autres actions applicables
        Iterator<Action> iter = getOps().iterator();
        while (iter.hasNext()) {
            Action action = iter.next();
            if (action.isApplicableForPropositions(prevLayer.getProps()) && 
                !action.hasMutexPreconditions(prevLayer.getMuProps())) 
            {
                Ai.add(action);
                
                if(!action.isNoop())
                    AiWithoutNoop.add(action);

                createNegArcs(Pi, action);
                createPosArcs(Pi, action);
                createOutArcs(prevLayer.getProps(), action);
            }
        }

        // (2) - Construire muAi

        createMuAi(newLayer, prevLayer, Ai);
        createMuPi(newLayer, Pi);

        getLayers().add(newLayer);

        if (isFixedPointLevel())
            fixedPointLevel = getLayers().size() - 1;
    }

    /**
     * @param newLayer
     * @param prevLayer
     * @param actsList
     */
    private void createMuAi(Layer newLayer, Layer prevLayer, Set<Action> acts) {
        Map<Action, MutexAction> muAi = newLayer.getMuActs();
        
        //Copie les éléments du map dans une liste
        List<Action> actsList = new ArrayList<Action>(acts.size());
        Iterator<Action> actIter = acts.iterator();
        while (actIter.hasNext()) actsList.add(actIter.next());

        for (int i = 0; i < actsList.size(); i++) {
            Action actionA = actsList.get(i);
            for (int j = i + 1; j < actsList.size(); j++) {
                Action actionB = actsList.get(j);

                // on vérifie si les actions A et B sont dépendantes
                if (actionA.isDependentOf(actionB)) {
                    newLayer.addNodeMutex(muAi, actionA, actionB);
                } else {
                    //on vérifie si les actions A et B partagent des précond
                    // élément de muPi-1
                    Iterator<Proposition> itrA = actionA.getPrecondMap().iterator();
                    while (itrA.hasNext()) {
                        Proposition precondA = itrA.next();
                        MutexProp mutex =  prevLayer.getMuProps().get(precondA);
                        if (mutex != null && 
                            mutex.containsAtLeastOneOf(actionB.getPrecondMap().iterator())) 
                        {
                            newLayer.addNodeMutex(muAi, actionA, actionB);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * @param pi
     * @param aiMutexMap
     * @param piMutexMap
     */
    private void createMuPi(Layer layer, Map<PropositionPGraph,PropositionPGraph> props) {
        
        //Copie les éléments du map dans une liste
        List<PropositionPGraph> propList = new ArrayList<PropositionPGraph>(props.size());
        Iterator<PropositionPGraph> propIter = props.values().iterator();
        while (propIter.hasNext()){
            propList.add(propIter.next());
        }

        for (int i = 0; i < propList.size(); i++) {
            PropositionPGraph p = propList.get(i);
            Collection<Action> pActs = p.getPosInMap().values();

            for (int j = i + 1; j < propList.size(); j++) {
                PropositionPGraph q = propList.get(j);
                Collection<Action> qActs = q.getPosInMap().values();
                
                boolean communeAct = false;
                Iterator<Action> iterP = pActs.iterator();
                while (iterP.hasNext() && !communeAct) {
                    communeAct = qActs.contains(iterP.next());
                }
                
                if(!communeAct)
                {
                    boolean isMutex = true;
                    iterP = pActs.iterator();
                    while (iterP.hasNext() && isMutex) {
                        MutexAction mutex = layer.getMuActs().get(iterP.next());
                        
                        if (mutex == null ||
                            mutex != null && !mutex.containsAll(qActs.iterator())){
                            isMutex = false;
                        }
                    }

                    if(isMutex){
                        layer.addNodeMutex(layer.getMuProps(), p, q);
                    }
                }
            }
        }
    }

    /**
     * Vérifie et met à jour les actions Noop pour la couche actuelle.
     * 
     * @param prevLayer
     *            La couche précédente.
     */
    public void createNoopActions(Layer prevLayer) {
        Iterator<PropositionPGraph> iter = prevLayer.getProps().values().iterator();
        while (iter.hasNext()) {
            Proposition prop = iter.next();
            Action newAction = new Action(//PREFIX_ACTION_NOOP +   J'AI MIS CECI EN COMMENTAIRE 2005! DANGER!
                    prop.getName(), true);
            if (!getOps().contains(newAction)) {
                getOps().add(newAction);
                newAction.getPosEffectSet().add(prop);
                newAction.getPrecondMap().add(prop);
            }
        }
    }

    /**
     * Méthode qui vérifie si un état contient toutes les propositions du but
     * 
     * @param state
     *            Une map contenant les proposition de l'états.
     * @return Un booléen.
     */
    public boolean isPiContainingAllGoalProposition(List<Proposition> goal) {
        Layer layer = (Layer) layers.get(layers.size() - 1);
        for(int i = 0; i < goal.size(); i++) {
            Proposition prop =  goal.get(i);
            if (!layer.getProps().containsKey(prop)) {
                return false;
            }

            MutexProp mutex = layer.getMuProps().get(prop);
            if (mutex != null &&
                mutex.containsAtLeastOneOf(goal.iterator())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Méthode pour vérifier si nous avons atteint un point fixe.
     * 
     * @return Un booléen
     */
    public boolean isFixedPointLevel() {
        if (layers.size() < 2)
            return false;
        Layer newLayer = layers.get(layers.size() - 1);
        Layer prevLayer = layers.get(layers.size() - 2);
        return newLayer.getProps().size() == prevLayer.getProps().size()
            && getNbMutexPair(newLayer.getMuProps()) == 
                getNbMutexPair(prevLayer.getMuProps()); 
    }
    
    /**
     * Calcule le nb de paire de propositions mutex
     * Elle compte (a,b) et (b,a) comme deux paires, à cause de notre structure.
     * 
     */
    public int getNbMutexPair(Map<Proposition, MutexProp> mutexMap) {
        int nb = 0;
        Iterator<MutexProp> iterMutex = mutexMap.values().iterator();
        while (iterMutex.hasNext()) {
            nb += iterMutex.next().getNbMutexPair();
        }
        return nb;
    }

    /**
     * Ajouter les liens négatif d'une action vers un état Pi.
     * 
     * @param Pi
     *            Une map de proposition (l'état).
     * @param action
     *            L'action.
     */
    public void createNegArcs(Map<PropositionPGraph, PropositionPGraph> Pi, Action action) {
        Iterator<Proposition> iter = action.getNegEffectSet().iterator();
        while (iter.hasNext()) {
            Proposition prop = iter.next();
            PropositionPGraph propPi = Pi.get(prop);

            if (propPi == null) {
                propPi = prop.duplicatePropositionPGraph();
                Pi.put(propPi, propPi);
            }
            propPi.getNegInMap().put(action, action);
        }
    }

    /**
     * Ajouter les liens positifs d'une action vers un état Pi.
     * 
     * @param Pi
     *            Une map de proposition (l'état).
     * @param action
     *            L'action.
     */
    public void createPosArcs(Map<PropositionPGraph,PropositionPGraph> Pi, Action action) {
        Iterator<Proposition> iter = action.getPosEffectSet().iterator();
        while (iter.hasNext()) {
            Proposition prop = iter.next();
            PropositionPGraph propPi = Pi.get(prop);

            if (propPi == null) {
                propPi = prop.duplicatePropositionPGraph();
                Pi.put(propPi, propPi);
            }
            propPi.getPosInMap().put(action, action);
        }
    }

    /**
     * Ajouter les liens préconds d'une action vers un état Pi-1.
     * 
     * @param prevPi
     *            Une map de proposition (l'état).
     * @param action
     *            L'action.
     */
    public void createOutArcs(Map<PropositionPGraph,PropositionPGraph> prevPi, Action action) {
        Iterator<Proposition> iter = action.getPrecondMap().iterator();
        while (iter.hasNext()) {
            prevPi.get(iter.next()).getOutMap().put(action, action);
        }
    }
    
    public Layer getLayerAt(int index){
        return getLayers().get(index);
    }

    //  Getters & setters
    public Set<Proposition> getFacts() {
        return factMap;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public Set<Action> getOps() {
        return opMap;
    }

    public int getFixedPointLevel() {
        return fixedPointLevel;
    }

    /**
     * @param fixedPointLevel
     * @return
     */
    public Map<List<Proposition>, List<Proposition>> getNoGoodTableAt(int index) {
        
        return layers.get(index).getNoGoodTable();
    }

    /**
     * @return
     */
    public int getNbLayers() {
        return layers.size();
    }
}