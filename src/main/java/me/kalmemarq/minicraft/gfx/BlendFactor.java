package me.kalmemarq.minicraft.gfx;

public class BlendFactor {
    public static final BlendFactor ZERO = new BlendFactor((sc, dc, sa, da) -> 0.0f);
    public static final BlendFactor ONE = new BlendFactor((sc, dc, sa, da) -> 1.0f);

    public static final BlendFactor SRC_COLOR = new BlendFactor((sc, dc, sa, da) -> sc);
    public static final BlendFactor DST_COLOR = new BlendFactor((sc, dc, sa, da) -> dc);
    
    public static final BlendFactor ONE_MINUS_SRC_COLOR = new BlendFactor((sc, dc, sa, da) -> 1.0f - sc);
    public static final BlendFactor ONE_MINUS_DST_COLOR = new BlendFactor((sc, dc, sa, da) -> 1.0f - dc);

    public static final BlendFactor SRC_ALPHA = new BlendFactor((sc, dc, sa, da) -> sa);
    public static final BlendFactor DST_ALPHA = new BlendFactor((sc, dc, sa, da) -> da);

    public static final BlendFactor ONE_MINUS_SRC_ALPHA = new BlendFactor((sc, dc, sa, da) -> 1.0f - sa);
    public static final BlendFactor ONE_MINUS_DST_ALPHA = new BlendFactor((sc, dc, sa, da) -> 1.0f - da);

    private final GetFactor getFactor;

    private BlendFactor(GetFactor getFactor) {
        this.getFactor = getFactor;
    }

    public float get(float sc, float dc, float sa, float da) {
        return this.getFactor.get(sc, dc, sa, da);
    }
  
    @FunctionalInterface
    interface GetFactor {
        float get(float sc, float dc, float sa, float da);
    }
}
