package animal;

import java.util.*;

import food.*;

public class Bird extends RandomBehavedAnimal {
    private static int total = 0;
    private int id;

    public Bird() {
        super();
        id = ++total;
    }

    @Override
    public String toString() {
        return id + "号" + "小鸟";
    }

    @Override
    public boolean acceptFood(Food food) {
        // can only eat one food at a time
        return foods.isEmpty() && super.acceptFood(food);
    }

    @Override
    public int chooseFoods(List<Food> allFoods, int maxNum) {
        return super.chooseFoods(allFoods, 1);
    }

    @Override
    protected void refreshState() {
        // bird eats little
        if (getEatenQuantity() >= .4) {
            setFull(true);
            nextEatQuantity = 0;
        } else {
            nextEatQuantity = behavior.nextExponential() * (.5 - getEatenQuantity()) * .2;
        }
    }

    @Override
    protected double getEatTime(Food food, double quantity) {
        // bird eats slowly
        return quantity / .2 * 10;
    }

    private final void fly() {
        if (isFull()) {
            System.out.println(toString() + "想飞一会，但是太饱了");
        } else {
            double distance = 30 * behavior.nextExponential() * (.5 - getEatenQuantity());
            System.out.printf("%s飞了%.1f米, 食欲提升了\n", toString(), distance);
            nextEatQuantity *= 1.5;
            addUsedTime(behavior.nextExponential() * distance / 10);
        }
    }

    @Override
    public void behaveRandomly(List<Food> allFoods) {
        if (isFull() && foods.isEmpty()) {
            return;
        }
        switch (behavior.nextInt(8)) {
            case 0:
                if (chooseFoods(allFoods) > 0)
                    break;
            case 4:
                finishEating();
                break;
            case 5:
                fly();
                if (behavior.nextGaussian() > 1) {
                    break;
                }
            case 1:
                chooseFoods(allFoods);
            default:
                eat();
        }
    }
}
