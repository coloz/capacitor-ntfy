import Foundation

@objc public class Ntfy: NSObject {
    @objc public func unsupportedStatus() -> [String: Any] {
        return [
            "state": "stopped",
            "running": false,
            "connected": false,
            "batteryOptimizationsIgnored": false,
            "notificationPermission": "unsupported"
        ]
    }
}
