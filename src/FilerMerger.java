import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilerMerger {
	public static void main(String[] args) throws IOException {
		
		final String category = "prices";
		
		String file1 = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\Alibaba_" + category + ".csv";
		String file2 = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\Amazon_" + category + ".csv";
		String file3 = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\ebay_" + category + ".csv";
		String file4 = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\walmart_" + category + ".csv";
		
		String mergedFile = "C:\\Users\\Administrator\\Documents\\TIM245\\project\\data\\all_" + category + ".csv";
		
	    List<Path> paths = Arrays.asList(Paths.get(file1), Paths.get(file2), Paths.get(file3), Paths.get(file4));
	    List<String> mergedLines = getMergedLines(paths);
	    Path target = Paths.get(mergedFile);
	    Files.write(target, mergedLines, Charset.forName("UTF-8"));
	}

	private static List<String> getMergedLines(List<Path> paths) throws IOException {
	    List<String> mergedLines = new ArrayList<> ();
	    for (Path p : paths){
	        List<String> lines = Files.readAllLines(p, Charset.forName("UTF-8"));
	        if (!lines.isEmpty()) {
	            if (mergedLines.isEmpty()) {
	                mergedLines.add(lines.get(0)); //add header only once
	            }
	            mergedLines.addAll(lines.subList(1, lines.size()));
	        }
	    }
	    return mergedLines;
	}
}
