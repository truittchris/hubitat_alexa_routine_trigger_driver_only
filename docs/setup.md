# Setup

This is the end-to-end setup guide for Hubitat Alexa Routine Trigger.

Target user: a Hubitat hobbyist who is comfortable installing apps and drivers, but is not a programmer.

## Before you start
- Confirm your Hubitat platform version is 2.1.9 or newer (recommended).
- Confirm you have the Alexa app installed and signed in.
- Confirm Hubitat's built-in Amazon Echo Skill app is installed.

## Install (choose one)

### Option A - Hubitat Package Manager (recommended)
1. In Hubitat Package Manager (HPM), add a user repo using the raw URL to hpm/repository.json in this GitHub repo.
2. Install the package.
3. Proceed to Hubitat configuration.

### Option B - Manual install
1. Hubitat admin UI - Drivers Code:
   - Create a new driver for each file in drivers/ and paste the code.
2. Hubitat admin UI - Apps Code:
   - No app is required for this project.

## Hubitat configuration

You can use this project in two ways.

### Option 1 - Single trigger device (one Alexa routine)
1. Hubitat admin UI - Devices - Add Virtual Device.
2. Name: choose a name you will recognize in Alexa.
3. Type: Hubitat Alexa Routine Trigger - Virtual Contact Trigger.
4. Click Save Device.
5. On the device page:
   - Open Preferences, confirm Options, and click Done.
   - Run the Test command.

### Option 2 - Trigger group (multiple Alexa routines in order)
1. Hubitat admin UI - Devices - Add Virtual Device.
2. Name: choose a name you will use in Hubitat automations (this is the group controller).
3. Type: Hubitat Alexa Routine Trigger - MultiTrigger Controller.
4. Click Save Device.
5. On the device page:
   - Open Preferences.
   - Set Group name and How many child triggers to create.
   - Click Done.
6. The controller will create child devices named like: <Group name> - Trigger 1, Trigger 2, etc.
7. Optional cleanup:
   - If you later reduce the trigger count, extra children will remain (so you do not accidentally lose Alexa routines).
   - To remove extras, run the DeleteExtraChildTriggers command on the controller.

## Alexa configuration (required)

### Step 1 - Expose the triggers to Alexa
1. In Hubitat, open Apps - Amazon Echo Skill.
2. Under Select devices to expose, select:
   - Any single trigger devices you created, and
   - The child trigger devices created by your group controller.
3. Click Done.

### Step 2 - Discover devices in Alexa
1. Open the Alexa app.
2. Run device discovery (Devices - Add - Add Device, then follow prompts).
3. Confirm you see your trigger devices as contact sensors.

### Step 3 - Create Alexa routines
For each routine you want to trigger:
1. Alexa app - Routines - New routine.
2. When this happens - Smart Home - select the trigger device.
3. Choose Contact sensor - Opens.
4. Add actions (what you want Alexa to do).
5. Save.

Group sequencing model
- Each child trigger should map to one routine.
- When the controller runs, it opens Trigger 1, then Trigger 2, and so on, applying the configured delay between triggers.

## Validation

### What success looks like
- Single trigger: running Test opens the contact and Alexa runs the routine.
- Group trigger: running Test on the controller causes Alexa routines for Trigger 1..N to run in order.

### Where to look
- Hubitat device page:
  - LastTestAt, LastTestResult, LastError
- Hubitat Logs:
  - Temporarily enable Debug logging if troubleshooting.

If validation fails, see docs/troubleshooting.md.
