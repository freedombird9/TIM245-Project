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
		
		String file1 = "/Users/peijiang/tim245/amazon1.csv";
		String file2 = "/Users/peijiang/tim245/amazon2.csv";
		String file3 = "/Users/peijiang/tim245/walmart.csv";
		String file4 = "/Users/peijiang/tim245/alibaba.csv";
		String file5 = "/Users/peijiang/tim245/ebay.csv";
		
		String mergedFile = "/Users/peijiang/tim245/merged.csv";
		
	    List<Path> paths = Arrays.asList(Paths.get(file1), Paths.get(file2), Paths.get(file3), Paths.get(file4), Paths.get(file5));
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
