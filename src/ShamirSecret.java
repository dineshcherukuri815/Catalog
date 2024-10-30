package src;
import org.json.JSONObject;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ShamirSecret {

    public static void main(String[] args) throws Exception {
        
        String content1 = new String(Files.readAllBytes(Paths.get("src/testcase1.json")));
        JSONObject json1 = new JSONObject(content1);
        BigInteger secret1 = processTestCase(json1);

        
        String content2 = new String(Files.readAllBytes(Paths.get("src/testcase2.json")));
        JSONObject json2 = new JSONObject(content2);
        BigInteger secret2 = processTestCase(json2);

        
        System.out.println("The secret for the first test case is: " + secret1);
        System.out.println("The secret for the second test case is: " + secret2);
    }

    
    private static BigInteger processTestCase(JSONObject json) {
        
        JSONObject keys = json.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        
        Map<Integer, BigInteger> points = new HashMap<>();

        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;

            JSONObject point = json.getJSONObject(key);
            int x = Integer.parseInt(key); 
            int base = Integer.parseInt(point.getString("base")); 
            String encodedY = point.getString("value"); 

            
            BigInteger y = new BigInteger(encodedY, base);
            points.put(x, y);
        }

       
        return lagrangeInterpolation(points, k);
    }

    
    private static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger result = BigInteger.ZERO;

        
        for (Map.Entry<Integer, BigInteger> entry1 : points.entrySet()) {
            int xi = entry1.getKey();
            BigInteger yi = entry1.getValue();

            BigInteger term = yi;

           
            for (Map.Entry<Integer, BigInteger> entry2 : points.entrySet()) {
                int xj = entry2.getKey();
                if (xi != xj) {
                    term = term.multiply(BigInteger.valueOf(xj))
                               .divide(BigInteger.valueOf(xj - xi));
                }
            }

           
            result = result.add(term);
        }

        return result;
    }
}
