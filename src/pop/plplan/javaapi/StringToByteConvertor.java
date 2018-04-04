package pop.plplan.javaapi;

import java.util.ArrayList;
/**
 * This class is use to associate a Byte for each String in the world description. This is
 * used to optimize comparaison (comparing two bytes is faster than comparing two strings).
 * <p>
 * THE PLPLAN AI PLANNER
 * Author :  Philippe Fournier-Viger and Ludovic lebel
 * <p>
 * ENGLISH LICENSE  :
 * This work is licensed under the Creative Commons Attribution 2.5 License. To
 * view a copy of this license, visit
 * http://creativecommons.org/licenses/by/2.5/ or send a letter to Creative
 * Commons, 543 Howard Street, 5th Floor, San Francisco, California, 94105, USA.
 * <p>
 * The GRAPHPLAN algorithm has been implemented based on
 * the description of the algorithm in :
 * "Automated planning", 2004, Malik Ghallab, Dana nau et Paolo Traverso, 
 * Morgan Kaufman, ISBN 1-55860-856-7
 * <p>
 * @author Philippe Fournier-Viger and Ludovic Lebel
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pop.plplan.algorithms.Action;

/**
 * Add comment here.
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

public class StringToByteConvertor {
    
    // ON PEUT AVOIR AU MAXIMUM 128 PROPOSITION ET 128 ACTIONS
    
    public StringToByteConvertor(){
    }
    
    // Pour les propositions
    private Map<String, Byte> mapSCProp = new HashMap<String, Byte>(20);
    private Map<Byte, String> mapCSProp = new HashMap<Byte, String>(20);
    
    private byte nextByteProp = Byte.MIN_VALUE;
    
    public byte getIDForPropositionName(String name){
        if(mapSCProp.get(name) ==  null){
            mapSCProp.put(name, nextByteProp);
            mapCSProp.put(nextByteProp, name);
            return nextByteProp++;
        }
        return mapSCProp.get(name);
    }
    
    public String getPropositionNameForID(char value){
        return mapCSProp.get(value);
    }
    
    // Pour les actions
    private Map<String, Byte> mapSCAct = new HashMap<String, Byte>(20);
    private Map<Byte, String> mapCSAct = new HashMap<Byte, String>(20);
    
    private byte nextByteAct = Byte.MIN_VALUE + 128;
     
    public byte getIDForActionName(String name){
        if(mapSCAct.get(name) ==  null){
            mapSCAct.put(name, nextByteAct);
            mapCSAct.put(nextByteAct, name);
            return nextByteAct++;
        }
        return mapSCAct.get(name);
    }
    
    public String getActionNameForID(byte value){
        return mapCSAct.get(value);
    }
    
    
    public List convertIntSolutionToStringSolution(List liste){
        List resultat = new ArrayList();
        for(Object o : liste){
            if(o instanceof Action){
                Action act = ((Action)o);
                resultat.add(getActionNameForID(act.getName()));
            }else{
                List list = (List)o;
                resultat.add(convertIntSolutionToStringSolution(list));
            }
        }
        return resultat;
    }
}
