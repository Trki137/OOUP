package hr.fer.zemris.ooup.lab2.model.plugins;

import hr.fer.zemris.ooup.lab2.model.Animal;

public class Dog extends Animal {
    private String animalName;

    public Dog(String animalName){
        this.animalName = animalName;
    }

    @Override
    public String name() {
        return animalName;
    }

    @Override
    public String greet() {
        return "Vau vau";
    }

    @Override
    public String menu() {
        return "Meat";
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }
}
