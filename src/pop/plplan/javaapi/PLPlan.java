package pop.plplan.javaapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pop.plplan.algorithms.AbstractPlanner;
import pop.plplan.algorithms.Action;
import pop.plplan.algorithms.BWGraphplan;
import pop.plplan.algorithms.FWPlanner;
import pop.plplan.algorithms.Proposition;
import pop.plplan.algorithms.WorldReader;

/**
 * This is the main class to use PLPlan. It contains methods (1) to create
 * facts/operators/goal, (2) to load facts/operators/goal from files (3) to choose
 * the planning algorithm and (4) to find a plan.
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
public class PLPlan {

    private Set<Action> ops = new HashSet<Action>();

    private Set<Proposition> facts = new HashSet<Proposition>();

    private List<Proposition> goal = new ArrayList<Proposition>();

    private String planningOutput;

    private StringToByteConvertor convertor = new StringToByteConvertor();

    private EnumAlgorithm algorithm = EnumAlgorithm.GRAPHPLAN;

    public PLPlan() {

    }

    /**
     * Find a plan with the current selected algorithm, for the current world.
     * 
     * @return The plan, as a list containing actions. The list may contains
     *         list, if the Graphplan algorithm is used.
     * @throws PlPlanException
     *             If errors occur.
     */
    public List findPlan() throws PlPlanException {
        AbstractPlanner planner = getChoosenGraphPlan();
        List resultat = null;
        try {
            resultat = planner.run();
        } catch (OutOfMemoryError e) {
            throw new PlPlanException("Out of memory");
        }
        planningOutput = planner.getPlanningOutput();
        return convertor.convertIntSolutionToStringSolution(resultat);
    }

    /**
     * Set the planning algorithm to be used.
     * 
     * @param algorithm
     */
    public void setAlgorithm(EnumAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    private AbstractPlanner getChoosenGraphPlan() {
        switch (algorithm) {
        case GRAPHPLAN: // Graphplan (arrière)
            return new BWGraphplan(ops, facts, goal);
        case FW_0: // Simple forward search
            FWPlanner fg = new FWPlanner(ops, facts, goal);
            fg.setAlgorithmePersistentSet(0);
            fg.setWasSeenEnabled(false);
            fg.setSleepSetEnabled(false);
            return fg;
        case FW_SM_0: // Simple forward search with state matching
            FWPlanner fg2 = new FWPlanner(ops, facts, goal);
            fg2.setAlgorithmePersistentSet(0);
            fg2.setWasSeenEnabled(true);
            fg2.setSleepSetEnabled(false);
            return fg2;
        case FW_SS_0: // Forward search, with sleep sets
            FWPlanner fg3 = new FWPlanner(ops, facts, goal);
            fg3.setAlgorithmePersistentSet(0);
            fg3.setWasSeenEnabled(false);
            fg3.setSleepSetEnabled(true);
            return fg3;
        case FW_SS_SM_0: // Forward search with sleep set and state matching
            FWPlanner fg4 = new FWPlanner(ops, facts, goal);
            fg4.setAlgorithmePersistentSet(0);
            fg4.setWasSeenEnabled(true);
            fg4.setSleepSetEnabled(true);
            return fg4;
        case FW_1: // Forward search with persistant sets
            // Because of our persistant sets implementation, it is not
            // guaranteed to always find best solution
            FWPlanner fg5 = new FWPlanner(ops, facts, goal);
            fg5.setAlgorithmePersistentSet(1);
            fg5.setWasSeenEnabled(false);
            fg5.setSleepSetEnabled(false);
            return fg5;
        case FW_SM_1: // Forward search with persistant sets and state
            // matching
            // Because of our persistant sets implementation, it is not
            // guaranteed to always find best solution
            FWPlanner fg6 = new FWPlanner(ops, facts, goal);
            fg6.setAlgorithmePersistentSet(1);
            fg6.setWasSeenEnabled(true);
            fg6.setSleepSetEnabled(false);
            return fg6;
        case FW_SS_1: // Forward search with persistant sets and sleep sets
            // Because of our persistant sets implementation, it is not
            // guaranteed to always find the best solution
            FWPlanner fg7 = new FWPlanner(ops, facts, goal);
            fg7.setAlgorithmePersistentSet(1);
            fg7.setWasSeenEnabled(false);
            fg7.setSleepSetEnabled(true);
            return fg7;
        case FW_SS_SM_1: // Forward search with persistant sets, sleep sets
            // and state matching
            // Because of our persistant sets implementation, it is not
            // guaranteed to always find the best solution
            FWPlanner fg8 = new FWPlanner(ops, facts, goal);
            fg8.setAlgorithmePersistentSet(1);
            fg8.setWasSeenEnabled(true);
            fg8.setSleepSetEnabled(true);
            return fg8;
        }
        return null;
    }

    /**
     * Read goal(s) from a file.
     * 
     * @param filePath
     *            The path to a file containing goals description.
     * @throws PlPlanException
     *             If errors occur.
     */
    public void readGoalFromFile(String filePath) throws PlPlanException {
        WorldReader worldReader = new WorldReader(convertor);
        try {
            worldReader.parseProbFile(filePath);
        } catch (Exception e) {
            throw new PlPlanException("PLPLan.readFromFile() " + e, e);
        }
        goal = worldReader.getGoalList();
    }

    /**
     * Read fact(s) from a file.
     * 
     * @param filePath
     *            The path to a file containing facts description.
     * @throws PlPlanException
     *             If errors occur.
     */
    public void readFactsFromFile(String filePath) throws PlPlanException {
        WorldReader worldReader = new WorldReader(convertor);
        try {
            worldReader.parseProbFile(filePath);
        } catch (Exception e) {
            throw new PlPlanException("PLPLan.readFromFile() " + e, e);
        }
        facts = worldReader.getFactSet();
    }

    /**
     * Read operator(s) from a file.
     * 
     * @param filePath
     *            The path to a file containing operators description.
     * @throws PlPlanException
     *             If errors occur.
     */
    public void readOperatorsFromFile(String filePath) throws PlPlanException {
        WorldReader worldReader = new WorldReader(convertor);
        try {
            worldReader.parseOpsFile(filePath);
        } catch (Exception e) {
            throw new PlPlanException("PLPLan.readFromFile() " + e, e);
        }
        ops = worldReader.getOpSet();
    }

    /**
     * Add a new fact to the current world description.
     */
    public void addFact(String propositionString) {
        Proposition prop = new Proposition(convertor
                .getIDForPropositionName(propositionString));
        facts.add(prop);
    }

    /**
     * Add a new goal to the current world description.
     */
    public void addGoalFact(String propositionString) {
        Proposition prop = new Proposition(convertor
                .getIDForPropositionName(propositionString));
        goal.add(prop);
    }

    /**
     * Add a new goal to the current world description.
     */
    public void addOperator(String name, List<String> precond,
            List<String> neg, List<String> pos) {
        Set<Proposition> precondMap = new HashSet<Proposition>();
        Set<Proposition> negMap = new HashSet<Proposition>();
        Set<Proposition> posMap = new HashSet<Proposition>();
        for (String val : precond) {
            Proposition prop = new Proposition(convertor
                    .getIDForPropositionName(val));
            precondMap.add(prop);
        }
        for (String val : neg) {
            Proposition prop = new Proposition(convertor
                    .getIDForPropositionName(val));
            negMap.add(prop);
        }
        for (String val : pos) {
            Proposition prop = new Proposition(convertor
                    .getIDForPropositionName(val));
            posMap.add(prop);
        }
        Action operator = new Action(convertor.getIDForActionName(name),
                precondMap, posMap, negMap);
        ops.add(operator);
    }

    /**
     * Get a string that give informations about a planning task achievement.
     * Used for tests.
     * 
     * @return String.
     */
    public String getPlanningOutput() {
        return planningOutput;
    }
}
