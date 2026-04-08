package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    private static final Set<Integer> fullDomain = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    public static final int ALL_VAL_SIZE = 81;

    public static Set<Integer> getFullDomain() {
        return new HashSet<>(fullDomain);
    }

    public static List<Integer> generateAllValues(){
        List<Integer> allValues = new ArrayList<>(81);
        for (int i = 0; i < 9; i++){
            allValues.addAll(getFullDomain());
        }
        return allValues;
    }
}
