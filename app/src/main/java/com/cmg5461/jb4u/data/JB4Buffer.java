package com.cmg5461.jb4u.data;

import com.cmg5461.jb4u.log.DetailLogPoint;

/**
 * Created by Chris on 11/10/2015.
 */
public class JB4Buffer {
    private DetailLogPoint logPoint;
    private int byte_counter = 0;
    private int byte_counter_end = 0;
    private int bufferSize = 2000;
    private int queueLength = 0;
    private byte[] byteIN = new byte[bufferSize];
    private byte[] tempBuffer = new byte[100];

    public JB4Buffer(DetailLogPoint logPoint) {
        this.logPoint = logPoint;
        for (int i = 0; i < bufferSize; i++) byteIN[i] = 46;
        for (int i = 0; i < tempBuffer.length; i++) tempBuffer[i] = 0;
    }

    public void AddBytes(byte[] bytes) {
        for (byte b : bytes) {
            byteIN[byte_counter_end++] = b;
            queueLength++;
            if (byte_counter_end == bufferSize) byte_counter_end = 0;
        }
    }

    public void ParseBuffer() {
        while (byte_counter != byte_counter_end) {
            int idx = byte_counter;
            int tempQueueLength = queueLength;
            double num2 = logPoint.N20_Tmap == 1 ? 7.045 : 10;
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
                break;
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
                logPoint.Gear = Transform.bytes2Gear(bytes);
                break;
            case LEARNED_FF:
                logPoint.LearnedFF = Transform.bytes2LearnedFF(bytes);
                break;
            case OIL_TEMP:
                logPoint.OilTemp = Transform.bytes2OilTemp(bytes);
                break;
            case METH:
                logPoint.Meth = Transform.bytes2Meth(bytes);
                break;
            case EGT:
                logPoint.Egt = Transform.bytes2Egt(bytes);
                break;
            case AVG_IGN:
                logPoint.AvgIgn = Transform.bytes2AvgIgn(bytes);
                break;
            case IGN_ADV:
                logPoint.IgnAdv = Transform.bytes2IgnAdv(bytes);
                break;
            case DME_BOOST_TARG:
                logPoint.Dmebt = Transform.bytes2Dmebt(bytes);
                break;
            case FPS_SAFETY:
                logPoint.FpsSafety = Transform.bytes2FpsSafety(bytes);
                break;
            case FFPID:
                logPoint.FFPid = Transform.bytes2FFPid(bytes);
                break;
            case AFR2:
                logPoint.Afr2 = Transform.bytes2Afr2(bytes);
                break;
            case CPS:
                logPoint.Cps = Transform.bytes2Cps(bytes);
                break;
            case METH_RANGE:
                logPoint.M1MethRange = Transform.bytes2M1MethRange(bytes);
                break;
            case RPM:
                logPoint.Rpm = Transform.bytes2Rpm(bytes);
                //Constants.LogD("RPM IN: " +  logPoint.Rpm + " " + Arrays.toString(bytes));
                break;
            case BOOST:
                logPoint.Boost = Transform.bytes2Boost(bytes, scale);
                break;
            case TPS:
                logPoint.Tps = Transform.bytes2Tps(bytes);
                break;
            case PWM:
                logPoint.Pwm = Transform.bytes2Pwm(bytes);
                break;
            case FUEL:
                logPoint.Fuel = Transform.bytes2Fuel(bytes);
                break;
            case MAP:
                logPoint.Map = Transform.bytes2Map(bytes);
                break;
            case IAT:
                logPoint.IAT = Transform.bytes2IAT(bytes);
                break;
            case CLOCK:
                logPoint.Clock = Transform.bytes2Clock(bytes, logPoint.Clock);
                break;
            case KR2:
                logPoint.Kr2 = Transform.bytes2Kr2(bytes);
                break;
            case KR3:
                logPoint.Kr3 = Transform.bytes2Kr3(bytes);
                break;
            case KR5:
                logPoint.Kr5 = Transform.bytes2Kr5(bytes);
                break;
            case AMBIENT_V:
                logPoint.Ambientv = Transform.bytes2Ambientv(bytes);
                break;
            case DME_BOOST:
                logPoint.DmeBoost = Transform.bytes2DmeBoost(bytes);
                break;
            case DME_TARGET:
                logPoint.DmeTarget = Transform.bytes2DmeTarget(bytes); // actual target
                break;
            case KR4:
                logPoint.Kr4 = Transform.bytes2Kr4(bytes);
                break;
            case N1_MIN_PSI:
                logPoint.N1MinPsi = Transform.bytes2N1MinPsi(bytes, scale);
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
            case ACCEL:
                logPoint.Accel = Transform.bytes2Accel(bytes);
                break;
            case FUEL_PRESSURE:
                logPoint.FuelPressure = Transform.bytes2FuelPressure(bytes);
                break;
            case AFR:
                logPoint.Afr = Transform.bytes2Afr(bytes);
                break;
            case CPS_OFFSET:
                logPoint.CpsOffset = Transform.bytes2CpsOffset(bytes);
                break;
            case CPS_VALUES:
                // TODO
                break;
            case M1_BOG_FIX:
                logPoint.M1BogFix = Transform.bytes2M1BogFix(bytes);
                break;
            case M1_FUEL:
                logPoint.M1Fuel = Transform.bytes2M1Fuel(bytes);
                break;
            case DPS_STRENGTH:
                logPoint.DpsStrength = Transform.bytes2DpsStrength(bytes);
                break;
            case M1_PID_GAIN:
                logPoint.M1PidGain = Transform.bytes2M1PidGain(bytes);
                break;
            case FIRMWARE:
                logPoint.Firmware = Transform.bytes2Firmware(bytes);
                break;
            case M1_THROTTLE:
                logPoint.M1Throttle = Transform.bytes2M1Throttle(bytes);
                break;
            case M1_LAGFIX:
                logPoint.M1LagFix = Transform.bytes2M1LagFix(bytes);
                break;
            case M1_BOOST_LIMIT:
                logPoint.BoostLimit = Transform.bytes2BoostLimit(bytes, scale);
                break;
            case M1_LEAN_CRUISE:
                logPoint.M1LeanCruise = Transform.bytes2M1LeanCruise(bytes);
                break;
            case TRACTION_ASSIST:
                logPoint.TractionAssist = Transform.bytes2TractionAssist(bytes, scale);
                break;
            case M1_METH_HARD:
                logPoint.M1MethHard = Transform.bytes2M1MethHard(bytes);
                break;
            case AUTOSTR:
                logPoint.AutoStr = Transform.bytes2AutoStr(bytes, scale);
                break;
            case KR6:
                logPoint.Kr6 = Transform.bytes2Kr6(bytes);
                break;
            case AVG_IGN_DROP:
                logPoint.AvgIgnDrop = Transform.bytes2AvgIgnDrop(bytes);
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
            case KR1:
                logPoint.Kr1 = Transform.bytes2Kr1(bytes);
                break;
        }
    }

    public void updateTimestamp() {
        logPoint.Timestamp = System.currentTimeMillis();
    }
}