package courier.service;

import courier.domain.Parcel;

public class BasePricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Parcel parcel) {
        return parcel.calculateShippingCost();
    }

    @Override
    public String getStrategyName() {
        return "Cena Bazowa";
    }
}
