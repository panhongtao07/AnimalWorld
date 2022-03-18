package animal;

import java.util.*;

import food.*;

public class Human extends RandomBehavedAnimal {
    private String name;

    public Human(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected void refreshState() {
        if (getEatenQuantity() >= 1.5 && behavior.nextGaussian(1, .5) < getEatenQuantity()) {
            setFull(true);
            nextEatQuantity = 0;
        } else {
            nextEatQuantity = behavior.nextExponential() * .5;
        }
    }

    @Override
    protected double getEatTime(Food food, double quantity) {
        return behavior.nextExponential() * quantity / .5 * 10;
    }

    private final void speak() {
        System.out.println(toString() + "正在自言自语");
    }

    private final void playCellphone() {
        addUsedTime(behavior.nextExponential() * behavior.nextInt(1, 6) * 3);
        System.out.println(toString() + "玩了一会手机");
    }

    @Override
    public void behaveRandomly(List<Food> allFoods) {
        switch (behavior.nextInt(8)) {
            case 0:
                playCellphone();
            case 1:
                chooseFoods(allFoods);
                break;
            case 2:
                playCellphone();
            case 3:
                if (behavior.nextGaussian() > 1) {
                    setFull(false);
                }
                while (!foods.isEmpty() && !isFull() && foods.get(0).canEat() && eat());
            case 4:
                finishEating();
                if (!isFull() && getUsedTime() != 0 && behavior.nextGaussian() > 1.5) {
                    setFull(true);
                }
                break;
            case 5:
                speak();
                break;
            default:
                if (foods.isEmpty())
                    chooseFoods(allFoods);
                eat();
        }
    }
}
