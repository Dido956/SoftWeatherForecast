package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfTheWeek;
import softuni.exam.models.entity.Forecast;

import javax.persistence.Table;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findAllByCityAndDayOfWeek(City city, DayOfTheWeek dayOfTheWeek);

    List<Forecast> findByDayOfWeekAndCityPopulationLessThanOrderByMaxTemperatureDesc(DayOfTheWeek dayOfWeek, Integer populationLimit);

}
