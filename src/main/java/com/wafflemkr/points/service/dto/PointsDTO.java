package com.wafflemkr.points.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Points entity.
 */
public class PointsDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private Integer exercise;

    private Integer meals;

    private Integer alcohol;

    @NotNull
    @Size(max = 140)
    private String notes;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getExercise() {
        return exercise;
    }

    public void setExercise(Integer exercise) {
        this.exercise = exercise;
    }

    public Integer getMeals() {
        return meals;
    }

    public void setMeals(Integer meals) {
        this.meals = meals;
    }

    public Integer getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

        PointsDTO pointsDTO = (PointsDTO) o;
        if(pointsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pointsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PointsDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", exercise=" + getExercise() +
            ", meals=" + getMeals() +
            ", alcohol=" + getAlcohol() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
