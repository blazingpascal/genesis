package model.genetics;

import java.util.Random;

public class SingleTrait {
    private int value;
    private int maternal;
    private int paternal;

    public SingleTrait(int v, int m, int p) {
        this.value = v;
        this.maternal = m;
        this.paternal = p;
    }

    public SingleTrait(int v) {
        this.value = v;
        this.maternal = v;
        this.paternal = v;
    }

    public int get() {
        return value;
    }

    public int getRandomHistorical(Random r) {
        return r.nextBoolean() ? maternal : paternal;
    }
}
