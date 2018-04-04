package pop.plplan.javaapi;

/**
 * The enumeration for planning algorithm supported by PLPlan
 * 
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
public enum EnumAlgorithm {
    GRAPHPLAN, // The GRAPHPLAN algorithm has been implemented based on
    // the description of the algorithm in : "Automated planning", 2004, Malik Ghallab, 
    // Dana nau et Paolo Traverso, Morgan Kaufman, ISBN 1-55860-856-7
    
    FW_0,   // Simple forward search
    FW_SM_0, // Forward search with state matching
    FW_SS_0,  // Forward search with sleep sets
    FW_SS_SM_0, // Forward search with sleep sets and state matching
    
    //Note : Because of our implementation of persistant sets, the four following
    // algorithms does not always return optimal plans.
    
    FW_1,  // Forward search with persistant sets
    FW_SM_1, // Forward search with persistant sets and state matching
    FW_SS_1,  // Forward search with persistant sets  and sleep sets
    FW_SS_SM_1}; // Forward search with sleep sets, state matching and persistant sets
