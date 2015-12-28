package com.cmg5461.jb4u.log;

/**
 * Created by Chris on 11/10/2015.
 */
public class LogPoint {
    public long timestamp;
    public int iat;
    public int rpm;
    public double boost;
    public int pedal; // pedal
    public int map;
    public int clock; // timestamp
    public int pwm;
    public int fuelen; // fuelen
    public int trims;
    public int gear;
    public double dutycycle;
    public int meth;
    public int fp_l;
    public int ff;
    public double afr;
    public double ecu_psi; // ECU psi
    public double avg_ign;
    public double dme_bt; // ecu boost target
    public double target; // real target
    public double N1Enabled;
    public double last_safety;
    public double N1MinGear;
    public double fp_h;
    public double N1ShiftRed; // auto shift reduction
    public double load;
    public double CpsSafety;
    public double N1MethFlow;
    public double N1MinRpm;
    public double N1MaxRpm;
    public double N1RampRate;
    public int throttle;
    public double N1MaxGear;
    public double N1MinAfr;
    public double N1MinAdv;
    public double afr2;
    public double ign_1;
    public double ign_2;
    public double ign_3;
    public double ign_4;
    public double ign_5;
    public double ign_6;
    public int waterTemp;
    public int oilTemp;

    public LogPoint() {
    }

    public LogPoint(boolean populate) {
        timestamp = 0L;
        rpm = -1;
        boost = 0.0D;
        pedal = -1;
        iat = -1;
        map = -1;
        clock = -1;
        pwm = -1;
        fuelen = -1;
        trims = -1;
        gear = -1;
        dutycycle = 0.0D;
        meth = -1;
        fp_l = -1;
        ff = -1;
        afr = 0.0D;
        ecu_psi = 0.0D;
        ign_1 = 0.0D;
        avg_ign = 0.0D;
        dme_bt = 0.0D;
        target = 0.0D;
        fp_h = 0.0D;
        N1Enabled = 0.0D;
        N1MethFlow = 0.0D;
        N1MinRpm = 0.0D;
        N1MaxRpm = 0.0D;
        N1RampRate = 0.0D;
        N1ShiftRed = 0.0D;
        CpsSafety = 0.0D;
        load = 0.0D;
        last_safety = 0.0D;
        throttle = -1;
        N1MinGear = 0.0D;
        N1MaxGear = 0.0D;
        N1MinAfr = 0.0D;
        N1MinAdv = 0.0D;
        afr2 = 0.0D;
        ign_2 = 0.0D;
        ign_3 = 0.0D;
        ign_4 = 0.0D;
        ign_5 = 0.0D;
        ign_6 = 0.0D;
        waterTemp = -1;
        oilTemp = -1;
    }

    public static LogPoint clone(LogPoint in) {
        LogPoint out = new LogPoint();
        out.timestamp = in.timestamp;
        out.rpm = in.rpm;
        out.boost = in.boost;
        out.pedal = in.pedal;
        out.iat = in.iat;
        out.map = in.map;
        out.clock = in.clock;
        out.pwm = in.pwm;
        out.fuelen = in.fuelen;
        out.trims = in.trims;
        out.gear = in.gear;
        out.dutycycle = in.dutycycle;
        out.meth = in.meth;
        out.fp_l = in.fp_l;
        out.ff = in.ff;
        out.afr = in.afr;
        out.ecu_psi = in.ecu_psi;
        out.ign_1 = in.ign_1;
        out.avg_ign = in.avg_ign;
        out.dme_bt = in.dme_bt;
        out.target = in.target;
        out.fp_h = in.fp_h;
        out.N1Enabled = in.N1Enabled;
        out.N1MethFlow = in.N1MethFlow;
        out.N1MinRpm = in.N1MinRpm;
        out.N1MaxRpm = in.N1MaxRpm;
        out.N1RampRate = in.N1RampRate;
        out.N1ShiftRed = in.N1ShiftRed;
        out.CpsSafety = in.CpsSafety;
        out.load = in.load;
        out.last_safety = in.last_safety;
        out.throttle = in.throttle;
        out.N1MinGear = in.N1MinGear;
        out.N1MaxGear = in.N1MaxGear;
        out.N1MinAfr = in.N1MinAfr;
        out.N1MinAdv = in.N1MinAdv;
        out.afr2 = in.afr2;
        out.ign_2 = in.ign_2;
        out.ign_3 = in.ign_3;
        out.ign_4 = in.ign_4;
        out.ign_5 = in.ign_5;
        out.ign_6 = in.ign_6;
        out.waterTemp = in.waterTemp;
        out.oilTemp = in.oilTemp;
        return out;
    }

    public static void Copy(LogPoint in, LogPoint out) {
        out.timestamp = in.timestamp;
        out.rpm = in.rpm;
        out.boost = in.boost;
        out.pedal = in.pedal;
        out.iat = in.iat;
        out.map = in.map;
        out.clock = in.clock;
        out.pwm = in.pwm;
        out.fuelen = in.fuelen;
        out.trims = in.trims;
        out.gear = in.gear;
        out.dutycycle = in.dutycycle;
        out.meth = in.meth;
        out.fp_l = in.fp_l;
        out.ff = in.ff;
        out.afr = in.afr;
        out.ecu_psi = in.ecu_psi;
        out.ign_1 = in.ign_1;
        out.avg_ign = in.avg_ign;
        out.dme_bt = in.dme_bt;
        out.target = in.target;
        out.fp_h = in.fp_h;
        out.N1Enabled = in.N1Enabled;
        out.N1MethFlow = in.N1MethFlow;
        out.N1MinRpm = in.N1MinRpm;
        out.N1MaxRpm = in.N1MaxRpm;
        out.N1RampRate = in.N1RampRate;
        out.N1ShiftRed = in.N1ShiftRed;
        out.CpsSafety = in.CpsSafety;
        out.load = in.load;
        out.last_safety = in.last_safety;
        out.throttle = in.throttle;
        out.N1MinGear = in.N1MinGear;
        out.N1MaxGear = in.N1MaxGear;
        out.N1MinAfr = in.N1MinAfr;
        out.N1MinAdv = in.N1MinAdv;
        out.afr2 = in.afr2;
        out.ign_2 = in.ign_2;
        out.ign_3 = in.ign_3;
        out.ign_4 = in.ign_4;
        out.ign_5 = in.ign_5;
        out.ign_6 = in.ign_6;
        out.waterTemp = in.waterTemp;
        out.oilTemp = in.oilTemp;
    }

    public static String getJB4LogPointData(LogPoint lp, long startTime) {
        StringBuilder sb = new StringBuilder();
        sb.append((lp.timestamp - startTime) / 100D).append(",");
        sb.append(lp.rpm).append(",");
        sb.append(lp.ecu_psi).append(",");
        sb.append(lp.target).append(",");
        sb.append(lp.boost).append(",");
        sb.append(lp.pedal).append(",");
        sb.append(lp.iat).append(",");
        sb.append(lp.fuelen).append(",");
        sb.append(lp.pwm).append(",");
        sb.append(lp.throttle).append(",");
        sb.append(lp.fp_h).append(",");
        sb.append(lp.ign_1).append(",");
        sb.append(lp.avg_ign).append(",");
        sb.append(lp.dutycycle).append(",");
        sb.append(lp.trims).append(",");
        sb.append(lp.dme_bt).append(",");
        sb.append(lp.meth).append(",");
        sb.append(lp.fp_l).append(",");
        sb.append(lp.afr).append(",");
        sb.append(lp.gear).append(",");
        sb.append(lp.ff).append(",");
        sb.append(lp.load).append(",");
        sb.append(lp.clock).append(",");
        sb.append(lp.map).append(",");
        sb.append(lp.afr2).append(",");
        sb.append(lp.ign_2).append(",");
        sb.append(lp.ign_3).append(",");
        sb.append(lp.ign_4).append(",");
        sb.append(lp.ign_5).append(",");
        sb.append(lp.ign_6).append(",\r\n"); // jb4 log
        return sb.toString();
    }

    public static String getCsvHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("timestamp,");
        sb.append("rpm,");
        sb.append("ecu_psi,");
        sb.append("target,");
        sb.append("boost,");
        sb.append("pedal,");
        sb.append("iat,");
        sb.append("fuelen,");
        sb.append("pwm,");
        sb.append("throttle,");
        sb.append("fp_h,");
        sb.append("ign_1,");
        sb.append("avg_ign,");
        sb.append("dutycycle,");
        sb.append("trims,");
        sb.append("dme_bt,");
        sb.append("meth,");
        sb.append("fp_l,");
        sb.append("afr,");
        sb.append("gear,");
        sb.append("ff,");
        sb.append("load,");
        sb.append("clock,");
        sb.append("map,");
        sb.append("afr2,");
        sb.append("ign_2,");
        sb.append("ign_3,");
        sb.append("ign_4,");
        sb.append("ign_5,");
        sb.append("ign_6,"); // jb4 log

        sb.append("N1Enabled,");
        sb.append("N1MethFlow,");
        sb.append("N1MinRpm,");
        sb.append("N1MaxRpm,");
        sb.append("N1RampRate,");
        sb.append("N1ShiftRed,");
        sb.append("CpsSafety,");
        sb.append("N1MinGear,");
        sb.append("N1MaxGear,");
        sb.append("N1MinAfr,");
        sb.append("N1MinAdv,");
        sb.append("WaterTemp,");
        sb.append("OilTemp\r\n");
        return sb.toString();
    }

    public static String getCsvString(LogPoint lp) {
        StringBuilder sb = new StringBuilder();
        sb.append(lp.timestamp).append(",");
        sb.append(lp.rpm).append(",");
        sb.append(lp.ecu_psi).append(",");
        sb.append(lp.target).append(",");
        sb.append(lp.boost).append(",");
        sb.append(lp.pedal).append(",");
        sb.append(lp.iat).append(",");
        sb.append(lp.fuelen).append(",");
        sb.append(lp.pwm).append(",");
        sb.append(lp.throttle).append(",");
        sb.append(lp.fp_h).append(",");
        sb.append(lp.ign_1).append(",");
        sb.append(lp.avg_ign).append(",");
        sb.append(lp.dutycycle).append(",");
        sb.append(lp.trims).append(",");
        sb.append(lp.dme_bt).append(",");
        sb.append(lp.meth).append(",");
        sb.append(lp.fp_l).append(",");
        sb.append(lp.afr).append(",");
        sb.append(lp.gear).append(",");
        sb.append(lp.ff).append(",");
        sb.append(lp.load).append(",");
        sb.append(lp.clock).append(",");
        sb.append(lp.map).append(",");
        sb.append(lp.afr2).append(",");
        sb.append(lp.ign_2).append(",");
        sb.append(lp.ign_3).append(",");
        sb.append(lp.ign_4).append(",");
        sb.append(lp.ign_5).append(",");
        sb.append(lp.ign_6).append(","); // jb4 log

        sb.append(lp.N1Enabled).append(",");
        sb.append(lp.N1MethFlow).append(",");
        sb.append(lp.N1MinRpm).append(",");
        sb.append(lp.N1MaxRpm).append(",");
        sb.append(lp.N1RampRate).append(",");
        sb.append(lp.N1ShiftRed).append(",");
        sb.append(lp.CpsSafety).append(",");
        sb.append(lp.N1MinGear).append(",");
        sb.append(lp.N1MaxGear).append(",");
        sb.append(lp.N1MinAfr).append(",");
        sb.append(lp.N1MinAdv).append(",");
        sb.append(lp.waterTemp).append(",");
        sb.append(lp.oilTemp).append(",\r\n"); // others
        return sb.toString();
    }
}
