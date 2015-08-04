import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class MockLambdaLogger implements LambdaLogger {

	@Override
	public void log(String string) {
		System.out.println(string);
	}

}
