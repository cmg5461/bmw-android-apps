package com.cmg5461.jb4u.log;

/**
 * Created by Chris on 11/10/2015.
 */
public class DetailLogPoint {
    public long Timestamp;
    public double CpsOffset;
    public double IAT;
    public int Rpm;
    public double Boost;
    public double Tps; // Pedal
    public int Map;
    public int Clock; // timestamp
    public int Pwm;
    public int Fuel; // fuelen
    public double Egt;
    public int Gear;
    public double Afr;
    public double Meth;
    public double OilTemp;
    public int FFPid;
    public double Afr2;
    public double Ambientv;
    public double DmeBoost; // ECU psi
    public double IgnAdv;
    public double AvgIgn;
    public double AvgIgnDrop;
    public double Dmebt; // ecu boost target
    public double DmeTarget; // real target
    public double M1Fuel;
    public double M1PidGain; // pid
    public double M1MethRange;
    public double M1LagFix;
    public double TractionAssist;
    public double M1MethHard;
    public double FpsSafety;
    public double N1Enabled;
    public double M1LeanCruise;
    public double Kr3;
    public double LearnedFF;
    public double N1MinGear;
    public double FuelPressure;
    public double N1ShiftRed; // auto shift reduction
    public double M1Throttle; // throttle
    public double Cps;
    public double BoostLimit; // boost limit
    public double AutoStr;
    public double CpsSafety;
    public double M1BogFix;
    public String Firmware;
    public double N1MinPsi;
    public double DpsStrength; // FP_L
    public double N1MethFlow;
    public double N1MinRpm;
    public double N1MaxRpm;
    public double N1RampRate;
    public double Accel;
    public double N1MaxGear;
    public double N1MinAfr;
    public double N1MinAdv;
    public double Kr1;
    public double Kr2;
    public double Kr4;
    public double Kr5;
    public double Kr6;
    public int N20_Tmap;


    public DetailLogPoint() {
    }

    public DetailLogPoint(boolean populate) {
        Timestamp = 0L;
        Rpm = -1;
        Boost = 0.0D;
        Tps = 0.0D;
        IAT = 0.0D;
        Map = -1;
        Clock = -1;
        Pwm = -1;
        Fuel = -1;
        Egt = 0.0D;
        Gear = -1;
        Afr = 0.0D;
        Meth = 0.0D;
        OilTemp = 0.0D;
        FFPid = -1;
        Afr2 = 0.0D;
        Ambientv = 0.0D;
        DmeBoost = 0.0D;
        IgnAdv = 0.0D;
        AvgIgn = 0.0D;
        AvgIgnDrop = 0.0D;
        Dmebt = 0.0D;
        DmeTarget = 0.0D;
        M1Fuel = 0.0D;
        M1PidGain = 0.0D;
        M1Throttle = 0.0D;
        M1LagFix = 0.0D;
        BoostLimit = 0.0D;
        M1LeanCruise = 0.0D;
        TractionAssist = 0.0D;
        AutoStr = 0.0D;
        M1MethHard = 0.0D;
        M1MethRange = 0.0D;
        M1BogFix = 0.0D;
        DpsStrength = 0.0D;
        FuelPressure = 0.0D;
        Firmware = "";
        N1MinPsi = 0.0D;
        N1Enabled = 0.0D;
        N1MethFlow = 0.0D;
        N1MinRpm = 0.0D;
        N1MaxRpm = 0.0D;
        N1RampRate = 0.0D;
        N1ShiftRed = 0.0D;
        FpsSafety = 0.0D;
        CpsOffset = 0.0D;
        CpsSafety = 0.0D;
        Cps = 0.0D;
        LearnedFF = 0.0D;
        Accel = 0.0D;
        N1MinGear = 0.0D;
        N1MaxGear = 0.0D;
        N1MinAfr = 0.0D;
        N1MinAdv = 0.0D;
        Kr1 = 0.0D;
        Kr2 = 0.0D;
        Kr3 = 0.0D;
        Kr4 = 0.0D;
        Kr5 = 0.0D;
        Kr6 = 0.0D;
        N20_Tmap = 0;
    }

    public static void Copy(DetailLogPoint in, DetailLogPoint out) {
        out.Timestamp = in.Timestamp;
        out.Rpm = in.Rpm;
        out.Boost = in.Boost;
        out.Tps = in.Tps;
        out.IAT = in.IAT;
        out.Map = in.Map;
        out.Clock = in.Clock;
        out.Pwm = in.Pwm;
        out.Fuel = in.Fuel;
        out.Egt = in.Egt;
        out.Gear = in.Gear;
        out.Afr = in.Afr;
        out.Meth = in.Meth;
        out.OilTemp = in.OilTemp;
        out.FFPid = in.FFPid;
        out.Afr2 = in.Afr2;
        out.Ambientv = in.Ambientv;
        out.DmeBoost = in.DmeBoost;
        out.IgnAdv = in.IgnAdv;
        out.AvgIgn = in.AvgIgn;
        out.AvgIgnDrop = in.AvgIgnDrop;
        out.Dmebt = in.Dmebt;
        out.DmeTarget = in.DmeTarget;
        out.M1Fuel = in.M1Fuel;
        out.M1PidGain = in.M1PidGain;
        out.M1Throttle = in.M1Throttle;
        out.M1LagFix = in.M1LagFix;
        out.BoostLimit = in.BoostLimit;
        out.M1LeanCruise = in.M1LeanCruise;
        out.TractionAssist = in.TractionAssist;
        out.AutoStr = in.AutoStr;
        out.M1MethHard = in.M1MethHard;
        out.M1MethRange = in.M1MethRange;
        out.M1BogFix = in.M1BogFix;
        out.DpsStrength = in.DpsStrength;
        out.FuelPressure = in.FuelPressure;
        out.Firmware = in.Firmware;
        out.N1MinPsi = in.N1MinPsi;
        out.N1Enabled = in.N1Enabled;
        out.N1MethFlow = in.N1MethFlow;
        out.N1MinRpm = in.N1MinRpm;
        out.N1MaxRpm = in.N1MaxRpm;
        out.N1RampRate = in.N1RampRate;
        out.N1ShiftRed = in.N1ShiftRed;
        out.FpsSafety = in.FpsSafety;
        out.CpsOffset = in.CpsOffset;
        out.CpsSafety = in.CpsSafety;
        out.Cps = in.Cps;
        out.LearnedFF = in.LearnedFF;
        out.Accel = in.Accel;
        out.N1MinGear = in.N1MinGear;
        out.N1MaxGear = in.N1MaxGear;
        out.N1MinAfr = in.N1MinAfr;
        out.N1MinAdv = in.N1MinAdv;
        out.Kr1 = in.Kr1;
        out.Kr2 = in.Kr2;
        out.Kr3 = in.Kr3;
        out.Kr4 = in.Kr4;
        out.Kr5 = in.Kr5;
        out.Kr6 = in.Kr6;
        out.N20_Tmap = in.N20_Tmap;
    }

    public static String getCsvHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("Timestamp,");
        sb.append("Rpm,");
        sb.append("Boost,");
        sb.append("Tps,");
        sb.append("IAT,");
        sb.append("Map,");
        sb.append("Clock,");
        sb.append("Pwm,");
        sb.append("Fuel,");
        sb.append("EGT,");
        sb.append("Gear,");
        sb.append("AFR,");
        sb.append("Meth,");
        sb.append("OilTemp,");
        sb.append("FFPid,");
        sb.append("AFR2,");
        sb.append("Ambientv,");
        sb.append("DMEBoost,");
        sb.append("IgnAdv,");
        sb.append("AvgIgn,");
        sb.append("AvgIgnDrop,");
        sb.append("DmeBT,");
        sb.append("DmeTarget,");
        sb.append("M1Fuel,");
        sb.append("M1PidGain,");
        sb.append("M1Throttle,");
        sb.append("M1LagFix,");
        sb.append("BoostLimit,");
        sb.append("M1LeanCruise,");
        sb.append("TractionAssist,");
        sb.append("AutoStr,");
        sb.append("M1MethHard,");
        sb.append("M1MethRange,");
        sb.append("M1BogFix,");
        sb.append("DpsStrength,");
        sb.append("FuelPressure,");
        sb.append("Firmware,");
        sb.append("N1MinPsi,");
        sb.append("N1Enabled,");
        sb.append("N1MethFlow,");
        sb.append("N1MinRpm,");
        sb.append("N1MaxRpm,");
        sb.append("N1RampRate,");
        sb.append("N1ShiftRed,");
        sb.append("FpsSafety,");
        sb.append("CpsOffset,");
        sb.append("CpsSafety,");
        sb.append("Cps,");
        sb.append("LearnedFF,");
        sb.append("Accel,");
        sb.append("N1MinGear,");
        sb.append("N1MaxGear,");
        sb.append("N1MinAfr,");
        sb.append("N1MinAdv,");
        sb.append("Kr1,");
        sb.append("Kr2,");
        sb.append("Kr3,");
        sb.append("Kr4,");
        sb.append("Kr5,");
        sb.append("Kr6,");
        sb.append("N20_Tmap\n");
        return sb.toString();
    }

    public static String getCsvString(DetailLogPoint lp) {
        StringBuilder sb = new StringBuilder();
        sb.append(lp.Timestamp).append(",");
        sb.append(lp.Rpm).append(",");
        sb.append(lp.Boost).append(",");
        sb.append(lp.Tps).append(",");
        sb.append(lp.IAT).append(",");
        sb.append(lp.Map).append(",");
        sb.append(lp.Clock).append(",");
        sb.append(lp.Pwm).append(",");
        sb.append(lp.Fuel).append(",");
        sb.append(lp.Egt).append(",");
        sb.append(lp.Gear).append(",");
        sb.append(lp.Afr).append(",");
        sb.append(lp.Meth).append(",");
        sb.append(lp.OilTemp).append(",");
        sb.append(lp.FFPid).append(",");
        sb.append(lp.Afr2).append(",");
        sb.append(lp.Ambientv).append(",");
        sb.append(lp.DmeBoost).append(",");
        sb.append(lp.IgnAdv).append(",");
        sb.append(lp.AvgIgn).append(",");
        sb.append(lp.AvgIgnDrop).append(",");
        sb.append(lp.Dmebt).append(",");
        sb.append(lp.DmeTarget).append(",");
        sb.append(lp.M1Fuel).append(",");
        sb.append(lp.M1PidGain).append(",");
        sb.append(lp.M1Throttle).append(",");
        sb.append(lp.M1LagFix).append(",");
        sb.append(lp.BoostLimit).append(",");
        sb.append(lp.M1LeanCruise).append(",");
        sb.append(lp.TractionAssist).append(",");
        sb.append(lp.AutoStr).append(",");
        sb.append(lp.M1MethHard).append(",");
        sb.append(lp.M1MethRange).append(",");
        sb.append(lp.M1BogFix).append(",");
        sb.append(lp.DpsStrength).append(",");
        sb.append(lp.FuelPressure).append(",");
        sb.append(lp.Firmware).append(",");
        sb.append(lp.N1MinPsi).append(",");
        sb.append(lp.N1Enabled).append(",");
        sb.append(lp.N1MethFlow).append(",");
        sb.append(lp.N1MinRpm).append(",");
        sb.append(lp.N1MaxRpm).append(",");
        sb.append(lp.N1RampRate).append(",");
        sb.append(lp.N1ShiftRed).append(",");
        sb.append(lp.FpsSafety).append(",");
        sb.append(lp.CpsOffset).append(",");
        sb.append(lp.CpsSafety).append(",");
        sb.append(lp.Cps).append(",");
        sb.append(lp.LearnedFF).append(",");
        sb.append(lp.Accel).append(",");
        sb.append(lp.N1MinGear).append(",");
        sb.append(lp.N1MaxGear).append(",");
        sb.append(lp.N1MinAfr).append(",");
        sb.append(lp.N1MinAdv).append(",");
        sb.append(lp.Kr1).append(",");
        sb.append(lp.Kr2).append(",");
        sb.append(lp.Kr3).append(",");
        sb.append(lp.Kr4).append(",");
        sb.append(lp.Kr5).append(",");
        sb.append(lp.Kr6).append(",");
        sb.append(lp.N20_Tmap).append('\n');
        return sb.toString();
    }
}
