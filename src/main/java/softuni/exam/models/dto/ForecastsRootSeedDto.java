package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "forecasts")
@XmlAccessorType(XmlAccessType.FIELD)
public class ForecastsRootSeedDto {


    @XmlElement(name = "forecast")
    private List<ForecastSeedDto> forecasts;

    public List<ForecastSeedDto> getForecasts() {
        return forecasts;
    }
}