# Love Message Scheduler (Android source)

This project schedules one randomized WhatsApp message in each enabled time window every day. Enter the recipient's number with country code and digits only. The three windows and message lists can be changed in the app.

## Important Android limitation

WhatsApp does not provide a public personal-account API for silently sending scheduled messages. Android requires the user to enable the app's Accessibility service once so the app can press WhatsApp's Send button. This is an Android system permission, and no sideloaded app can bypass it. Sending is otherwise unattended after setup, but Android battery management, background launch restrictions, WhatsApp UI changes, connectivity, or account state can delay or prevent delivery. Do not use this for sensitive messages.

The app uses Android's inexact idle-friendly alarm API so it does not request exact-alarm access. Random send times may shift due to Doze and phone manufacturer battery controls. Schedules are restored after reboot.

## Build

Open this folder in Android Studio, allow Gradle to sync, then choose **Build > Build APK(s)**. The generated APK is normally at `app/build/outputs/apk/debug/app-debug.apk`. Alternatively, the included GitHub Actions workflow builds the APK on every push; it uploads `app-debug.apk` as a downloadable workflow artifact.

## Setup

1. Install the APK and open **Love Message Scheduler**.
2. Enter the WhatsApp number, including the country code and without spaces or a leading `+`.
3. Edit the time windows and one-message-per-line message lists.
4. Tap **Save and schedule**.
5. Tap **One-time setup: enable WhatsApp sending**, then enable **WhatsApp message sender** in Android Accessibility settings.
6. Keep WhatsApp installed and signed in. Disable aggressive battery restrictions for this app if the phone delays background work.

To stop the schedule, open the app and tap **Stop all scheduled messages**.
