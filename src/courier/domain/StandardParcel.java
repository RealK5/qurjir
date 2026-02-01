package courier.domain;

public class StandardParcel extends Parcel
{
    private static final double BASE_COST = 10.0;  // zł
    private static final double COST_PER_KG = 2.0;  // zł/kg

    public StandardParcel(String id, double weight, String dimensions, Address senderAddress, Address recipientAddress)
    {
        super(id, weight, dimensions, senderAddress, recipientAddress);
    }

    @Override
    public String getHandlingRequirements()
    {
        return "Standardowa obsługa. Przechowywać w suchym miejscu.";
    }

    @Override
    public double calculateShippingCost()
    {
        return BASE_COST + (getWeight() * COST_PER_KG);
    }

    @Override
    public boolean canBeAddedToRoute()
    {
        return super.canBeAddedToRoute() && getWeight() <= 30.0;
    }
}
