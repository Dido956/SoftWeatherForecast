package softuni.exam.models.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class CountriesSeedDto {

    @Expose
    @Size(min = 2, max = 60)
    private String countryName;

    @Expose
    @Size(min = 2, max = 20)
    private String currency;


    public String getCountryName() {
        return countryName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
