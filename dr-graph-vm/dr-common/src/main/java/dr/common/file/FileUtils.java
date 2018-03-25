package dr.common.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	private static Class getClass(Class clazz) {
		return (clazz != null ? clazz : FileUtils.class);
	}

	public static String fromClassPathFile(String file, Class clazz) throws IOException {
		InputStream inputStream = getClass(clazz).getResourceAsStream(file);
		return inputStream == null ? null : fromStream(inputStream);
	}

	// java 7
	public static String fromStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	// java 8
	public static String fromFile(String file, Class clazz) throws IOException, URISyntaxException {
		
		logger.info("path ... "+Paths.get(file).toAbsolutePath());
		
		Path path = Paths.get(file);

		StringBuilder data = new StringBuilder();
		Stream<String> lines = Files.lines(path);
		lines.forEach(line -> data.append(line).append("\n"));
		lines.close();
		return data.toString();

	}

}
