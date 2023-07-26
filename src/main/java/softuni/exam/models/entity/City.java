package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cities")
public class City extends BaseEntity{

    @Column(name = "city_name",unique = true,nullable = false)
    private String cityName;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "population", nullable = false)
    private Integer population;

    @ManyToOne
    private Country country;
}
