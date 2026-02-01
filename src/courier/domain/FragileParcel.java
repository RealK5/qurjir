package courier.domain;

public class FragileParcel extends Parcel
{
    private static final double BASE_COST = 15.0;  // zł
    private static final double COST_PER_KG = 3.5;  // zł/kg (wyższa stawka)
    private static final double FRAGILE_SURCHARGE = 20.0;  // dodatkowa opłata

    public FragileParcel(String id, double weight, String dimensions, Address senderAddress, Address recipientAddress)
    {
        super(id, weight, dimensions, senderAddress, recipientAddress);
    }

    @Override
    public String getHandlingRequirements()
    {
        return "⚠ DELIKATNA PACZKA ⚠\n" +
                "- Nie stawiać na przednią stronę\n" +
                "- Przechowywać poziomo\n" +
                "- Unikać przechyłów\n" +
                "- Wymagana ostrożna obsługa";
    }

    @Override
    public double calculateShippingCost()
    {
        return BASE_COST + (getWeight() * COST_PER_KG) + FRAGILE_SURCHARGE;
    }

    @Override
    public boolean canBeAddedToRoute()
    {
        // Paczki delikatne mają ograniczenia wagi
        return super.canBeAddedToRoute() && getWeight() <= 15.0;
    }
}
