//
// Created by Cadis on 17/06/25.
//

import Foundation
import UIKit
import FirebaseKit
import FirebaseAuth
import FirebaseCore
import FirebaseMessaging

class AppDelegate: UIResponder, UIApplicationDelegate, MessagingDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        print("Firebase Initializer: Starting setup...")

        FirebaseApp.configure()
        Messaging.messaging().delegate = self
        UNUserNotificationCenter.current().delegate = self
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, _ in
            if granted {
                DispatchQueue.main.async {
                    application.registerForRemoteNotifications()
                }
            }
        }

        if let projectId = FirebaseApp.app()?.options.projectID {
            print("Firebase Initializer: Configured with Project ID: \(projectId)")
        }

        return true
    }

    func application(
        _ application: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey : Any]
    ) -> Bool {
        return Auth.auth().canHandle(url);
    }

    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification notification: [AnyHashable : Any],
        fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void
    ) {
        if Auth.auth().canHandleNotification(notification) {
            completionHandler(.noData)
        } else {
            Messaging.messaging().appDidReceiveMessage(notification)
            completionHandler(.newData)
        }
    }

    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ){
        Auth.auth().setAPNSToken(deviceToken, type: .unknown)
        Messaging.messaging().apnsToken = deviceToken
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        if let fcmToken {
            NotificationCenter.default.post(
                name: .onNewToken,
                object: nil,
                userInfo: ["fcmToken": fcmToken]
            )
        }
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification
    ) async -> UNNotificationPresentationOptions {
        return [.badge, .banner, .sound]
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse
    ) async {
        let userInfo = response.notification.request.content.userInfo
        NotificationCenter.default.post(
            name: .onNotificationClicked,
            object: nil,
            userInfo: userInfo
        )
    }
}

extension Notification.Name {
    static let onNewToken = Notification.Name("onNewToken")
    static let onNotificationClicked = Notification.Name("onNotificationClicked")
}
