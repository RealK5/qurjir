package courier.service;

import courier.domain.Parcel;

public class DiscountPricingStrategy implements PricingStrategy {
    private final double discountPercent;

    public DiscountPricingStrategy(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Rabat musi być między 0 a 100%");
        }
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculatePrice(Parcel parcel) {
        double basePrice = parcel.calculateShippingCost();
        return basePrice * (1 - discountPercent / 100.0);
    }

    @Override
    public String getStrategyName() {
        return String.format("Rabat %.0f%%", discountPercent);
    }
}
