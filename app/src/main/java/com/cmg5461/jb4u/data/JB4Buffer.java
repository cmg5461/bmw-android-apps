package com.cmg5461.jb4u.data;

import com.cmg5461.jb4u.log.DetailLogPoint;
import com.cmg5461.jb4u.log.JB4SettingPoint;

/**
 * Created by Chris on 11/10/2015.
 */
public class JB4Buffer {
    private final JB4SettingPoint settingPoint;
    private DetailLogPoint logPoint;
    private int byte_counter = 0;
    private int byte_counter_end = 0;
    private int bufferSize = 2000;
    private int queueLength = 0;
    private byte[] byteIN = new byte[bufferSize];
    private byte[] tempBuffer = new byte[100];

    public JB4Buffer(DetailLogPoint logPoint, JB4SettingPoint settingPoint) {
        this.logPoint = logPoint;
        this.settingPoint = settingPoint;
        for (int i = 0; i < bufferSize; i++) byteIN[i] = 46;
        for (int i = 0; i < tempBuffer.length; i++) tempBuffer[i] = 0;
    }

    public void AddBytes(byte[] bytes) {
        synchronized (this) {
            for (byte b : bytes) {
                byteIN[byte_counter_end++] = b;
                queueLength++;
                if (byte_counter_end == bufferSize) byte_counter_end = 0;
            }
        }
    }

    public void ParseBuffer() {
        synchronized (this) {
            while (byte_counter != byte_counter_end) {
                int idx = byte_counter;
                int tempQueueLength = queueLength;
                double num2 = settingPoint.N20_Tmap == 1 ? 7.045 : 10;
                byte cmd = byteIN[idx++];
                if (idx == bufferSize) idx = 0;
                tempQueueLength--;
                if (cmd == JB4Data.CPS_VALUES.value()) {
                    if (tempQueueLength < 36) return;
                    for (int idx2 = 0; idx2 < 36; idx2++) {
                        tempBuffer[idx2] = byteIN[idx2 + idx++];
                        tempQueueLength--;
                        if (idx == bufferSize) idx = 0;
                    }
                    tempBuffer[36] = 0;
                    ParseBufferString(cmd, tempBuffer, num2);
                    queueLength = tempQueueLength;
                    byte_counter = idx;
                } else {
                    int idx2 = 0;
                    while (idx != byte_counter_end) {
                        byte b = byteIN[idx];
                        if (b >= 46 && b <= 57) {
                            tempBuffer[idx2++] = b;
                            tempQueueLength--;
                            if (++idx == bufferSize) idx = 0;
                        } else {
                            tempBuffer[idx2] = 0;
                            ParseBufferString(cmd, tempBuffer, num2);
                            queueLength = tempQueueLength;
                            byte_counter = idx;
                            break;
                        }
                    }
                    if (idx == byte_counter_end) break;
                }
            }
        }
    }

    public void ParseBufferString(byte cmd, byte[] bytes, double scale) {
        JB4Data data = JB4Data.getJB4DataForCmd(cmd);
        if (data == null) {
            Constants.LogD("JB4Data was null: cmd -> " + cmd);
            return;
        }
        switch (data) {
            case GEAR:
                logPoint.gear = Transform.bytes2Gear(bytes);
                break;
            case LAST_SAFETY:
                logPoint.last_safety = Transform.bytes2LearnedFF(bytes);
                break;
            case FP_LOW:
                logPoint.fp_l = Transform.bytes2OilTemp(bytes);
                break;
            case METH:
                logPoint.meth = Transform.bytes2Meth(bytes);
                break;
            case TRIMS:
                logPoint.trims = Transform.bytes2Egt(bytes);
                break;
            case AVG_IGN:
                logPoint.avg_ign = Transform.bytes2AvgIgn(bytes);
                break;
            case IGN_1:
                logPoint.ign_1 = Transform.bytes2IgnAdv(bytes);
                break;
            case DME_BOOST_TARG:
                logPoint.dme_bt = Transform.bytes2Dmebt(bytes);
                break;
            case FUEL_OPEN_LOOP:
                settingPoint.fuel_ol = Transform.bytes2FpsSafety(bytes);
                break;
            case FFPID:
                logPoint.ff = Transform.bytes2FFPid(bytes);
                break;
            case AFR:
                logPoint.afr = Transform.bytes2Afr2(bytes);
                break;
            case LOAD:
                logPoint.load = Transform.bytes2Cps(bytes);
                break;
            case METH_SCALE:
                settingPoint.methscale = Transform.bytes2M1MethRange(bytes);
                break;
            case RPM:
                logPoint.rpm = Transform.bytes2Rpm(bytes);
                break;
            case BOOST:
                logPoint.boost = Transform.bytes2Boost(bytes, scale);
                break;
            case PEDAL:
                logPoint.pedal = Transform.bytes2Tps(bytes);
                break;
            case PWM:
                logPoint.pwm = Transform.bytes2Pwm(bytes);
                break;
            case FUELEN:
                logPoint.fuelen = Transform.bytes2Fuel(bytes);
                break;
            case MAP:
                logPoint.map = Transform.bytes2Map(bytes);
                break;
            case IAT:
                logPoint.iat = Transform.bytes2IAT(bytes);
                break;
            case CLOCK:
                logPoint.clock = Transform.bytes2Clock(bytes, logPoint.clock);
                break;
            case IGN_2:
                logPoint.ign_2 = Transform.bytes2Kr2(bytes);
                break;
            case IGN_3:
                logPoint.ign_3 = Transform.bytes2Kr3(bytes);
                break;
            case IGN_5:
                logPoint.ign_5 = Transform.bytes2Kr5(bytes);
                break;
            case TMAP_V:
                settingPoint.tmap_v = Transform.bytes2Ambientv(bytes);
                break;
            case ECU_PSI:
                logPoint.ecu_psi = Transform.bytes2DmeBoost(bytes);
                break;
            case TARGET:
                logPoint.target = Transform.bytes2DmeTarget(bytes); // actual target
                break;
            case IGN_4:
                logPoint.ign_4 = Transform.bytes2Kr4(bytes);
                break;
            case METHPSI:
                settingPoint.methpsi = Transform.bytes2N1MinPsi(bytes, scale);
                break;
            case N1_ENABLED:
                logPoint.N1Enabled = Transform.bytes2N1Enabled(bytes);
                break;
            case N1_METH_FLOW:
                logPoint.N1MethFlow = Transform.bytes2N1MethFlow(bytes);
                break;
            case N1_MIN_RPM:
                logPoint.N1MinRpm = Transform.bytes2N1MinRpm(bytes);
                break;
            case N1_MAX_RPM:
                logPoint.N1MaxRpm = Transform.bytes2N1MaxRpm(bytes);
                break;
            case N1_RAMP_RATE:
                logPoint.N1RampRate = Transform.bytes2N1RampRate(bytes);
                break;
            case N1_SHIFT_RED:
                logPoint.N1ShiftRed = Transform.bytes2N1ShiftRed(bytes);
                break;
            case THROTTLE:
                logPoint.throttle = Transform.bytes2Accel(bytes);
                break;
            case FP_HIGH:
                logPoint.fp_h = Transform.bytes2FuelPressure(bytes);
                break;
            case DUTYCYCLE:
                logPoint.dutycycle = Transform.bytes2Afr(bytes);
                break;
            case METHSAFEMODE:
                settingPoint.methsafemode = Transform.bytes2CpsOffset(bytes);
                break;
            case CPS_VALUES:
                settingPoint.N20_Tmap = Transform.byte2cps_fuel(bytes[12]);
                scale = settingPoint.N20_Tmap == 1 ? 7.045 : 10;
                settingPoint.rpm1500 = Transform.byte2cps_rpm(bytes[0], scale);
                settingPoint.rpm2000 = Transform.byte2cps_rpm(bytes[1], scale);
                settingPoint.rpm2500 = Transform.byte2cps_rpm(bytes[2], scale);
                settingPoint.rpm3000 = Transform.byte2cps_rpm(bytes[3], scale);
                settingPoint.rpm3500 = Transform.byte2cps_rpm(bytes[4], scale);
                settingPoint.rpm4000 = Transform.byte2cps_rpm(bytes[5], scale);
                settingPoint.rpm4500 = Transform.byte2cps_rpm(bytes[6], scale);
                settingPoint.rpm5000 = Transform.byte2cps_rpm(bytes[7], scale);
                settingPoint.rpm5500 = Transform.byte2cps_rpm(bytes[8], scale);
                settingPoint.rpm6000 = Transform.byte2cps_rpm(bytes[9], scale);
                settingPoint.rpm6500 = Transform.byte2cps_rpm(bytes[10], scale);
                settingPoint.rpm7000 = Transform.byte2cps_rpm(bytes[11], scale);
                settingPoint.limit_3rd = Transform.byte2cps_rpm(bytes[13], scale);
                settingPoint.fuel2500 = Transform.byte2cps_fuel(bytes[14]);
                settingPoint.fuel3000 = Transform.byte2cps_fuel(bytes[15]);
                settingPoint.fuel3500 = Transform.byte2cps_fuel(bytes[16]);
                settingPoint.fuel4000 = Transform.byte2cps_fuel(bytes[17]);
                settingPoint.fuel4500 = Transform.byte2cps_fuel(bytes[18]);
                settingPoint.fuel5000 = Transform.byte2cps_fuel(bytes[19]);
                settingPoint.fuel5500 = Transform.byte2cps_fuel(bytes[20]);
                settingPoint.fuel6000 = Transform.byte2cps_fuel(bytes[21]);
                settingPoint.fuel6500 = Transform.byte2cps_fuel(bytes[22]);
                settingPoint.mode6cyl = Transform.byte2cps_fuel(bytes[23]);
                settingPoint.cps1500 = Transform.byte2cps_fuel(bytes[24]);
                settingPoint.cps2000 = Transform.byte2cps_fuel(bytes[25]);
                settingPoint.cps2500 = Transform.byte2cps_fuel(bytes[26]);
                settingPoint.cps3000 = Transform.byte2cps_fuel(bytes[27]);
                settingPoint.cps3500 = Transform.byte2cps_fuel(bytes[28]);
                settingPoint.cps4000 = Transform.byte2cps_fuel(bytes[29]);
                settingPoint.cps4500 = Transform.byte2cps_fuel(bytes[20]);
                settingPoint.cps5000 = Transform.byte2cps_fuel(bytes[31]);
                settingPoint.cps5500 = Transform.byte2cps_fuel(bytes[32]);
                settingPoint.cps6000 = Transform.byte2cps_fuel(bytes[33]);
                settingPoint.cps6500 = Transform.byte2cps_fuel(bytes[34]);
                settingPoint.cps7000 = Transform.byte2cps_fuel(bytes[35]);
                break;
            case METHTRIGGERMODE:
                settingPoint.methtriggermode = Transform.bytes2M1BogFix(bytes);
                break;
            case FUA:
                settingPoint.fua = Transform.bytes2M1Fuel(bytes);
                break;
            case FUD:
                settingPoint.fud = Transform.bytes2DpsStrength(bytes);
                break;
            case PID_GAIN:
                settingPoint.pid_gain = Transform.bytes2M1PidGain(bytes);
                break;
            case FIRMWARE:
                settingPoint.firmware = Transform.bytes2Firmware(bytes);
                break;
            case AUTO_SHIFT_RED:
                settingPoint.auto_shift_red = Transform.bytes2M1Throttle(bytes);
                break;
            case DWP:
                settingPoint.dwp = Transform.bytes2M1LagFix(bytes);
                break;
            case BOOST_SAFETY:
                settingPoint.boost_safety = Transform.bytes2BoostLimit(bytes, scale);
                break;
            case FF:
                settingPoint.FF = Transform.bytes2M1LeanCruise(bytes);
                break;
            case LIMIT_1ST:
                settingPoint.limit_1st = Transform.bytes2TractionAssist(bytes, scale);
                break;
            case METHADD:
                settingPoint.methadd = Transform.bytes2M1MethHard(bytes);
                break;
            case LIMIT_2ND:
                settingPoint.limit_2nd = Transform.bytes2AutoStr(bytes, scale);
                break;
            case IGN_6:
                logPoint.ign_6 = Transform.bytes2Kr6(bytes);
                break;
            case AVGIGN:
                settingPoint.avgign = Transform.bytes2AvgIgn(bytes);
                break;
            case N1_MIN_GEAR:
                logPoint.N1MinGear = Transform.bytes2N1MinGear(bytes);
                break;
            case N1_MIN_ADV:
                logPoint.N1MinAdv = Transform.bytes2N1MinAdv(bytes);
                break;
            case N1_MIN_AFR:
                logPoint.N1MinAfr = Transform.bytes2N1MinAfr(bytes);
                break;
            case AFR2:
                logPoint.afr2 = Transform.bytes2Kr1(bytes);
                break;
            case END_OF_LINE:
                break;
        }
    }

    public void updateTimestamp() {
        logPoint.timestamp = System.currentTimeMillis();
    }

    public DetailLogPoint getLogPoint() {
        return logPoint;
    }
}