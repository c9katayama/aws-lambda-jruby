import java.net.URL;
import java.net.URLClassLoader;
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
	public String handler(Map data, Context context) throws Exception {
		
		ScriptingContainer container = new ScriptingContainer();
		String arg = new Gson().toJson(data);
		context.getLogger().log(arg);

		//add std lib path
		URL stdLibPath = getClass().getResource("/stdlib/").toURI().toURL();
    container.addLoadPath(new URLClassLoader(new URL[]{stdLibPath}));

		//set argument of lambda function to ruby global variable as JSON format
    container.put("$lambda_arg", arg);
    
		// uploaded zip is extracted to /var/task directory
    container.setCurrentDirectory("/var/task");

    // Setup Bulder configs
    container.runScriptlet("ENV['BUNDLE_GEMFILE'] = \"/var/task/Gemfile\"");
    container.runScriptlet("ENV['GEM_HOME'] = \"/var/task/vendor\"");
    container.runScriptlet("ENV['GEM_PATH'] = \"/var/task/vendor\"");

    // Setup load the gems
    container.runScriptlet("require 'rubygems'");
    container.runScriptlet("require 'bundler/setup'");
    container.runScriptlet("Bundler.require");

    // Run main.rb
    Object result = container.runScriptlet(PathType.CLASSPATH,rubyFileName);

		return result == null ? null : result.toString();
	}

}
