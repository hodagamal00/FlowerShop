package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;

public final class PricingService {

    private static final double WORKER_DISCOUNT_PERCENT = 10.0;
    private static final double MANAGER_DISCOUNT_PERCENT = 15.0;
    private static final double CHAIN_MANAGER_DISCOUNT_PERCENT = 20.0;

    private PricingService() {
    }

    public static PricingResult calculatePricing(Product product, Account account) {
        int privilege = account != null ? account.getPrivilegeLevel() : 0;
        return calculatePricing(product, privilege);
    }

    public static PricingResult calculatePricing(Product product, int privilege) {
        if (product == null) {
            return new PricingResult(0.0, 0.0, 0.0, false, false);
        }

        double basePrice = product.getPrice();
        boolean hasPromotion = product.isPromotion() && product.getDiscountPercent() > 0;
        double promotionPrice = basePrice;
        if (hasPromotion) {
            promotionPrice = basePrice * (1 - product.getDiscountPercent() / 100.0);
        }

        double privilegeDiscountPercent = resolvePrivilegeDiscountPercent(privilege);
        boolean hasPrivilegeDiscount = privilegeDiscountPercent > 0;
        double finalPrice = promotionPrice * (1 - privilegeDiscountPercent / 100.0);

        return new PricingResult(basePrice, promotionPrice, finalPrice, hasPromotion, hasPrivilegeDiscount);
    }

    public static double calculateDisplayPrice(Product product, Account account) {
        return calculatePricing(product, account).getFinalPrice();
    }

    public static double calculateDisplayPrice(Product product, int privilege) {
        return calculatePricing(product, privilege).getFinalPrice();
    }

    private static double resolvePrivilegeDiscountPercent(int privilege) {
        if (privilege >= 4) {
            return CHAIN_MANAGER_DISCOUNT_PERCENT;
        }
        if (privilege >= 3) {
            return MANAGER_DISCOUNT_PERCENT;
        }
        if (privilege >= 2) {
            return WORKER_DISCOUNT_PERCENT;
        }
        return 0.0;
    }

    public static class PricingResult {
        private final double basePrice;
        private final double promotionPrice;
        private final double finalPrice;
        private final boolean promotionApplied;
        private final boolean privilegeDiscountApplied;

        public PricingResult(double basePrice, double promotionPrice, double finalPrice,
                             boolean promotionApplied, boolean privilegeDiscountApplied) {
            this.basePrice = basePrice;
            this.promotionPrice = promotionPrice;
            this.finalPrice = finalPrice;
            this.promotionApplied = promotionApplied;
            this.privilegeDiscountApplied = privilegeDiscountApplied;
        }

        public double getBasePrice() {
            return basePrice;
        }

        public double getPromotionPrice() {
            return promotionPrice;
        }

        public double getFinalPrice() {
            return finalPrice;
        }

        public boolean isPromotionApplied() {
            return promotionApplied;
        }

        public boolean isPrivilegeDiscountApplied() {
            return privilegeDiscountApplied;
        }
    }
}
