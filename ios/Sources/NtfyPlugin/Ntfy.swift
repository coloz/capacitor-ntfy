import Foundation

@objc public class Ntfy: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
