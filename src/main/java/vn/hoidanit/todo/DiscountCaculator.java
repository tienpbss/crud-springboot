package vn.hoidanit.todo;

public class DiscountCaculator {
    public double caculateDiscount(double price) {
        if (price <= 100) return 0;
        else if (price <= 500) return price * 0.1;
        else return price * 0.2;
    }
}
