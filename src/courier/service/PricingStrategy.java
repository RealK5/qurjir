package courier.service;

import courier.domain.Parcel;

public interface PricingStrategy {
    double calculatePrice(Parcel parcel);
    String getStrategyName();
}
