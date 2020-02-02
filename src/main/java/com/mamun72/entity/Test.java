package com.mamun72.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Test {

    @Id
    @Column(name = "ID")
    Long ID;
    @Column(name = "NAME")
    String NAME;

    @Override
    public String toString() {
        return "Test{" +
                "ID=" + ID +
                ", NAME='" + NAME + '\'' +
                '}';
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
