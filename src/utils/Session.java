package utils;

import model.User;

public class Session {
    private static User currentUser = null;

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getRole() {
        if (currentUser != null) {
            return currentUser.getRole();
        }
        return null;
    }

    public static boolean isSuperAdmin() {
        return currentUser != null && "super_admin".equals(currentUser.getRole());
    }

    public static boolean isOwner() {
        return currentUser != null && "owner".equals(currentUser.getRole());
    }

    public static boolean isPelanggan() {
        return currentUser != null && "pelanggan".equals(currentUser.getRole());
    }
}