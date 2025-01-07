package br.com.gs3tecnologia.user_management.model.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "profiles")
public class ProfileEntity extends BaseEntity {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
