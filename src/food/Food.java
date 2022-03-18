package food;

import util.*;

public class Food {
    private double quantity;
    private boolean free;

    public Food(double quantity) {
        this.quantity = quantity;
        free = true;
    }

    public boolean isFree() {
        return free;
    }

    final public boolean canEat() {
        return quantity > 0;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public double getQuantity() {
        return quantity;
    }

    public double decQuantity(double quantity) {
        if (quantity < 0)
            return 0;
        double eat_q = this.quantity < quantity ? this.quantity : quantity;
        this.quantity -= eat_q;
        return eat_q;
    }

    public static String getQuantityString(double quantity) {
        return String.format("%s斤", Display.formatNumber(quantity));
    }

    public String toRawString() {
        return super.toString();
    }

    public String toQuantityString(double quantity) {
        return Food.getQuantityString(quantity);
    }

    public String toQuantityString() {
        return toQuantityString(getQuantity());
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", toRawString(), canEat() ? toQuantityString() : "已吃完");
    }
}
