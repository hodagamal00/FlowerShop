package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;

public final class PricingService {

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
        boolean hasPromotion = product.hasActivePromotion();
        double promotionPrice = hasPromotion
                ? Product.calculateDiscountedPrice(basePrice, product.getDiscountPercent())
                : basePrice;

        double privilegeDiscountPercent = resolvePrivilegeDiscountPercent(privilege);
        boolean hasPrivilegeDiscount = privilegeDiscountPercent > 0;
        double finalPrice = promotionPrice;
        double roundedFinalPrice = Product.roundCurrency(finalPrice);
        double roundedPromotionPrice = Product.roundCurrency(promotionPrice);

        return new PricingResult(basePrice, roundedPromotionPrice, roundedFinalPrice, hasPromotion, hasPrivilegeDiscount);
    }

    public static double calculateDisplayPrice(Product product, Account account) {
        return calculatePricing(product, account).getFinalPrice();
    }

    public static double calculateDisplayPrice(Product product, int privilege) {
        return calculatePricing(product, privilege).getFinalPrice();
    }

    private static double resolvePrivilegeDiscountPercent(int privilege) {
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
