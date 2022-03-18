package animal;

import java.util.*;

import food.*;

public class Snake extends RandomBehavedAnimal {
    public Snake() {
        super();
        nextEatQuantity = 1.5;
    }

    // Use python as prototype
    @Override
    public String toString() {
        return "蛇";
    }

    @Override
    public boolean acceptFood(Food food) {
        return super.acceptFood(food) && food.getQuantity() <= nextEatQuantity;
    }

    @Override
    protected void refreshState() {
        // Use swallow instead of bite, thus next quantity is the max size
        nextEatQuantity = Double.max(nextEatQuantity * .8, 1.5 - getEatenQuantity());
        if (getNextEatQuantity() <= .5 || getEatenQuantity() >= 4 + nextEatQuantity) {
            setFull(true);
        }
    }

    @Override
    protected double getEatTime(Food food, double quantity) {
        // swallow speed depends on eaten quantity
        return behavior.nextExponential() * Math.log1p(Double.max(
                getEatenQuantity(), quantity)) * 15;
    }

    private final void gatherFoods(List<Food> allFoods) {
        addUsedTime(behavior.nextExponential() * behavior.nextInt(1, 3));
        chooseFoods(allFoods, behavior.nextInt(1, 3));
    }

    private final void digest() {
        nextEatQuantity *= 1.25;
        if (getEatenQuantity() == 0) {
            nextEatQuantity *= 1.25;
            System.out.println(toString() + "非常的饿");
        } else {
            addUsedTime(behavior.nextExponential() * behavior.nextInt(1, 2) * 5);
            System.out.println(toString() + "正在消化食物");
        }
    }

    @Override
    public void behaveRandomly(List<Food> allFoods) {
        switch (behavior.nextInt(5)) {
            case 0:
                chooseFoods(allFoods);
                break;
            case 1:
                gatherFoods(allFoods);
                break;
            case 2:
                digest();
                break;
            case 3:
                if (foods.isEmpty())
                    chooseFoods(allFoods, 1);
            default:
                eat();
        }
    }
}
