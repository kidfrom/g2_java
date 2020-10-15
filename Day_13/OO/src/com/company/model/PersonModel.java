package com.company.model;

public class PersonModel {
  private final int id;
  private final String name;
  private final int age;

  public PersonModel(int id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }
}
