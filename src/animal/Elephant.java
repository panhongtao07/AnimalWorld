package animal;

import java.util.*;

import food.*;

public class Elephant extends RandomBehavedAnimal {
    @Override
    public String toString() {
        return "大象";
    }

    @Override
    public boolean acceptFood(Food food) {
        // can't eat the food that are too small
        return super.acceptFood(food) && food.getQuantity() > nextEatQuantity * .5;
    }

    @Override
    protected void refreshState() {
        nextEatQuantity = behavior.nextExponential() * 1.5;
    }

    @Override
    protected double getEatTime(Food food, double quantity) {
        return behavior.nextExponential() * 5;
    }

    private final void drink() {
        nextEatQuantity *= .5;
        addUsedTime(behavior.nextExponential() * 5);
        System.out.println(toString() + "喝完水略微有点饱");
    }

    private final void rest() {
        refreshState();
        double time = behavior.nextExponential() * behavior.nextInt(1, 10) * 2;
        addUsedTime(time);
        System.out.printf(toString() + "休息了%.1fs\n", time);
    }

    @Override
    public void behaveRandomly(List<Food> allFoods) {
        switch (behavior.nextInt(5)) {
            case 0:
                if (!foods.isEmpty()) {
                    eat();
                }
                finishEating();
                break;
            case 1:
                drink();
                break;
            case 2:
                rest();
                break;
            case 3:
                chooseFoods(allFoods);
            default:
                if (foods.isEmpty())
                    chooseFoods(allFoods);
                eat();
        }
    }
}
