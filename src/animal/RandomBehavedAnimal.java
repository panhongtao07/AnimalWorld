package animal;

import java.util.*;

import food.*;

public abstract class RandomBehavedAnimal extends Animal {
    Random behavior;

    RandomBehavedAnimal() {
        super();
        behavior = new Random();
        refreshState();
    }

    @Override
    public int chooseFoods(List<Food> allFoods, int maxNum) {
        List<Food> eatableFoods = getFoods(allFoods);
        if (eatableFoods.isEmpty()) {
            System.out.println(toString() + "暂时没有想吃的东西");
        }
        List<Food> chosenFoods = new ArrayList<Food>();
        while (!eatableFoods.isEmpty() && maxNum-- > 0) {
            int index = behavior.nextInt(eatableFoods.size());
            Food food = eatableFoods.remove(index);
            chosenFoods.add(food);
        }
        return addFoods(chosenFoods);
    }

    @Override
    public int chooseFoods(List<Food> allFoods) {
        return chooseFoods(allFoods, 1);
    }

    @Override
    protected Food foodToEat(List<Food> foods) {
        if (foods.size() == 0) {
            return null;
        }
        return foods.get(behavior.nextInt(foods.size()));
    }

    public abstract void behaveRandomly(List<Food> allFoods);
}
