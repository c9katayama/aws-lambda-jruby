import java.util.HashMap;
import java.util.Map;

public class AWSLambdaJRubyTestMain {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		Map data = new HashMap();
		data.put("hoge", "foo");
		String result = new AWSLambdaJRuby().handler(data, new MockContext());
		System.out.println(result);
	}

}
