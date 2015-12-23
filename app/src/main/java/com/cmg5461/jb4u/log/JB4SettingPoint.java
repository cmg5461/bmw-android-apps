package com.cmg5461.jb4u.log;

/**
 * Created by Chris on 11/16/2015.
 */
public class JB4SettingPoint {

    public String firmware;
    public String jb4interface;
    public String motor;
    public double tmap_v;
    public double avgign;
    public double dwp;
    public double methtriggermode;
    public double FF;
    public double methsafemode;
    public double methadd;
    public double methscale;
    public double methpsi;

    public double boost_safety;
    public double pid_gain;
    public double auto_shift_red;
    public double fuel_ol;
    public double fua;
    public double limit_1st;
    public double limit_2nd;
    public double limit_3rd;
    public double fud;
    public int N20_Tmap;
    public int mode6cyl;
    public double last_safety;
    public double rpm1500;
    public double rpm2000;
    public double rpm2500;
    public double rpm3000;
    public double rpm3500;
    public double rpm4000;
    public double rpm4500;
    public double rpm5000;
    public double rpm5500;
    public double rpm6000;
    public double rpm6500;
    public double rpm7000;
    public int fuel2500;
    public int fuel3000;
    public int fuel3500;
    public int fuel4000;
    public int fuel4500;
    public int fuel5000;
    public int fuel5500;
    public int fuel6000;
    public int fuel6500;
    public int cps1500;
    public int cps2000;
    public int cps2500;
    public int cps3000;
    public int cps3500;
    public int cps4000;
    public int cps4500;
    public int cps5000;
    public int cps5500;
    public int cps6000;
    public int cps6500;
    public int cps7000;

    public JB4SettingPoint() {
        firmware = "";
        jb4interface = "";
        motor = "";
        tmap_v = -1;
        avgign = -1;
        dwp = -1;
        methtriggermode = -1;
        FF = -1;
        methsafemode = -1;
        methadd = -1;
        methscale = -1;
        methpsi = -1;
        boost_safety = -1;
        pid_gain = -1;
        auto_shift_red = -1;
        fuel_ol = -1;
        fua = -1;
        limit_1st = -1;
        limit_2nd = -1;
        limit_3rd = -1;
        fud = -1;
        N20_Tmap = -1;
        mode6cyl = -1;
        last_safety = -1;
        rpm1500 = -1;
        rpm2000 = -1;
        rpm2500 = -1;
        rpm3000 = -1;
        rpm3500 = -1;
        rpm4000 = -1;
        rpm4500 = -1;
        rpm5000 = -1;
        rpm5500 = -1;
        rpm6000 = -1;
        rpm6500 = -1;
        rpm7000 = -1;
        fuel2500 = -1;
        fuel3000 = -1;
        fuel3500 = -1;
        fuel4000 = -1;
        fuel4500 = -1;
        fuel5000 = -1;
        fuel5500 = -1;
        fuel6000 = -1;
        fuel6500 = -1;
        cps1500 = -1;
        cps2000 = -1;
        cps2500 = -1;
        cps3000 = -1;
        cps3500 = -1;
        cps4000 = -1;
        cps4500 = -1;
        cps5000 = -1;
        cps5500 = -1;
        cps6000 = -1;
        cps6500 = -1;
        cps7000 = -1;
    }

    public static String getJB4Header(JB4SettingPoint lp) {
        StringBuilder sb = new StringBuilder();
        sb.append("Firmware,");
        sb.append("Interface,");
        sb.append("Engine,");
        sb.append("TMAP_V,");
        sb.append("AvgIgn,");
        sb.append("DWP,");
        sb.append("MethTriggerMode,");
        sb.append("FF,");
        sb.append("MethSafeMode,");
        sb.append("MethAdd,");
        sb.append("MethScale,");
        sb.append("MethPSI,\r\n");
        sb.append(lp.firmware).append(",");
        sb.append(lp.jb4interface).append(",");
        sb.append(lp.motor).append(",");
        sb.append(lp.tmap_v).append(",");
        sb.append(lp.avgign).append(",");
        sb.append(lp.dwp).append(",");
        sb.append(lp.methtriggermode).append(",");
        sb.append(lp.FF).append(",");
        sb.append(lp.methsafemode).append(",");
        sb.append(lp.methadd).append(",");
        sb.append(lp.methscale).append(",");
        sb.append(lp.methpsi).append(",\r\n"); // first line
        sb.append("BoostSafety,");
        sb.append("PID Gain,");
        sb.append("AutoShiftRed,");
        sb.append("Fuel_OL,");
        sb.append("FUA,");
        sb.append("1st_limiter,");
        sb.append("2nd_limiter,");
        sb.append("3rd_limiter,");
        sb.append("FUD,");
        sb.append("N20_Tmap,");
        sb.append("6CylMode,");
        sb.append("LastSafety,\r\n");
        sb.append(lp.boost_safety).append(",");
        sb.append(lp.pid_gain).append(",");
        sb.append(lp.auto_shift_red).append(",");
        sb.append(lp.fuel_ol).append(",");
        sb.append(lp.fua).append(",");
        sb.append(lp.limit_1st).append(",");
        sb.append(lp.limit_2nd).append(",");
        sb.append(lp.limit_3rd).append(","); // fuel2000
        sb.append(lp.fud).append(",");
        sb.append(lp.N20_Tmap).append(',');
        sb.append(lp.mode6cyl).append(",");
        sb.append(lp.last_safety).append(",\r\n");
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
        sb.append("ign_6,\r\n"); // jb4 log
        return sb.toString();
    }

    public static void Copy(JB4SettingPoint in, JB4SettingPoint out) {
        out.firmware = in.firmware;
        out.jb4interface = in.jb4interface;
        out.motor = in.motor;
        out.tmap_v = in.tmap_v;
        out.avgign = in.avgign;
        out.dwp = in.dwp;
        out.methtriggermode = in.methtriggermode;
        out.FF = in.FF;
        out.methsafemode = in.methsafemode;
        out.methadd = in.methadd;
        out.methscale = in.methscale;
        out.methpsi = in.methpsi;
        out.boost_safety = in.boost_safety;
        out.pid_gain = in.pid_gain;
        out.auto_shift_red = in.auto_shift_red;
        out.fuel_ol = in.fuel_ol;
        out.fua = in.fua;
        out.limit_1st = in.limit_1st;
        out.limit_2nd = in.limit_2nd;
        out.limit_3rd = in.limit_3rd;
        out.fud = in.fud;
        out.N20_Tmap = in.N20_Tmap;
        out.mode6cyl = in.mode6cyl;
        out.last_safety = in.last_safety;
        out.rpm1500 = in.rpm1500;
        out.rpm2000 = in.rpm2000;
        out.rpm2500 = in.rpm2500;
        out.rpm3000 = in.rpm3000;
        out.rpm3500 = in.rpm3500;
        out.rpm4000 = in.rpm4000;
        out.rpm4500 = in.rpm4500;
        out.rpm5000 = in.rpm5000;
        out.rpm5500 = in.rpm5500;
        out.rpm6000 = in.rpm6000;
        out.rpm6500 = in.rpm6500;
        out.rpm7000 = in.rpm7000;
        out.fuel2500 = in.fuel2500;
        out.fuel3000 = in.fuel3000;
        out.fuel3500 = in.fuel3500;
        out.fuel4000 = in.fuel4000;
        out.fuel4500 = in.fuel4500;
        out.fuel5000 = in.fuel5000;
        out.fuel5500 = in.fuel5500;
        out.fuel6000 = in.fuel6000;
        out.fuel6500 = in.fuel6500;
        out.cps1500 = in.cps1500;
        out.cps2000 = in.cps2000;
        out.cps2500 = in.cps2500;
        out.cps3000 = in.cps3000;
        out.cps3500 = in.cps3500;
        out.cps4000 = in.cps4000;
        out.cps4500 = in.cps4500;
        out.cps5000 = in.cps5000;
        out.cps5500 = in.cps5500;
        out.cps6000 = in.cps6000;
        out.cps6500 = in.cps6500;
        out.cps7000 = in.cps7000;
    }
}
