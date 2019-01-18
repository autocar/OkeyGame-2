package com.digitoygames;

/**
 * @author sedos17
 *
 */

public class Stone {

    public Stone(Integer Number) {
        this.Number = Number;
    }
    private Integer Number ;
    private boolean Used;

    public Integer getNumber() {
        return Number;
    }

    public void setNumber(Integer number) {
        Number = number;
    }

    public boolean isUsed() {
        return Used;
    }

    public void setUsed(boolean used) {
        Used = used;
    }


    @Override
    public boolean equals(Object obj) {
        return Number.equals(((Stone)obj).Number);
    }
    @Override
    public int hashCode() {
        int hash = 13;
        hash = (31 * hash) + (null == Number ? 0 : Number.hashCode());
        return hash;
    }
}
