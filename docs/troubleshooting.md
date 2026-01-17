# Troubleshooting

This guide covers the most common setup and runtime issues for Hubitat Alexa Routine Trigger.

## Quick checklist
- In Hubitat, confirm the device(s) are created and the correct driver is selected.
- In the device preferences, click Done after any changes.
- In Hubitat's Amazon Echo Skill app, confirm the trigger devices are selected for exposure.
- In the Alexa app, run Discover Devices after exposing new devices.
- In Alexa routines, confirm you used: When this happens - Contact sensor - Opens.

## Symptoms and fixes

### Alexa does not see the device
Likely causes
- The device is not selected in Hubitat's Amazon Echo Skill app.
- Alexa discovery has not been run since exposing the device.

What to try
1. Hubitat - Apps - Amazon Echo Skill - confirm the device is selected.
2. Alexa app - run Discover Devices.
3. Wait a minute and check Devices - Sensors in Alexa.

### The Hubitat Test command runs, but the Alexa routine does not fire
Likely causes
- The routine is not configured on the correct device.
- The routine is using "closes" instead of "opens".
- Alexa did not complete discovery or is using a stale device record.

What to try
1. In Alexa, open the routine and confirm the trigger is: Contact opens.
2. Delete and recreate the routine trigger if it looks suspicious.
3. In Alexa, remove the device and discover again (or disable and re-enable the Hubitat skill).
4. In Hubitat, increase Reset back to closed after (seconds) to 2-5 seconds.

### Test result shows Suppressed
Meaning
- The driver ignored the command because it was triggered again inside the configured cooldown window.

What to try
1. Wait for the cooldown window to pass and run Test again.
2. Reduce Ignore repeated triggers for (ms) if you legitimately need faster repeats.

### MultiTrigger Controller is missing child triggers
Likely causes
- The controller has not been saved (Done) since changing the trigger count.
- Child device creation was blocked by a prior error.

What to try
1. Open the controller device page and click Done in preferences.
2. Run the SyncChildTriggers command.
3. Check LastError on the controller device.

### MultiTrigger Controller runs, but not all routines fire
Likely causes
- Delay between triggers is too short for Alexa to process reliably.
- Child trigger devices are missing from the Amazon Echo Skill exposure list.

What to try
1. Increase Delay between child triggers (ms) to 350-750ms.
2. Confirm every child trigger is selected in Hubitat's Amazon Echo Skill app.
3. In Alexa, confirm each routine uses a distinct child trigger device.

## Collecting diagnostics
When requesting help, enable Debug logging and include:
- Hubitat platform version
- Driver version (shown on the device)
- A screenshot of the device preferences
- The relevant log lines from the Hubitat Logs page

Support portal: https://www.christruitt.com/support
