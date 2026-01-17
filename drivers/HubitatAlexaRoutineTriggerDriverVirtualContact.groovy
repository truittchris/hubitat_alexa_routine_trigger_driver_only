/**
 * Hubitat Alexa Routine Trigger - Virtual Contact Trigger
 *
 * Author: Chris Truitt
 * Website: https://christruitt.com
 * GitHub: https://github.com/truittchris
 * Namespace: truittchris
 *
 * Purpose
 * - Creates an Alexa-friendly virtual contact sensor.
 * - Use the contact "opens" event as an Alexa routine trigger.
 * - Auto-resets back to closed so it can be triggered repeatedly.
 *
 * Notes
 * - Some Hubitat environments do not support paragraph() in preferences.
 *   This driver intentionally avoids paragraph() to prevent compilation errors.
 */

import groovy.transform.Field

@Field static final String DRIVER_VERSION = "0.9.0"

metadata {
    definition(
        name: "Hubitat Alexa Routine Trigger - Virtual Contact Trigger",
        namespace: "truittchris",
        author: "Chris Truitt"
    ) {
        capability "ContactSensor"
        capability "Sensor"
        capability "Actuator"
        capability "Refresh"

        command "open"
        command "close"
        command "test"

        attribute "resetAfterSeconds", "number"
        attribute "ignoreRepeatedTriggersMs", "number"

        attribute "lastActionAt", "string"
        attribute "lastCommand", "string"

        attribute "lastTestAt", "string"
        attribute "lastTestResult", "string"
        attribute "lastTestDetails", "string"

        attribute "driverVersion", "string"
        attribute "supportUrl", "string"
    }

    preferences {
        section("Overview") {
            input name: "overviewInfo", type: "enum", title: "What this does", required: false,
                options: ["Creates a virtual contact sensor you can use as an Alexa routine trigger (contact opens)."],
                defaultValue: "Creates a virtual contact sensor you can use as an Alexa routine trigger (contact opens)."
        }

        section("Setup") {
            input name: "setupInfo", type: "enum", title: "Required setup", required: false,
                options: ["Expose this device to Alexa using Hubitat's built-in Amazon Echo Skill app."],
                defaultValue: "Expose this device to Alexa using Hubitat's built-in Amazon Echo Skill app."
        }

        section("Options") {
            input name: "autoResetSeconds", type: "number", title: "Reset back to closed after (seconds)", defaultValue: 5
            input name: "debounceMs", type: "number", title: "Ignore repeated triggers for (ms)", defaultValue: 250
        }

        section("Test") {
            input name: "testInfo", type: "enum", title: "How to test", required: false,
                options: ["On the device page, run the Test command. It will open the contact and allow the normal auto-reset to close it."],
                defaultValue: "On the device page, run the Test command. It will open the contact and allow the normal auto-reset to close it."
        }

        section("Advanced") {
            input name: "advancedInfo", type: "enum", title: "Optional", required: false,
                options: ["These settings are optional and only needed in special cases."],
                defaultValue: "These settings are optional and only needed in special cases."

            input name: "logLevel", type: "enum", title: "Logging", options: ["Off", "Basic", "Debug"], defaultValue: "Off", required: true
            input name: "debugFor30", type: "bool", title: "Enable Debug logging for 30 minutes", defaultValue: false, required: false
        }

        section("Support") {
            input name: "supportInfo", type: "enum", title: "Support and development", required: false,
                options: ["Support: https://www.christruitt.com/support | Tip jar: https://christruitt.com/tip-jar"],
                defaultValue: "Support: https://www.christruitt.com/support | Tip jar: https://christruitt.com/tip-jar"
        }
    }
}

def installed() { initialize() }

def updated() {
    migrateLoggingIfNeeded()
    if (settings?.debugFor30 == true) enableDebugFor30Minutes()
    unschedule()
    initialize()
}

def refresh() {
    clearErrorFields()
    syncAttributesFromLocalConfig()
}

def initialize() {
    migrateLoggingIfNeeded()

    if (device.currentValue("contact") == null) {
        sendEvent(name: "contact", value: "closed", isStateChange: true)
    }

    sendEvent(name: "driverVersion", value: DRIVER_VERSION, isStateChange: true)
    sendEvent(name: "supportUrl", value: "https://christruitt.com", isStateChange: true)

    syncAttributesFromLocalConfig()
}

/* Commands */

def open() { doOpen(false) }

def close() { doClose(false) }

def test() {
    def ts = nowIso()
    sendEvent(name: "lastTestAt", value: ts, isStateChange: true)

    Boolean didTrigger = doOpen(true)
    if (!didTrigger) {
        sendEvent(name: "lastTestResult", value: "Suppressed", isStateChange: true)
        sendEvent(name: "lastTestDetails", value: "Ignored by cooldown. Wait a moment and run Test again.", isStateChange: true)
        return
    }

    sendEvent(name: "lastTestResult", value: "Success", isStateChange: true)
    sendEvent(name: "lastTestDetails", value: "Contact opened. It will close automatically based on your reset setting.", isStateChange: true)
}

/* Internals */

private Boolean doOpen(Boolean fromTest) {
    if (isDebounced()) return false

    sendEvent(name: "contact", value: "open", isStateChange: true)
    diag("open")

    Integer sec = safeInt(settings.autoResetSeconds, 5)
    if (sec < 0) sec = 0
    if (sec > 3600) sec = 3600
    if (sec > 0) runIn(sec, "close", [overwrite: true])

    return true
}

private Boolean doClose(Boolean fromTest) {
    if (isDebounced(forceCloseOk: true) == true) return false
    sendEvent(name: "contact", value: "closed", isStateChange: true)
    diag("close")
    return true
}

private void syncAttributesFromLocalConfig() {
    sendEvent(name: "resetAfterSeconds", value: safeInt(settings.autoResetSeconds, 5), isStateChange: true)
    sendEvent(name: "ignoreRepeatedTriggersMs", value: safeInt(settings.debounceMs, 250), isStateChange: true)
}

private Boolean isDebounced(Map opts = [:]) {
    Boolean forceCloseOk = (opts?.forceCloseOk == true)

    Long last = state.lastActionMs as Long
    Long nowMs = now()

    Integer window = safeInt(settings.debounceMs, 250)
    if (window < 0) window = 0
    if (window > 60000) window = 60000

    if (last && (nowMs - last) < window) {
        if (forceCloseOk && device.currentValue("contact") == "open") {
            // allow scheduled close even inside debounce window
        } else {
            logDebug("Suppressed duplicate (${nowMs - last}ms < ${window}ms)")
            return true
        }
    }

    state.lastActionMs = nowMs
    return false
}

private void diag(String cmd) {
    sendEvent(name: "lastCommand", value: cmd, isStateChange: true)
    sendEvent(name: "lastActionAt", value: nowIso(), isStateChange: true)
    logBasic("${cmd}()")
}

private void clearErrorFields() {
    // reserved for future error fields on this device
}

private void migrateLoggingIfNeeded() {
    if (settings?.logLevel) return

    String inferred = "Off"
    if (settings?.debugLogging == true) inferred = "Debug"
    else if (settings?.infoLogging == true) inferred = "Basic"

    device.updateSetting("logLevel", [type: "enum", value: inferred])
}

private void enableDebugFor30Minutes() {
    device.updateSetting("logLevel", [type: "enum", value: "Debug"])
    device.updateSetting("debugFor30", [type: "bool", value: false])
    runIn(1800, "disableDebug")
    logWarn("Debug logging enabled for 30 minutes.")
}

def disableDebug() {
    device.updateSetting("logLevel", [type: "enum", value: "Off"])
    logWarn("Debug logging disabled.")
}

private void logDebug(String msg) {
    if (settings?.logLevel == "Debug") log.debug "${device.displayName}: ${msg}"
}

private void logBasic(String msg) {
    if (settings?.logLevel in ["Basic", "Debug"]) log.info "${device.displayName}: ${msg}"
}

private void logWarn(String msg) {
    log.warn "${device.displayName}: ${msg}"
}

private Integer safeInt(def v, Integer dflt) {
    try { return (v == null) ? dflt : Integer.parseInt(v.toString()) }
    catch (ignored) { return dflt }
}

private String nowIso() {
    def tz = location?.timeZone ?: TimeZone.getTimeZone("America/New_York")
    return new Date().format("yyyy-MM-dd HH:mm:ss", tz)
}
