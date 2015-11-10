package com.cmg5461.jb4u.data;

/**
 * Created by Chris on 11/10/2015.
 */
public enum JB4Data {
    GEAR((byte) 33),
    LEARNED_FF((byte) 35),
    OILTEMP((byte) 36),
    METH((byte) 37),
    EGT((byte) 38),
    AVG_IGN((byte) 40),
    IGN_ADV((byte) 41),
    DME_BOOST_TARG((byte) 42),
    FPS_SAFETY((byte) 43),
    FFPID((byte) 45),
    AFR2((byte) 58),
    CPS((byte) 61),
    METH_RANGE((byte) 64),
    RPM((byte) 65),
    BOOST((byte) 66),
    TPS((byte) 67),
    PWM((byte) 68),
    FUEL((byte) 69),
    MAP((byte) 70),
    IAT((byte) 71),
    KR2((byte) 73),
    KR3((byte) 74),
    KR5((byte) 75),
    AMBIENTT((byte) 76),
    DME_BOOST((byte) 77),
    DME_TARGET((byte) 78),
    KR4((byte) 79),
    N1_MIN_PSI((byte) 80),
    N1_ENABLED((byte) 81),
    N1_METH_FLOW((byte) 82),
    N1_MIN_RPM((byte) 83),
    N1_MAX_RPM((byte) 84),
    N1_RAMP_RATE((byte) 85),
    N1_SHIFT_RED((byte) 86),
    ACCEL((byte) 87),
    FUEL_PRESSURE((byte) 88),
    MISSING_FIRMWARE((byte) 89),
    AFR((byte) 94),
    CPSOFFSET((byte) 96),
    M1_BOG_FIX((byte) 109),
    M1_FUEL((byte) 110),
    DPS_STRENGTH((byte) 111),
    M1_PID_GAIN((byte) 112),
    JB3_FIRMWARE((byte) 113),
    M1_THROTTLE((byte) 114),
    M1_LAGFIX((byte) 115),
    M1_BOOST_LIMIT((byte) 116),
    M1_LEAN_CRUISE((byte) 117),
    TRACTION_ASSIST((byte) 118),
    M1_METH_HARD((byte) 119),
    AUTOSTR((byte) 120),
    KR6((byte) 121),
    AVG_IGN_DROP((byte) 122),
    N1_MIN_GEAR((byte) 123),
    N1_MIN_ADV((byte) 124),
    N1_MIN_AFT((byte) 125),
    KR1((byte) 126),;

    private byte b;

    JB4Data(byte b) {
        this.b = b;
    }

    public byte value() {
        return b;
    }
}
