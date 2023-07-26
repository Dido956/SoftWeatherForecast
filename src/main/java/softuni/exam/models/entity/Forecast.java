package softuni.exam.models.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;

@Entity
@Table(name = "forecasts")
public class Forecast extends BaseEntity {

    @Column(name = "day_of_week",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfTheWeek dayOfWeek;

    @Column(name = "max_temperature",
            nullable = false)
    private Float maxTemperature;

    @Column(name = "min_temperature",
            nullable = false)
    private Float minTemperature;

    @Column(name = "sunrise", nullable = false)
    private Time sunrise;

    @Column(name = "sunset", nullable = false)
    private Time sunset;

    @ManyToOne
    private City city;

    public DayOfTheWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfTheWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Float getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Float maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Float getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Float minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Time getSunrise() {
        return sunrise;
    }

    public void setSunrise(Time sunrise) {
        this.sunrise = sunrise;
    }

    public Time getSunset() {
        return sunset;
    }

    public void setSunset(Time sunset) {
        this.sunset = sunset;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return String.format("""
                City: %s
                -min temperature: %.2f
                --max temperature: %.2f
                ---sunrise: %s
                ----sunset: %s%n""",
                city.getCityName(),
                minTemperature,
                maxTemperature,
                sunrise,
                sunset);
    }
}
