import Foundation
import Capacitor

/**
 * This package intentionally implements the persistent ntfy transport only on Android.
 * Use APNs through Capacitor's official push plugin for production iOS delivery.
 */
@objc(NtfyPlugin)
public class NtfyPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "NtfyPlugin"
    public let jsName = "Ntfy"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "start", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "stop", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "getStatus", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "getMessages", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "clearMessages", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "publish", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "requestNotificationPermission", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "getNotificationPermission", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "isIgnoringBatteryOptimizations", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "openBatteryOptimizationSettings", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "openAppSettings", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = Ntfy()

    @objc func start(_ call: CAPPluginCall) {
        call.reject("The persistent ntfy transport is Android-only. Use APNs on iOS.")
    }

    @objc func stop(_ call: CAPPluginCall) {
        call.resolve(implementation.unsupportedStatus())
    }

    @objc func getStatus(_ call: CAPPluginCall) {
        call.resolve(implementation.unsupportedStatus())
    }

    @objc func getMessages(_ call: CAPPluginCall) {
        call.resolve(["messages": []])
    }

    @objc func clearMessages(_ call: CAPPluginCall) {
        call.resolve()
    }

    @objc func publish(_ call: CAPPluginCall) {
        call.reject("Publishing is not implemented by the iOS stub.")
    }

    @objc func requestNotificationPermission(_ call: CAPPluginCall) {
        call.resolve(["state": "unsupported"])
    }

    @objc func getNotificationPermission(_ call: CAPPluginCall) {
        call.resolve(["state": "unsupported"])
    }

    @objc func isIgnoringBatteryOptimizations(_ call: CAPPluginCall) {
        call.resolve(["value": false])
    }

    @objc func openBatteryOptimizationSettings(_ call: CAPPluginCall) {
        call.reject("Battery optimization settings are only available on Android.")
    }

    @objc func openAppSettings(_ call: CAPPluginCall) {
        call.reject("Use Capacitor's App plugin or UIApplication.openSettingsURLString on iOS.")
    }
}
