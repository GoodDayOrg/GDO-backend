package org.example.utils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Utils {

    public static <T, G> void compareLists(List<T> list1, List<G> list2) {
        if (list1.size() != list2.size()) {
            fail();
        }

        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }

    }

}
