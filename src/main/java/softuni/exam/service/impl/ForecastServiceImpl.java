package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastsRootSeedDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfTheWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.util.List;

import static softuni.exam.util.Constants.*;

@Service
public class ForecastServiceImpl implements ForecastService {

    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    public ForecastServiceImpl(ForecastRepository forecastRepository, ValidationUtil validationUtil, ModelMapper modelMapper, XmlParser xmlParser, CityRepository cityRepository) {
        this.forecastRepository = forecastRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.cityRepository = cityRepository;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files
                .readString(Path.of(FORECASTS_PATH_FILE));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        ForecastsRootSeedDto forecastsRootSeedDto = xmlParser.fromFile(FORECASTS_PATH_FILE, ForecastsRootSeedDto.class);
        xmlParser.fromFile(FORECASTS_PATH_FILE, ForecastsRootSeedDto.class)
                .getForecasts()
                .stream()
                .filter(forecastSeedDto -> {
                    boolean isValid = validationUtil.isValid(forecastSeedDto);

                    Forecast forecast = forecastRepository
                            .findAllByCityAndDayOfWeek(
                                    cityRepository.getById(
                                            forecastSeedDto.getCity()),
                                    forecastSeedDto.getDayOfWeek()).orElse(null);

                    if (forecast != null) {
                        isValid = false;
                    }

                    sb.append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_FORECAST,
                                    forecastSeedDto.getDayOfWeek(),
                                    forecastSeedDto.getMaxTemperature())
                                    : String.format(INVALID, FORECAST))
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(forecastSeedDto -> {
                    Forecast forecast = modelMapper.map(forecastSeedDto, Forecast.class);

                    forecast.setCity(findById(forecastSeedDto.getCity()));
                    return forecast;
                })
                .forEach(forecastRepository::save);


        return sb.toString().trim();
    }

    private City findById(Long id) {
        return cityRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public String exportForecasts() {
        StringBuilder sb = new StringBuilder();

        forecastRepository.findByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDesc(DayOfTheWeek.SUNDAY, 150000)
                .forEach(forecast -> {
                    sb.append(forecast.toString());
                });


        return sb.toString().trim();
    }
}
