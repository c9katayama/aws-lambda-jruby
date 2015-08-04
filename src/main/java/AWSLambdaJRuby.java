import java.util.Map;

import org.jruby.Ruby;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

public class AWSLambdaJRuby {

	private static final String rubyFileName = "main.rb";

	static {
		Ruby.newInstance();
	}

	@SuppressWarnings("rawtypes")
	public String handler(Map data, Context context) {
		
		ScriptingContainer container = new ScriptingContainer();
		String arg = new Gson().toJson(data);
		context.getLogger().log(arg);
		
		//set argument of lambda function to ruby global variable as JSON format 
		container.put("$lambda_arg", arg);
		
		// upload zip is extracted to /var/task directory
		Object result = container.runScriptlet(PathType.ABSOLUTE, "/var/task/" + rubyFileName);
		
		return result == null ? null : result.toString();
	}

}