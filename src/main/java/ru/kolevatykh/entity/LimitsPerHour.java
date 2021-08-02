package ru.kolevatykh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity(name = "limits_per_hour")
@Table(schema = "traffic_limits", name = "limits_per_hour")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitsPerHour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "gen_random_uuid()")
    private UUID id;

    @Basic(optional = false)
    @Column(name = "limit_name")
    private String limitName;

    @Basic(optional = false)
    @Column(name = "limit_value")
    private Integer limitValue;

    @Basic(optional = false)
    @Column(name = "effective_date")
    private OffsetDateTime effectiveDate;

    public LimitsPerHour(String name, Integer value, OffsetDateTime date) {
        this.limitName = name;
        this.limitValue = value;
        this.effectiveDate = date;
    }
}
