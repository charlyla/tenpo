package com.tenpo.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("tenp_sum")
public class TenpSum implements Serializable {

    @Id
    @JsonProperty("id")
    private Long id;
    private double value;
    private String ip;
}
