package vn.hoidanit.todo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiscountCaculatorTest {

    @Test
    public void testNoDiscount() {
        DiscountCaculator discountCaculator = new DiscountCaculator();
        double discount = discountCaculator.caculateDiscount(10);
        Assertions.assertEquals(0, discount);
    }

    @Test
    public void test10Discount() {
        DiscountCaculator discountCaculator = new DiscountCaculator();
        double discount = discountCaculator.caculateDiscount(200);
        Assertions.assertEquals(20, discount);
    }

    @Test
    public void test20Discount() {
        DiscountCaculator discountCaculator = new DiscountCaculator();
        double discount = discountCaculator.caculateDiscount(600);
        Assertions.assertEquals(120, discount);
    }
}
