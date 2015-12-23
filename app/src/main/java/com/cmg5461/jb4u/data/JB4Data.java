package com.cmg5461.jb4u.data;

/**
 * Created by Chris on 11/10/2015.
 */
public enum JB4Data {
    GEAR((byte) 33),
    LAST_SAFETY((byte) 35),
    FP_LOW((byte) 36),
    METH((byte) 37),
    TRIMS((byte) 38),
    AVG_IGN((byte) 40),
    IGN_1((byte) 41),
    DME_BOOST_TARG((byte) 42),
    FUEL_OPEN_LOOP((byte) 43),
    FFPID((byte) 45),
    AFR((byte) 58),
    END_OF_LINE((byte) 59),
    WATER_TEMP((byte) 60),
    LOAD((byte) 61),
    OIL_TEMP((byte) 62),
    METH_SCALE((byte) 64),
    RPM((byte) 65),
    BOOST((byte) 66),
    PEDAL((byte) 67),
    PWM((byte) 68),
    FUELEN((byte) 69),
    MAP((byte) 70),
    IAT((byte) 71),
    CLOCK((byte) 72),
    IGN_2((byte) 73),
    IGN_3((byte) 74),
    IGN_5((byte) 75),
    TMAP_V((byte) 76),
    ECU_PSI((byte) 77),
    TARGET((byte) 78),
    IGN_4((byte) 79),
    METHPSI((byte) 80),
    N1_ENABLED((byte) 81),
    N1_METH_FLOW((byte) 82),
    N1_MIN_RPM((byte) 83),
    N1_MAX_RPM((byte) 84),
    N1_RAMP_RATE((byte) 85),
    N1_SHIFT_RED((byte) 86),
    THROTTLE((byte) 87),
    FP_HIGH((byte) 88),
    MISSING_FIRMWARE((byte) 89),
    DUTYCYCLE((byte) 94),
    METHSAFEMODE((byte) 96),
    CPS_VALUES((byte) 97),
    METHTRIGGERMODE((byte) 109),
    FUA((byte) 110),
    FUD((byte) 111),
    PID_GAIN((byte) 112),
    FIRMWARE((byte) 113),
    AUTO_SHIFT_RED((byte) 114),
    DWP((byte) 115),
    BOOST_SAFETY((byte) 116),
    FF((byte) 117),
    LIMIT_1ST((byte) 118),
    METHADD((byte) 119),
    LIMIT_2ND((byte) 120),
    IGN_6((byte) 121),
    AVGIGN((byte) 122),
    N1_MIN_GEAR((byte) 123),
    N1_MIN_ADV((byte) 124),
    N1_MIN_AFR((byte) 125),
    AFR2((byte) 126),;

    private byte b;

    JB4Data(byte b) {
        this.b = b;
    }

    public byte value() {
        return b;
    }

    public static JB4Data getJB4DataForCmd(byte b) {
        for (JB4Data data : values()) {
            if (data.value() == b) return data;
        }
        return null;
    }
}
