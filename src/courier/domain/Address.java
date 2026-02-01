package courier.domain;

import java.util.Objects;

public class Address
{
    private final String street;
    private final String city;
    private final String postalCode;
    private final String country;

    public Address(String street, String city, String postalCode, String country)
    {
        this.street = validateNotEmpty(street, "Ulica");
        this.city = validateNotEmpty(city, "Miasto");
        this.postalCode = validateNotEmpty(postalCode, "Kod pocztowy");
        this.country = validateNotEmpty(country, "Kraj");
    }

    private String validateNotEmpty(String value, String fieldName)
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " nie może być puste");
        }
        return value.trim();
    }

    public String getStreet()
    {
        return street;
    }
    public String getCity()
    {
        return city;
    }
    public String getPostalCode()
    {
        return postalCode;
    }
    public String getCountry()
    {
        return country;
    }

    @Override
    public String toString()
    {
        return String.format("%s, %s %s, %s", street, postalCode, city, country);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(city, address.city) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(country, address.country);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(street, city, postalCode, country);
    }
}
