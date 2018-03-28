package model.genetics;

import java.util.Random;

public class TraitData {
    private String[] values;
    private int split;

    public TraitData(int s, String[] v) {
        this.split = s;
        this.values = v;
    }

    public int random(Random r) {
        return r.nextInt(values.length);
    }

    public int randomSkewRecess (Random r) {
        for(int i = 0; i < 3; i++) {
            if(r.nextBoolean()) return r.nextInt(split);
        }
        return r.nextInt(values.length - split) + split;
    }
}
