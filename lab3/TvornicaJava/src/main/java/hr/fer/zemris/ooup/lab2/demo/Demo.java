package hr.fer.zemris.ooup.lab2.demo;


import hr.fer.zemris.ooup.lab2.factory.AnimalFactory;
import hr.fer.zemris.ooup.lab2.model.Animal;
import hr.fer.zemris.ooup.lab2.model.plugins.Dog;
import hr.fer.zemris.ooup.lab2.model.plugins.Parrot;

import java.lang.reflect.InvocationTargetException;

public class Demo {
    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        System.out.println("=".repeat(10) + "PARROT"+ "=".repeat(10));
        Animal parrot = AnimalFactory.newInstance("Parrot","parrot");
        parrot.animalPrintGreeting();
        parrot.animalPrintMenu();
        System.out.println(parrot.name());
        if(parrot instanceof Parrot){
            System.out.println("Instance of Parrot");
        }
        System.out.println();

        System.out.println("=".repeat(10) + "DOG"+ "=".repeat(10));
        Animal dog = AnimalFactory.newInstance("Dog", "Ben");
        dog.animalPrintGreeting();
        dog.animalPrintMenu();
        System.out.println(dog.name());
        if(dog instanceof Dog){
            System.out.println("Instance of Dog");
        }
        System.out.println();

    }

  }
