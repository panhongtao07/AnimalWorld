import java.util.*;

import food.*;
import animal.*;
import util.*;

/**
 * 
 * @Description
 * @author PHT
 * @date 2022-03-18 13:00
 */
public class AnimalWorld {
    static final int cutCount = 4, oriCount = 3;
    static final int total = cutCount + oriCount;
    static final int maxRound = 20;
    static final double gameTime = 100;
    static final boolean waitFullAnimals = true, enableFullAnimalActions = false;
    static Random seed = new Random();
    static List<Food> foods = new ArrayList<Food>();
    static List<RandomBehavedAnimal> animals = new ArrayList<RandomBehavedAnimal>();

    /**
     * 举行吃瓜比赛
     * 
     * <ul>
     * <li>通过类静态常量控制比赛设置（西瓜数量、切的个数、比赛时长和轮数等</li>
     * <li>修改gatherAnimal以调整比赛选手</li>
     * <li>可以使用Out.blockOutput()和Out.unblockOutput()决定呈现内容</li>
     * </ul>
     */
    public static void main(String[] args) throws Exception {
        // 规则
        System.out.println("动物事件将会举办吃西瓜大赛，比赛规则为：");
        System.out.printf("\t共有%d个西瓜，其中%d个切成小块，剩下%d个保留原样；\n",
                          total, cutCount, oriCount);
        System.out.println("\t参赛选手要在比赛时间内吃尽可能多的西瓜；");
        System.out.println("\t吃的最多的选手和吃的最快的选手即为胜者。");
        System.out.println();
        // 准备
        gatherAnimals();
        System.out.println("参赛选手有：");
        System.out.println(animals);
        System.out.println();
        prepareMelons();
        System.out.println();
        System.out.println("现在有" + foods.size() + "份西瓜：");
        System.out.println(foods);
        System.out.println();
        // 比赛
        // Out.blockOutput();
        startEating(maxRound, gameTime);
        System.out.println();
        Out.unblockOutput();
        // 总结
        finishEating();
    }

    public static void gatherAnimals() {
        animals.clear();
        animals.add(new Human("学生S"));
        animals.add(new Bird());
        animals.add(new Bird());
        animals.add(new Elephant());
        animals.add(new Human("老师T"));
        animals.add(new Snake());
    }

    public static void prepareMelons() {
        addMelons(cutCount);
        cutMelons();
        cutMelons();
        addMelons(oriCount);
        reorderMelons();
    }

    public static void addMelon() {
        double quantity = seed.nextExponential() * 4;
        foods.add(new Melon(quantity));
    }

    public static void addMelons(int num) {
        while (num-- > 0) {
            addMelon();
        }
    }

    static <T extends Cutable> List<T> getCutables(Class<T> tClass) {
        List<T> cutableFoods = new ArrayList<T>();
        for (Food food : foods) {
            if (tClass.isInstance(food)) {
                cutableFoods.add(tClass.cast(food));
            }
        }
        return cutableFoods;
    }

    private static List<Melon> getMelons() {
        return getCutables(Melon.class);
    }

    public static void cutMelons() {
        double avg = 0;
        List<Melon> melons = getMelons();
        if (melons.isEmpty()) {
            return;
        }
        for (Melon melon : melons) {
            avg += melon.getQuantity();
        }
        avg /= melons.size();
        // 减小平均值，较大的都可以切
        avg *= .85;
        for (Melon melon : melons) {
            while (melon.getQuantity() > avg) {
                // 尽量保证切的两半均匀又不会太小
                double mean = Math.min(avg * .85, melon.getQuantity() / 2);
                // 每刀是随机的
                double expVal = seed.nextExponential();
                double stddev = mean * expVal / 4.0;
                // nextExponential有bug，有时会导致stddev为负，但这里不用考虑
                double quantity = seed.nextGaussian(mean, stddev);
                // 高斯分布可能为负
                if (quantity <= 0) {
                    continue;
                }
                Melon newMelon = melon.cut(quantity);
                if (newMelon == null) {
                    continue;
                }
                foods.add(newMelon);
            }
        }
    }

    static void startEating(int rounds, double gameTime) {
        List<RandomBehavedAnimal> fullAnimals = new ArrayList<RandomBehavedAnimal>();
        List<RandomBehavedAnimal> hungryAnimals = new ArrayList<RandomBehavedAnimal>();
        for (int i = 0; i < rounds; i++) {
            fullAnimals.clear();
            hungryAnimals.clear();
            // System.out.printf("第%d轮：\n", i + 1);
            for (RandomBehavedAnimal animal : animals) {
                if (animal.isFull()) {
                    fullAnimals.add(animal);
                } else {
                    hungryAnimals.add(animal);
                }
            }
            if (!fullAnimals.isEmpty()) {
                System.out.println("已吃饱的动物有：" + fullAnimals);
            }
            // 动物按顺序做自己的事
            boolean end = true, noFood = true;
            double usedTime = 0;
            if (hungryAnimals.isEmpty() || enableFullAnimalActions) {
                for (RandomBehavedAnimal animal : fullAnimals) {
                    animal.behaveRandomly(foods);
                    usedTime = Double.max(usedTime, animal.getUsedTime());
                    if (waitFullAnimals && animal.getUsedTime() < gameTime) {
                        end = false;
                    }
                }
            } else {
                for (RandomBehavedAnimal animal : hungryAnimals) {
                    if (animal.getUsedTime() < gameTime) {
                        animal.behaveRandomly(foods);
                        end = false;
                    }
                    usedTime = Double.max(usedTime, animal.getUsedTime());
                }
            }
            for (Food food : foods) {
                if (food.canEat()) {
                    noFood = false;
                }
            }
            System.out.println();
            if (end || noFood || i + 1 == maxRound) {
                System.out.printf("比赛在第%d轮结束，共用时%.1fs\n", i + 1, usedTime);
                break;
            }
        }
    }

    static void finishEating() {
        ranking();
        printFoods();
        System.out.println();
        printAnimals();
        System.out.println();
        printGrade();
        System.out.println();
        printWinner();
    }

    public static void reorderMelons() {
        List<Melon> melons = getMelons();
        melons.sort(new MelonComparator());
        foods.removeAll(melons);
        foods.addAll(melons);
    }

    public static void ranking() {
        animals.sort(new GradeComparator());
    }

    public static void printFoods() {
        List<Food> clearedFoods = new ArrayList<Food>();
        List<Food> remainFoods = new ArrayList<Food>();
        for (Food food : foods) {
            if (food.canEat()) {
                remainFoods.add(food);
            } else {
                clearedFoods.add(food);
            }
        }
        System.out.printf("吃完了%d份西瓜, 还剩%d份：\n", clearedFoods.size(), remainFoods.size());
        System.out.println(remainFoods);
    }

    public static void printAnimals() {
        List<Animal> fullAnimals = new ArrayList<Animal>();
        List<Animal> hungryAnimals = new ArrayList<Animal>();
        for (Animal animal : animals) {
            if (animal.isFull()) {
                fullAnimals.add(animal);
            } else {
                hungryAnimals.add(animal);
            }
        }
        if (!fullAnimals.isEmpty()) {
            if (hungryAnimals.isEmpty()) {
                System.out.println("所有动物都吃饱了！");
            } else {
                System.out.println("吃饱的动物有：" + fullAnimals);
            }
        }
        if (!hungryAnimals.isEmpty()) {
            if (fullAnimals.isEmpty()) {
                System.out.println("所有动物都没吃饱！");
            } else {
                System.out.println("没吃饱的动物有：" + hungryAnimals);
            }
        }
    }

    public static void printGrade() {
        int l1 = 8, l2 = 10, l3 = 8;
        System.out.println("-".repeat(l1 + l2 + l3 + 4));
        System.out.printf("|%s|%s|%s|\n",
                ChineseDisplay.adjust("动物", l1),
                ChineseDisplay.adjust("食量", l2),
                ChineseDisplay.adjust("总时间", l3));
        System.out.println("|" + "-".repeat(l1 + l2 + l3 + 2) + "|");
        for (Animal animal : animals) {
            System.out.printf("|%s|%s|%s|\n",
                    ChineseDisplay.adjust(animal, l1),
                    ChineseDisplay.adjust(Melon.getQuantityString(animal.getEatenQuantity()), l2),
                    Display.center(Display.formatNumber(animal.getUsedTime(), 1) + "s", l3));
        }
        System.out.println("-".repeat(l1 + l2 + l3 + 4));
    }

    public static void printWinner() {
        int fl = 9, sl = 9, tl = 9;
        Animal first = animals.get(0), second = animals.get(1), third = animals.get(2);
        System.out.println("吃的最多的动物是：");
        System.out.println();
        System.out.printf("          %s          \n", ChineseDisplay.adjust(first, fl + 2));
        System.out.println("          ╭---------╮          ");
        System.out.printf("%s|         |          \n", ChineseDisplay.adjust(second, sl + 1));
        System.out.printf("╭---------|    1    |%s \n", ChineseDisplay.adjust(third, tl));
        System.out.println("|    2    |         |---------╮");
        System.out.println("|         |         |    3    |");
        System.out.println("|         |         |         |");
        System.out.println("-".repeat(fl + sl + tl + 4));
        System.out.printf("%s|%s|%s\n",
                ChineseDisplay.adjust(Melon.getQuantityString(second.getEatenQuantity()), sl + 1),
                ChineseDisplay.adjust(Melon.getQuantityString(first.getEatenQuantity()), fl),
                ChineseDisplay.adjust(Melon.getQuantityString(third.getEatenQuantity()), tl + 1));
        System.out.println();
        double speed = -1;
        Animal fastestAnimal = animals.get(0);
        for (RandomBehavedAnimal animal : animals) {
            if (animal.getUsedTime() != 0 && animal.getEatenQuantity() / animal.getUsedTime() > speed) {
                fastestAnimal = animal;
                speed = animal.getEatenQuantity() / animal.getUsedTime();
            }
        }
        System.out.println("吃的最快的动物是：");
        System.out.printf("\t%s：%.2f斤/分钟\n", fastestAnimal, speed * 60);
    }
}

class GradeComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal a1, Animal a2) {
        if (a1.getEatenQuantity() != a2.getEatenQuantity()) {
            return a1.getEatenQuantity() > a2.getEatenQuantity() ? -1 : 1;
        }
        if (a1.getUsedTime() != a2.getUsedTime()) {
            return a1.getUsedTime() < a2.getUsedTime() ? -1 : 1;
        }
        return 0;
    }
}
