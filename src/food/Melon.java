package food;

public class Melon extends Food implements Cutable {
    private static int total = 0;
    private int id;
    private boolean isSlice;

    public Melon(double quantity) {
        super(quantity);
        id = ++total;
        isSlice = false;
    }

    private Melon(double quantity, int id, boolean isSlice) {
        super(quantity);
        this.id = id;
        this.isSlice = isSlice;
    }

    public final int getId() {
        return id;
    }

    public final boolean isSlice() {
        return isSlice;
    }

    public static String getQuantityString(double quantity) {
        return Food.getQuantityString(quantity);
    }

    @Override
    public String toRawString() {
        if (isSlice) {
            return "西瓜块" + "(" + id + ")";
        }
        return id + "号" + "西瓜";
    }

    public Melon cut(double quantity) {
        if (quantity >= getQuantity() || quantity <= 0) {
            return null;
        }
        double new_q = super.decQuantity(quantity);
        Melon slice = new Melon(new_q, id, true);
        System.out.printf("%s被切掉了%s, 还剩%s\n",
                toRawString(), slice.toQuantityString(), toQuantityString());
        isSlice = true;
        return slice;
    }
}
