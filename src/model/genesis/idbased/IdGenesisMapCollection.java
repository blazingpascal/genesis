package model.genesis.idbased;

import java.util.HashMap;

public class IdGenesisMapCollection {
	private static HashMap<String, IIdBasedGenesis> map = new HashMap<>();
	
	public static void addGenesis(IIdBasedGenesis genesis, int id){
		map.put(Integer.toString(id), genesis);
	}

	public static void addGenesis(IIdBasedGenesis genesis, String id){
		map.put(id, genesis);
	}
	
	public static IIdBasedGenesis getIdGenesis(int id){
		if(map.containsKey("SUPER_" + id)){
			return map.get("SUPER_" + id);
		}
		return map.get(id);
	}

	public static long getGenesisIdCount(int genesisId) {
		long idCount =  map.get(Integer.toString(genesisId)).getIdCount();
		return idCount;
	}
}
