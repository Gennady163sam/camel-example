package com.zic;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "city")
public class PeopleStorage {
    private String name;
    private List<Person> people;


    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPeople() {
        return people;
    }

    @XmlElement(name = "person")
    public void setPeople(List<Person> people) {
        this.people = people;
    }
}
