package softuni.exam.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country extends BaseEntity{

    @Column(name = "country_name",unique = true)
    private String countryName;

    @Column(name = "currency",nullable = false)
    private String currency;
}
