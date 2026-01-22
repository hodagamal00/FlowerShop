package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Account;
import il.cshaifasweng.OCSFMediatorExample.entities.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PricingService {

    private static final double SUBSCRIPTION_DISCOUNT_PERCENT = 10.0;

    private PricingService() {
    }

    public static PricingResult calculatePricing(Product product, Account account) {
        if (product == null) {
            return new PricingResult(0.0, 0.0, 0.0, false, false);
        }

        double basePrice = roundCurrency(product.getPrice());
        boolean hasPromotion = product.hasActivePromotion();
        double promotionPrice = hasPromotion
                ? roundCurrency(Product.calculateDiscountedPrice(basePrice, product.getDiscountPercent()))
                : basePrice;

        boolean subscriptionApplied = false;
        double finalPrice = promotionPrice;
        if (account != null && account.isSubscription() && promotionPrice > 50.0) {
            finalPrice = roundCurrency(Product.calculateDiscountedPrice(promotionPrice, SUBSCRIPTION_DISCOUNT_PERCENT));
            subscriptionApplied = true;
        }

        return new PricingResult(basePrice, promotionPrice, finalPrice, hasPromotion, subscriptionApplied);
    }

    public static PricingResult calculatePricing(Product product, int privilege) {
        return calculatePricing(product, (Account) null);
    }

    public static double calculateDisplayPrice(Product product, Account account) {
        return calculatePricing(product, account).getFinalPrice();
    }

    public static double calculateDisplayPrice(Product product, int privilege) {
        return calculatePricing(product, privilege).getFinalPrice();
    }

    public static class PricingResult {
        private final double basePrice;
        private final double promotionPrice;
        private final double finalPrice;
        private final boolean promotionApplied;
        private final boolean subscriptionDiscountApplied;

        public PricingResult(double basePrice, double promotionPrice, double finalPrice,
                             boolean promotionApplied, boolean subscriptionDiscountApplied) {
            this.basePrice = basePrice;
            this.promotionPrice = promotionPrice;
            this.finalPrice = finalPrice;
            this.promotionApplied = promotionApplied;
            this.subscriptionDiscountApplied = subscriptionDiscountApplied;
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

        public boolean isSubscriptionDiscountApplied() {
            return subscriptionDiscountApplied;
        }
    }

    public static double roundCurrency(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
