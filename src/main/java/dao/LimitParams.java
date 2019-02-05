package dao;

public class LimitParams {

    private int offset;

    private int count;

    public LimitParams(int offset, int count) {
        this.offset = offset;
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
