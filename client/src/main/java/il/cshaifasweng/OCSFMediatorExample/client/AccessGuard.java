package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;

import java.util.HashMap;
import java.util.Map;

public final class AccessGuard {
    private static final Map<String, String> PAGE_NAME_OVERRIDES = buildPageNameOverrides();

    private AccessGuard() {
    }

    public static boolean requireMinPrivilege(int requiredPrivilege) {
        Account account = SimpleClient.getAccount();
        int currentPrivilege = account != null ? account.getPrivilegeLevel() : 0;
        if (currentPrivilege < requiredPrivilege) {
            String pageName = resolveAttemptedPage();
            AccessDeniedController.setAccessInfo(currentPrivilege, requiredPrivilege, pageName);
            AccessDeniedController.setReturnPage("Catalog");
            NavigationService.getInstance().navigate("AccessDenied");
            return false;
        }
        return true;
    }

    private static String resolveAttemptedPage() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            String className = element.getClassName();
            if (!className.startsWith("il.cshaifasweng.OCSFMediatorExample.client.")) {
                continue;
            }
            if (className.endsWith("AccessGuard")) {
                continue;
            }
            String simpleName = className.substring(className.lastIndexOf('.') + 1);
            String override = PAGE_NAME_OVERRIDES.get(simpleName);
            if (override != null) {
                return override;
            }
            if (simpleName.endsWith("Controller")) {
                simpleName = simpleName.substring(0, simpleName.length() - "Controller".length());
            }
            return simpleName.replaceAll("([a-z])([A-Z])", "$1 $2");
        }
        return "Restricted Page";
    }

    private static Map<String, String> buildPageNameOverrides() {
        Map<String, String> overrides = new HashMap<>();
        overrides.put("BranchReportsController", "Branch Reports");
        overrides.put("BranchSettingsController", "Branch Settings");
        overrides.put("PromotionsManagementController", "Promotions Management");
        overrides.put("NetworkDashboardController", "Network Dashboard");
        overrides.put("CrossBranchReportsController", "Cross-Branch Reports");
        overrides.put("GlobalSettingsController", "Global Settings");
        overrides.put("NetworkPromotionsController", "Network Promotions");
        overrides.put("RoleManagementController", "Role Management");
        overrides.put("ReplyComplaintController", "Reply Complaints");
        return overrides;
    }
}
