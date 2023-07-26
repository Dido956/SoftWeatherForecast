package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.gson.CountriesSeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static softuni.exam.util.Constants.*;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files
                .readString(Path.of(COUNTRIES_PATH_FILE));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();


        CountriesSeedDto[] countriesSeedDtos = gson.fromJson(readCountriesFromFile(), CountriesSeedDto[].class);
        Arrays.stream(gson
                        .fromJson(readCountriesFromFile(), CountriesSeedDto[].class))
                .filter(countriesSeedDto -> {
                    boolean isValid = validationUtil.isValid(countriesSeedDto)
                            && !existsByName(countriesSeedDto.getCountryName());

                    sb.append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_COUNTRY,
                                    countriesSeedDto.getCountryName(),
                                    countriesSeedDto.getCurrency())
                                    : String.format(INVALID, COUNTRY))
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(countriesSeedDto -> modelMapper.map(countriesSeedDto, Country.class))
                .forEach(countryRepository::save);


        return sb.toString().trim();
    }

    private boolean existsByName(String countryName) {
        return countryRepository.existsByCountryName(countryName);
    }
}
