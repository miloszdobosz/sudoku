package org.example;

import java.util.Arrays;

public class GroupGuardOld {
    private final Boolean[][] valueInGroup;

    public GroupGuardOld() {
        this.valueInGroup = new Boolean[SudokuMapOld.SIZE][SudokuMapOld.SIZE];
        for (Boolean[] group : this.valueInGroup) {
            Arrays.fill(group, false);
        }
    }

    public boolean contains(int group, int value) {
        if (value == 0) {
            return false;
        }
        return this.valueInGroup[group][value - 1];
    }

    public void add(int group, int value) {
        if (value != 0) {
            this.valueInGroup[group][value - 1] = true;
        }
    }

    public void remove(int group, int value) {
        if (value != 0) {
            this.valueInGroup[group][value - 1] = false;
        }
    }
}
