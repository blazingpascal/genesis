package model;

import java.util.HashMap;

class IdGenesisMapCollection {
	private static HashMap<Integer, IIdBasedGenesis> map = new HashMap<>();
	
	public static void addGenesis(IIdBasedGenesis genesis, int id){
		map.put(id, genesis);
	}
	
	public static IIdBasedGenesis getIdGenesis(int id){
		return map.get(id);
	}

	public static long getGenesisIdCount(int genesisId) {
		long idCount =  map.get(genesisId).getIdCount();
		return idCount;
	}
}
