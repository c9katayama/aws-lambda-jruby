import java.util.Map;

import org.jruby.embed.IsolatedScriptingContainer;
import org.jruby.embed.PathType;

import com.amazonaws.services.lambda.runtime.Context;

public class AWSLambdaJRuby {
    
	private static final String mainRubyFile = "main.rb";
	private static final String gemsRubyFile = "gems.rb";


	@SuppressWarnings("rawtypes")
	public String handler(Map data, Context context) throws Exception {

	    IsolatedScriptingContainer container = new IsolatedScriptingContainer();
	
	    context.getLogger().log(data.toString());
	    
	    container.put("$lambda_arg", data);

	    String result = "";
	    result += "yaml: " + container.runScriptlet(PathType.CLASSPATH, mainRubyFile);
	    result += " gems: " + container.runScriptlet(PathType.CLASSPATH, gemsRubyFile);
	    
		
	    return result;
	}

}
