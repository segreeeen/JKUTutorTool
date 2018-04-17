package at.hsol.ttool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MatNrReader {

	private List<String> matNrList;

	public List<String> getNamesList() {
		return matNrList;
	}

	public MatNrReader(String listFile) {
		try {
			List<String> list = Files.lines(Paths.get(listFile)).collect(Collectors.toList());
			this.matNrList = list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.matNrList = null;
		}
	}
}
