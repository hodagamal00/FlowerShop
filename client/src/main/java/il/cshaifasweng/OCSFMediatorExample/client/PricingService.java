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
                ? calculateDiscountedPrice(basePrice, product.getDiscountPercent())
                : basePrice;

        boolean subscriptionApplied = false;
        double finalPrice = promotionPrice;
        if (account != null && account.isSubscription() && promotionPrice > 50.0) {
            finalPrice = calculateDiscountedPrice(promotionPrice, SUBSCRIPTION_DISCOUNT_PERCENT);
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

    public static CartTotals calculateCartTotals(Iterable<Product> items, Account account) {
        double baseTotal = 0.0;
        double finalTotal = 0.0;
        if (items != null) {
            for (Product product : items) {
                if (product == null) {
                    continue;
                }
                PricingResult pricing = calculatePricing(product, account);
                baseTotal += pricing.getPromotionPrice();
                finalTotal += pricing.getFinalPrice();
            }
        }
        return new CartTotals(roundCurrency(baseTotal), roundCurrency(finalTotal));
    }

    public static double calculateOrderTotal(Iterable<Product> items, Account account, double deliveryFee) {
        double total = 0.0;
        if (items != null) {
            for (Product product : items) {
                if (product == null) {
                    continue;
                }
                total += calculateDisplayPrice(product, account);
            }
        }
        total += deliveryFee;
        return roundCurrency(total);
    }

    public static double calculateSubtotal(Iterable<Double> prices) {
        double subtotal = 0.0;
        if (prices != null) {
            for (Double price : prices) {
                if (price == null) {
                    continue;
                }
                subtotal += price;
            }
        }
        return roundCurrency(subtotal);
    }

    public static double calculateDiscountAmount(double subtotal, double deliveryFee, double total) {
        double discount = Math.max(0.0, (subtotal + deliveryFee) - total);
        return roundCurrency(discount);
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

    public static class CartTotals {
        private final double baseTotal;
        private final double finalTotal;

        public CartTotals(double baseTotal, double finalTotal) {
            this.baseTotal = baseTotal;
            this.finalTotal = finalTotal;
        }

        public double getBaseTotal() {
            return baseTotal;
        }

        public double getFinalTotal() {
            return finalTotal;
        }
    }

    public static double calculateDiscountedPrice(double basePrice, double discountPercent) {
        double normalizedDiscount = Product.normalizeDiscountPercent(discountPercent);
        double discountedPrice = basePrice * (1 - normalizedDiscount / 100.0);
        return roundCurrency(discountedPrice);
    }

    public static double roundCurrency(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
