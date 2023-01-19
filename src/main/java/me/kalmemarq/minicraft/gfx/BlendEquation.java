package me.kalmemarq.minicraft.gfx;

import me.kalmemarq.minicraft.util.math.MathHelper;

public class BlendEquation {
    public static final BlendEquation FUNC_ADD = new BlendEquation((colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor) -> {
        float sa = (colorSrc >> 24 & 0xFF) / 255.0f;
        float sr = (colorSrc >> 16 & 0xFF) / 255.0f;
        float sg = (colorSrc >> 8 & 0xFF) / 255.0f;
        float sb = (colorSrc & 0xFF) / 255.0f;

        float da = (colorDst >> 24 & 0xFF) / 255.0f;
        float dr = (colorDst >> 16 & 0xFF) / 255.0f;
        float dg = (colorDst >> 8 & 0xFF) / 255.0f;
        float db = (colorDst & 0xFF) / 255.0f;

        sr *= Math.abs(srcRGBFactor.get(sr, dr, sa, da));
        sg *= Math.abs(srcRGBFactor.get(sg, dg, sa, da));
        sb *= Math.abs(srcRGBFactor.get(sb, db, sa, da));
        sa *= Math.abs(srcAlphaFactor.get(sb, db, sa, da));

        dr *= Math.abs(dstRGBFactor.get(sr, dr, sa, da));
        dg *= Math.abs(dstRGBFactor.get(sg, dg, sa, da));
        db *= Math.abs(dstRGBFactor.get(sb, db, sa, da));
        da *= Math.abs(dstAlphaFactor.get(sb, db, sa, da));

        float cr = MathHelper.clamp(sr + dr, 0f, 1f);
        float cg = MathHelper.clamp(sg + dg, 0f, 1f);
        float cb = MathHelper.clamp(sb + db, 0f, 1f);
        float ca = MathHelper.clamp(sa + da, 0f, 1f);

        return (int)(ca * 255) << 24 | (int)(cr * 255) << 16 | (int)(cg * 255) << 8 | (int)(cb * 255);
    });

    public static final BlendEquation FUNC_SUBTRACT = new BlendEquation((colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor) -> {
        float sa = (colorSrc >> 24 & 0xFF) / 255.0f;
        float sr = (colorSrc >> 16 & 0xFF) / 255.0f;
        float sg = (colorSrc >> 8 & 0xFF) / 255.0f;
        float sb = (colorSrc & 0xFF) / 255.0f;

        float da = (colorDst >> 24 & 0xFF) / 255.0f;
        float dr = (colorDst >> 16 & 0xFF) / 255.0f;
        float dg = (colorDst >> 8 & 0xFF) / 255.0f;
        float db = (colorDst & 0xFF) / 255.0f;

        sr *= Math.abs(srcRGBFactor.get(sr, dr, sa, da));
        sg *= Math.abs(srcRGBFactor.get(sg, dg, sa, da));
        sb *= Math.abs(srcRGBFactor.get(sb, db, sa, da));
        sa *= Math.abs(srcAlphaFactor.get(sb, db, sa, da));

        dr *= Math.abs(dstRGBFactor.get(sr, dr, sa, da));
        dg *= Math.abs(dstRGBFactor.get(sg, dg, sa, da));
        db *= Math.abs(dstRGBFactor.get(sb, db, sa, da));
        da *= Math.abs(dstAlphaFactor.get(sb, db, sa, da));

        float cr = MathHelper.clamp(sr - dr, 0f, 1f);
        float cg = MathHelper.clamp(sg - dg, 0f, 1f);
        float cb = MathHelper.clamp(sb - db, 0f, 1f);
        float ca = MathHelper.clamp(sa - da, 0f, 1f);

        return (int)(ca * 255) << 24 | (int)(cr * 255) << 16 | (int)(cg * 255) << 8 | (int)(cb * 255);
    });

    public static final BlendEquation FUNC_REVERSE_SUBTRACT = new BlendEquation((colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor) -> {
        float sa = (colorSrc >> 24 & 0xFF) / 255.0f;
        float sr = (colorSrc >> 16 & 0xFF) / 255.0f;
        float sg = (colorSrc >> 8 & 0xFF) / 255.0f;
        float sb = (colorSrc & 0xFF) / 255.0f;

        float da = (colorDst >> 24 & 0xFF) / 255.0f;
        float dr = (colorDst >> 16 & 0xFF) / 255.0f;
        float dg = (colorDst >> 8 & 0xFF) / 255.0f;
        float db = (colorDst & 0xFF) / 255.0f;
        
        sr *= Math.abs(srcRGBFactor.get(sr, dr, sa, da));
        sg *= Math.abs(srcRGBFactor.get(sg, dg, sa, da));
        sb *= Math.abs(srcRGBFactor.get(sb, db, sa, da));
        sa *= Math.abs(srcAlphaFactor.get(sb, db, sa, da));

        dr *= Math.abs(dstRGBFactor.get(sr, dr, sa, da));
        dg *= Math.abs(dstRGBFactor.get(sg, dg, sa, da));
        db *= Math.abs(dstRGBFactor.get(sb, db, sa, da));
        da *= Math.abs(dstAlphaFactor.get(sb, db, sa, da));

        float cr = MathHelper.clamp(dr - sr, 0f, 1f);
        float cg = MathHelper.clamp(dg - sg, 0f, 1f);
        float cb = MathHelper.clamp(db - sb, 0f, 1f);
        float ca = MathHelper.clamp(da - sa, 0f, 1f);

        return (int)(ca * 255) << 24 | (int)(cr * 255) << 16 | (int)(cg * 255) << 8 | (int)(cb * 255);
    });

    private final Equation equation;

    private BlendEquation(Equation equation) {
        this.equation = equation;
    }

    public int calc(int colorSrc, int colorDst, BlendFactor srcRGBFactor, BlendFactor dstRGBFactor, BlendFactor srcAlphaFactor, BlendFactor dstAlphaFactor) {
        return this.equation.apply(colorSrc, colorDst, srcRGBFactor, dstRGBFactor, srcAlphaFactor, dstAlphaFactor);
    }

    interface Equation {
        int apply(int colorSrc, int colorDst, BlendFactor srcRGBFactor, BlendFactor dstRGBFactor, BlendFactor srcAlphaFactor, BlendFactor dstAlphaFactor);
    } 
}
