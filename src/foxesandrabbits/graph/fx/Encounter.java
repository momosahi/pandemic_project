package foxesandrabbits.graph.fx;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Represents a Fox-Rabbit encounter.
 *
 * @author Peter Sander
 */
class Encounter {
    static final String FOX_RABBIT = "Fox meets Rabbit.\nDoesn't end well for rabbit";
    // private static final Map<Encounter, BiConsumer<Animal, Animal>> outcome
    //         = new HashMap<Encounter, BiConsumer<Animal, Animal>>() {{
    //             put(new Encounter(Fox.class, Rabbit.class),
    //                 (f, r) -> System.out.println("Fox " + f + " meets Rabbit " + r
    //                     + ".\nDoesn't end well for rabbit;"));
    //             put(new Encounter(Rabbit.class, Fox.class),
    //                 get(new Encounter(Fox.class, Rabbit.class)));
    // }};
    static final Map<Integer, BiFunction<Animal, Animal, String>> outcome
        = new HashMap<>() {{
        put(animalKey(Fox.class, Rabbit.class), (f, r) -> FOX_RABBIT);
        put(animalKey(Rabbit.class, Fox.class),
            get(animalKey(Fox.class, Rabbit.class)));
    }};
    private final Class<? extends Animal> animal1;
    private final Class<? extends Animal> animal2;


    Encounter(Class<? extends Animal> animal1, Class<? extends Animal> animal2) {
        this.animal1 = animal1;
        this.animal2 = animal2;
    }

    static String outcome(Animal animal1, Animal animal2) {
        return outcome.get(animalKey(animal1, animal2)).apply(animal1, animal2);
    }

    static int animalKey(Animal animal1, Animal animal2) {
        return animalKey(animal1.getClass(), animal2.getClass());
    }

    static int animalKey(Class<? extends Animal> animal1Class,
                         Class<? extends Animal> animal2Class) {
        return animal1Class.hashCode() + animal2Class.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Encounter)) {
            return false;
        } else {
            Encounter encounter = (Encounter) obj;
            return this.animal1 == encounter.animal1 && this.animal2 == encounter.animal2
                || this.animal1 == encounter.animal2 && this.animal2 == encounter.animal1;
        }
    }

    @Override
    public int hashCode() {
        return animal1.hashCode() + animal2.hashCode();
    }
}
