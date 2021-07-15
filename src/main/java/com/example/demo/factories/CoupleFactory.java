package com.example.demo.factories;

import com.example.demo.domein.models.Couple;

public final class CoupleFactory {
    public CoupleFactory() {
    }

    public static Couple execute(long firstEmplId, long secondEmplId, long overlapDuration) {
        return new Couple(
                firstEmplId,
                secondEmplId,
                overlapDuration);
    }
}
