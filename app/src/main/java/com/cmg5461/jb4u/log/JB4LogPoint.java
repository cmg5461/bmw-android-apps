package com.cmg5461.jb4u.log;

/**
 * Created by Chris on 11/10/2015.
 */
public class JB4LogPoint {

    public int Rpm;
    public double Boost;
    public double Tps;
    public int Map;
    public int Clock;
    public int Pwm;
    public int Fuel;

    public double Afr;
    public double Meth;
    public double OilTemp;
    public int FFPid;
    public double Afr2;
    public double Ambientv;
    public double DmeBoost;
    public double IgnAdv;
    public double AvgIgn;
    public double AvgIgnDrop;
    public double Dmebt;
    public double DmeTarget;
    public double M1Fuel;
    public double M1PidGain;
    public double M1MethRange;
    public double M1LagFix;
    public double TractionAssist;
    public double M1MethHard;
    public double FpsSafety;
    public double N1Enabled;
    public double M1LenCruise;

    public double LearnedFF;
    public double N1MinGear;
    public double N1MaxRpm;
    public double FuelPressure;
    public double N1ShiftRed;
    public double M1Throttle;
    public double Cps;
    public double BoostLimit;
    public double AutoStr;
    public double CpsSafety;
    public double M1BogFix;
    public String Firmware;
    public double N1MinPsi;
    public double DpsStrength;
    public double N1MinRpm;
    public double N1MethFlow;
    public double N1RampRate;
    public double Accel;
    public double N1MaxGear;
    public double N1MinAfr;
    public double N1MinAdv;

    public double Egt;
    public int Gear;

    public double Kr1;
    public double Kr2;
    public double Kr3;
    public double Kr4;
    public double Kr5;
    public double Kr6;

    public JB4LogPoint() {
        Rpm = -1;
        Boost = 0.0D;
        Tps = 0.0D;
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
        M1LenCruise = 0.0D;
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
    }
}
