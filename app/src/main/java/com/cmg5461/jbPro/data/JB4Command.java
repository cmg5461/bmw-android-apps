package com.cmg5461.jbPro.data;

/**
 * Created by Chris on 11/6/2015.
 */
public enum JB4Command {
    HEARTBEAT_TICK("E"),
    INITIALIZE_CONNECTION1("B"),
    INITIALIZE_CONNECTION2("$#C"),
    INITIALIZE_CONNECTION3("A"),
    START_LOGGING("CA"),
    STOP_LOGGING1("B"),
    STOP_LOGGING2("B!#["),
    SWITCH_TO_MAP("M"), // + map#
    READ_CODES("G"),
    DELETE_CODES("H"),
    DELETE_FIRMWARE1("ZY"),
    DELETE_FIRMWARE2("U"),
    UPLOAD_FIRMWARE("V"),
    GET_FIRMWARE_CHECKSUM("W"),
    REBOOT_JB4("YX"),
    GET_STATUS(""),
    SEND_DASH_COMMAND("F"), // + speedo# + tach#
    ;

    private final String value;

    JB4Command(String val) {
        value = val;
    }

    public String getValue() {
        return value;
    }
}
