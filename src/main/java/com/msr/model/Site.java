package com.msr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class Site {
    @Id
    private int id;

    private String name;

    private String address;

    private String city;

    private String state;

    private String zipcode;

    @Transient
    @JsonProperty("total_size")
    private int totalSize;

    @JsonIgnore
    @JsonIgnoreProperties("site")
    @OneToMany(mappedBy = "site", fetch = FetchType.EAGER)
    private List<SiteUse> siteUses;

    @Transient
    @JsonProperty("primary_type")
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
    private UseType primaryType;
}
