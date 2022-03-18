package animal;

import java.util.*;

import food.*;

public abstract class Animal {
    private boolean isFull;
    double nextEatQuantity;
    private double eatenQuantity;
    private double usedTime;
    List<Food> foods;

    public Animal() {
        isFull = false;
        eatenQuantity = 0;
        usedTime = 0;
        nextEatQuantity = 0; // 防止没初始化，实际上默认初始化成0，这里只是显式初始化
        foods = new ArrayList<Food>();
        try {
            refreshState();
        } catch (Exception e) {
            ;
        }
    }

    public final boolean isFull() {
        return isFull;
    }

    protected void setFull(boolean full) {
        isFull = full;
        if (full) {
            System.out.println(toString() + "饱了");
        } else {
            System.out.println(toString() + "饿了");
        }
    }

    /**
     * Get the expected quantity to eat next time.
     * 
     * @return expected quantity to eat, 0 if the animal is full.
     */
    public final double getNextEatQuantity() {
        return isFull ? 0 : nextEatQuantity;
    }

    public final double getEatenQuantity() {
        return eatenQuantity;
    }

    public void addEatenQuantity(double quantity) {
        eatenQuantity += quantity;
    }

    public double getUsedTime() {
        return usedTime;
    }

    public void addUsedTime(double time) {
        usedTime += time;
    }

    public boolean acceptFood(Food food) {
        return !isFull() && food.isFree() && food.canEat();
    }

    protected List<Food> getFoods(List<Food> allFoods) {
        List<Food> foods = new ArrayList<Food>();
        for (Food food : allFoods) {
            if (acceptFood(food)) {
                foods.add(food);
            }
        }
        return foods;
    }

    protected boolean addFood(Food food) {
        if (!acceptFood(food)) {
            System.out.println(toString() + "不打算吃" + food);
            return false;
        }
        foods.add(food);
        food.setFree(false);
        System.out.println(toString() + "开始吃" + food);
        return true;
    }

    protected int addFoods(List<Food> Foods) {
        int ct = 0;
        for (Food food : Foods) {
            ct += addFood(food) ? 1 : 0;
        }
        return ct;
    }

    /**
     * Choose foods and prepare to eat, state their belonging.
     * 
     * @param allFoods
     * @param maxNum
     * @return number of the chosen foods
     */
    public abstract int chooseFoods(List<Food> allFoods, int maxNum);

    public int chooseFoods(List<Food> allFoods) {
        return addFoods(allFoods);
    }

    protected abstract Food foodToEat(List<Food> foods);

    public Food foodToEat() {
        return foodToEat(foods);
    }

    /**
     * Fresh the expected quantity to eat next time and the full state.
     * Notice that it won't immediately finish eating.
     */
    protected abstract void refreshState();

    /**
     * Get the time to eat a specified quantity of the fiven food.
     * 
     * @param food
     * @param quantity
     * @return expected time
     */
    protected abstract double getEatTime(Food food, double quantity);

    /**
     * Eat and refresh the state if successfully eaten
     * 
     * @param food
     * @param quantity The max quantity to eat
     * @return eaten or not
     */
    public final boolean eat(Food food, double quantity) {
        if (isFull() || quantity <= 0) {
            System.out.println(toString() + "已经很饱了，不想继续吃");
            return false;
        }
        if (food == null) {
            System.out.println(toString() + "有点饿，但不知道该吃什么");
            return false;
        }
        double eaten = food.decQuantity(quantity);
        if (eaten > 0) {
            double time = getEatTime(food, quantity);
            System.out.printf("%s吃了%s%s, 用时%.1fs\n",
                    toString(), food.toQuantityString(eaten), food.toRawString(), time);
            addEatenQuantity(eaten);
            addUsedTime(time);
            if (!food.canEat()) {
                finishEating(food);
            }
            refreshState();
        } else {
            System.out.printf("%s想吃%s, 但已经被吃光了\n", toString(), food.toRawString());
            finishEating(food);
        }
        return eaten > 0;
    }

    public boolean eat(double quantity) {
        return eat(foodToEat(), quantity);
    }

    public boolean eat() {
        return eat(getNextEatQuantity());
    }

    protected Food finishEating(int index) {
        Food food = foods.remove(index);
        food.setFree(true);
        if (food.canEat()) {
            System.out.println(toString() + "吃完" + food.toRawString() + "还剩" + food.toQuantityString());
        } else {
            System.out.println(toString() + "吃掉了" + food.toRawString());
        }
        return food;
    }

    /**
     * Finish eating a food and set it free if it belongs to this animal.
     * 
     * @param food
     * @return whether it belongs to this animal
     */
    public boolean finishEating(Food food) {
        int index = foods.indexOf(food);
        if (index != -1) {
            finishEating(index);
            return true;
        }
        return false;
    }

    /**
     * Finish eating all the foods and set them free.
     * 
     * @return the number of foods
     */
    public int finishEating() {
        int ct = foods.size();
        while (!foods.isEmpty()) {
            finishEating(0);
        }
        if (ct == 0) {
            System.out.println(toString() + "决定暂时先不吃");
        } else {
            System.out.println(toString() + "吃了" + ct + "样食物后暂时休息");
        }
        return ct;
    }
}
