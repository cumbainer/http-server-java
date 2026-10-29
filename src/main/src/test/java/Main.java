import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Main {

    @Test
    public void mainStarts() {
        var helper = new TestHelper();
        var res = 3;
        Assertions.assertEquals(res, helper.sum(1, 2));
    }
}
