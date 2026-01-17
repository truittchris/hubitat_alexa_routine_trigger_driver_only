# Hubitat Alexa Routine Trigger

Current version: 0.9.0

One-line description: Create Alexa-friendly trigger devices so you can start Alexa routines from Hubitat automations.

## Standards
- UX: standards/Chris_Truitt_Hubitat_UX_Standard_v2.1.md
- Repo naming and structure: standards/Chris_Truitt_Git_Repo_Standard_v1.0.md

## What this project is
Purpose: Provide virtual contact sensors that Alexa routines can trigger from, plus an optional group controller that fires multiple triggers in order.

Components: Drivers only (no app required).

Included drivers
- Hubitat Alexa Routine Trigger - Virtual Contact Trigger
  - Use this for a single Alexa routine trigger.
- Hubitat Alexa Routine Trigger - MultiTrigger Controller
  - Use this to create and trigger a sequence of child contact triggers.
- Hubitat Alexa Routine Trigger - Child Contact
  - Created automatically by the MultiTrigger Controller. Can also be used standalone.

## Compatibility and requirements
- Hubitat platform: 2.1.9 or newer (recommended)
- Dependencies: Hubitat built-in Amazon Echo Skill app
- Cloud requirements: Alexa routines (required)

## Installation

### Option A - Hubitat Package Manager (recommended)
1. Install Hubitat Package Manager (HPM) if you have not already.
2. In HPM, add this project as a user repository using the raw URL to hpm/repository.json in this GitHub repo.
3. In HPM, choose Install, select this package, and follow the prompts.
4. For updates, run Update from HPM.

### Option B - Manual install
1. Hubitat admin UI - Drivers Code:
   - Create a new driver for each file in drivers/ and paste the code.
2. Hubitat admin UI - Apps Code:
   - No app is required for this project.
3. Hubitat admin UI - Devices:
   - Create devices as described in Quick start below.

## Quick start
1. Install using Option A or Option B.
2. Create either:
   - A single trigger device using the Virtual Contact Trigger driver, or
   - A group controller device using the MultiTrigger Controller driver.
3. If using a group controller, open the device preferences and click Done. The driver will create its child trigger devices.
4. In Hubitat, open the built-in Amazon Echo Skill app and select the trigger devices you want Alexa to see.
5. In the Alexa app, run Discover Devices.
6. Build Alexa routines using: When this happens - Contact sensor - Opens.
7. Test:
   - On a single trigger device, run the Test command.
   - On a group controller device, run the Test command.

Full steps: docs/setup.md

## External setup (Alexa - required)
- Prerequisites:
  - An Amazon account with the Alexa app installed
  - Hubitat's built-in Amazon Echo Skill app installed and configured
- Setup steps, verification, and troubleshooting are in docs/setup.md and docs/troubleshooting.md

## Upgrading
- If updating via HPM, follow the HPM prompts.
- After updating code, open the relevant device preferences and click Done to apply any new settings.

## Security and secrets
This project is source-distributed. Do not put secrets in the repo.

- Never commit tokens, client secrets, API keys, or personal endpoints.
- If a secret is required, it should be provided by the user in Hubitat preferences during setup.
- See docs/SECURITY_AND_SECRETS.md for the rules and a pre-publish checklist.

## Support
Support is handled through the support portal:
- https://www.christruitt.com/support

When requesting help, include:
- Hubitat platform version
- Driver version (shown on the device)
- Steps to reproduce
- Logs (enable Debug logging if requested)
- Screenshot(s) of the relevant preference screens

## Support the project
If you find this project useful, you can support ongoing development at https://christruitt.com/tip-jar. Your support helps fund maintenance, testing hardware, and future features.

## License
Apache-2.0. See LICENSE.
