package com.wafflemkr.points.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.wafflemkr.points.domain.enumeration.WeightUnit;

/**
 * A DTO for the Preferences entity.
 */
public class PreferencesDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 10)
    @Max(value = 21)
    private Integer weeklyGoals;

    @NotNull
    private WeightUnit weightUnit;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeeklyGoals() {
        return weeklyGoals;
    }

    public void setWeeklyGoals(Integer weeklyGoals) {
        this.weeklyGoals = weeklyGoals;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreferencesDTO preferencesDTO = (PreferencesDTO) o;
        if(preferencesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferencesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreferencesDTO{" +
            "id=" + getId() +
            ", weeklyGoals=" + getWeeklyGoals() +
            ", weightUnit='" + getWeightUnit() + "'" +
            "}";
    }
}
