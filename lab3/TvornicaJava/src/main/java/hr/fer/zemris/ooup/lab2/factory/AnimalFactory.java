package hr.fer.zemris.ooup.lab2.factory;

import hr.fer.zemris.ooup.lab2.model.Animal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

public class AnimalFactory {
    public static Animal newInstance(String animalKind, String name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> animalClass =  Class.forName("hr.fer.zemris.ooup.lab2.model.plugins."+animalKind);
        Constructor<?> animalConstructor = animalClass.getConstructor(String.class);

        return (Animal) animalConstructor.newInstance(name);
    }

}
