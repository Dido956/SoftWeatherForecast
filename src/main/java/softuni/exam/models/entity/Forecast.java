package softuni.exam.models.entity;

import javax.persistence.*;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.List;

@Entity
@Table(name = "forecasts")
public class Forecast extends BaseEntity {

    @Column(name = "day_of_week",
            nullable = false)
    @Enumerated(EnumType.ORDINAL)
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
}
