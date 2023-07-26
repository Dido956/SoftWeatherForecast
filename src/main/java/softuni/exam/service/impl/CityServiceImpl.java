package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.gson.CitiesSeedDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.util.Constants.*;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files
                .readString(Path.of(CITIES_PATH_FILE));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder sb = new StringBuilder();


        CitiesSeedDto[] citiesSeedDtos = gson.fromJson(readCitiesFileContent(), CitiesSeedDto[].class);
        Arrays.stream(gson
                        .fromJson(readCitiesFileContent(), CitiesSeedDto[].class))
                .filter(citiesSeedDto -> {
                    boolean isValid = validationUtil.isValid(citiesSeedDto)
                            && !existsByName(citiesSeedDto.getCityName());

                    sb.append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_CITY,
                                    citiesSeedDto.getCityName(),
                                    citiesSeedDto.getPopulation())
                                    : String.format(INVALID, CITY))
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(citiesSeedDto -> {
                    City city = modelMapper.map(citiesSeedDto, City.class);

                    city.setCountry(findById(citiesSeedDto.getCountry()));

                    return city;
                })
                .forEach(cityRepository::save);


        return sb.toString().trim();
    }

    private Country findById(Long id) {
        return countryRepository
                .findById(id)
                .orElse(null);
    }

    private boolean existsByName(String cityName) {
        return cityRepository.existsByCityName(cityName);
    }
}
