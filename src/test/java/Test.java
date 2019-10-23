import com.eatrho.fundtool.service.FundService;

import java.util.Map;

public class Test {

    public static void main(String[] args) {
        {
            String code = "004746";
            Map<String, String> map = FundService.getjjccByCode(code);
            System.out.println(map);
            FundService.calculateTrendByjjcc(map);
        }
    }

}
