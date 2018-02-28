package databox.sample.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;

import org.bson.conversions.Bson;
import org.nustaq.serialization.FSTConfiguration;

public class BsonHelper {
	public static Bson getBsonFromStream(ServletInputStream in) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
	    int n = 0;
	    while (-1!=(n=in.read(buffer))) {
	        output.write(buffer, 0, n);
	    }
	    byte[] bytes=output.toByteArray();
	    FSTConfiguration configuration=FSTConfiguration.createDefaultConfiguration();
	    Bson bson=(Bson) configuration.asObject(bytes);
	    return bson;
	}
}
