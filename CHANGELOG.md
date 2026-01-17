# Changelog

This project uses semantic versioning.

## 0.9.0 - 2026-01-17

Breaking change
- Driver-only release: removed the Hubitat app requirement. All trigger creation and group triggering is now handled by drivers.

Added
- MultiTrigger Controller driver can create and manage its own child trigger devices (Child Contact) without an app.
- All drivers now include the portfolio-standard preference section order (Overview, Setup, Options, Test, Advanced, Support).
- All drivers now include an explicit Test command with last test timestamp and result.

Changed
- MultiTrigger Controller no longer calls parent app methods. Group options now live on the controller device.
- Child Contact driver no longer expects settings to be pushed from an app. Group metadata is stored as device data values.
- Logging standardized to a single selector: Off (default), Basic, Debug, with optional Debug-for-30-minutes.

Fixed
- Eliminated remaining UI patterns that can cause compilation issues on some Hubitat environments (no paragraph() usage).

## 0.8.2 - 2026-01-17

- Compatibility: removed all uses of paragraph() from driver preference UI to avoid driver compilation errors on some Hubitat environments.
- Drivers: version bumped to 0.8.2 (Virtual Contact Trigger, Child Contact, MultiTrigger Controller).
- Repo: restructured to the standard layout (/apps, /drivers, /docs, /hpm, /.github) for a public GitHub release.
- Docs: added setup and troubleshooting guides, including required Alexa routine steps.

## 0.7.2 - 2026-01-17

- Baseline app version as provided in HubitatAlexaRoutineTrigger_v0.7.2.groovy.
