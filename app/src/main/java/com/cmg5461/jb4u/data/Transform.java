package com.cmg5461.jb4u.data;

import java.util.Arrays;

/**
 * Created by Chris on 11/10/2015.
 */
public class Transform {

    public static int bytes2Rpm(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static double bytes2Boost(byte[] bytes, double scale) {
        return convertByteArrayToDouble(bytes) / scale;
    }

    public static int bytes2IAT(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2Pedal(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2Map(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2Clock(byte[] bytes, int lastClock) {
        return convertByteArrayToInt(bytes) - lastClock;
    }

    public static int bytes2Pwm(byte[] bytes) {
        return (int) (convertByteArrayToInt(bytes) * 0.3921568);
    }

    public static int bytes2Fuel(byte[] bytes) {
        double n = (convertByteArrayToDouble(bytes) - 147.0D) * -0.7;
        if (n < 0) n += 100;
        return (int) n;
    }

    public static int bytes2Egt(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2Gear(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static double bytes2Afr(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static int bytes2Meth(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2FPL(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2OilTemp(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2WaterTemp(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static int bytes2FFPid(byte[] bytes) {
        return (int) (convertByteArrayToDouble(bytes) / 2.55);
    }

    public static double bytes2Afr2(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2Ambientv(byte[] bytes) {
        double amb = convertByteArrayToDouble(bytes);
        if (amb < 10) return amb;
        return amb / 204.6;
    }

    public static double bytes2DmeBoost(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2IgnAdv(byte[] bytes) {
        double iga = convertByteArrayToDouble(bytes);
        return iga > 500 ? 50D : iga / 10;
    }

    public static double bytes2AvgIgn(byte[] bytes) {
        double iga = convertByteArrayToDouble(bytes);
        return iga > 500 ? 50D : iga / 10;
    }

    public static double bytes2AvgIgnDrop(byte[] bytes) {
        double iga = convertByteArrayToDouble(bytes);
        return iga > 500 ? 50D : iga / 10;
    }

    public static double bytes2Dmebt(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2DmeTarget(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2M1Fuel(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2M1PidGain(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2M1Throttle(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2M1LagFix(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2BoostLimit(byte[] bytes, double num2) {
        return convertByteArrayToDouble(bytes) / num2;
    }

    public static int bytes2M1LeanCruise(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static double bytes2TractionAssist(byte[] bytes, double num2) {
        return convertByteArrayToDouble(bytes) / num2;
    }

    public static double bytes2AutoStr(byte[] bytes, double num2) {
        return convertByteArrayToDouble(bytes) / num2;
    }

    public static double bytes2M1MethHard(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2M1MethRange(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2M1BogFix(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2DpsStrength(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2FuelPressure(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static String bytes2Firmware(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0) {
                return new String(Arrays.copyOfRange(bytes, 0, i));
            }
        }
        return "";
    }

    public static double bytes2N1MinPsi(byte[] bytes, double num2) {
        return convertByteArrayToDouble(bytes) / num2;
    }

    public static double bytes2N1Enabled(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2N1MethFlow(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2N1MinRpm(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2N1MaxRpm(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2N1RampRate(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2N1ShiftRed(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static int bytes2FpsSafety(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static double bytes2CpsOffset(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2Cps(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2LearnedFF(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static int bytes2Throttle(byte[] bytes) {
        return convertByteArrayToInt(bytes);
    }

    public static double bytes2N1MinGear(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2N1MaxGear(byte[] bytes) {
        return convertByteArrayToDouble(bytes);
    }

    public static double bytes2N1MinAfr(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2N1MinAdv(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2Kr1(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2Kr2(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2Kr3(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2Kr4(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2Kr5(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double bytes2Kr6(byte[] bytes) {
        return convertByteArrayToDouble(bytes) / 10;
    }

    public static double byte2cps_rpm(byte b, double scale) {
        return Double.parseDouble(String.valueOf(b)) / scale;
    }

    public static int byte2cps_fuel(byte b) {
        return Integer.parseInt(String.valueOf(b));
    }

    public static double convertByteArrayToDouble(byte[] bytes) {
        boolean decimalFound = false;
        double val = 0.0D;
        double dec = 0.0D;
        int decplace = 1;
        for (byte b : bytes) {
            if (b == 0) break;
            if (b == 46) {
                decimalFound = true;
                continue;
            }
            if (!decimalFound) {
                val *= 10;
                val += b - 48; // ascii to int
            } else
                dec += ((b - 48) / Math.pow(10, decplace++));
        }
        return val + dec;
    }

    public static int convertByteArrayToInt(byte[] bytes) {
        int val = 0;
        for (byte b : bytes) {
            if (b == 0) break;
            if (b == 46) return -1;
            val *= 10;
            val += b - 48; // ascii to int
        }
        return val;
    }
}
