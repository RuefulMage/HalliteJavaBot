package hlt;

public class Zone {
    public int leftX;
    public int leftY;
    public int rightX;
    public int rightY;
    public int area;

    public Zone(int leftX, int leftY, int rightX, int rightY){
        this.leftX = leftX;
        this.leftY = leftY;
        this.rightX = rightX;
        this.rightY = rightY;
        this.area = Math.abs(rightX - leftX)*Math.abs(rightY - leftY);
    }

}
