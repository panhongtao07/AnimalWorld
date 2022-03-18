package food;

import java.util.*;

public class MelonComparator implements Comparator<Melon> {
    @Override
    public int compare(Melon m1, Melon m2) {
        if (m1.isSlice() == m2.isSlice()) {
            return normalCompare(m1, m2);
        }
        return m1.isSlice() ? -1 : 1;
    }

    private int compareId(Melon m1, Melon m2) {
        if (m1.getId() != m2.getId()) {
            return m1.getId() < m2.getId() ? -1 : 1;
        }
        return 0;
    }

    private int compareQuantity(Melon m1, Melon m2) {
        if (m1.getQuantity() != m2.getQuantity()) {
            return m1.getQuantity() > m2.getQuantity() ? -1 : 1;
        }
        return 0;
    }

    private int normalCompare(Melon m1, Melon m2) {
        int ret = compareId(m1, m2);
        if (ret == 0) {
            ret = compareQuantity(m1, m2);
        }
        return ret;
    }
}
