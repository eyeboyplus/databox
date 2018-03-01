

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.bson.Document;

import databox.service.DataBoxService;

public class CorrelationCase {

	public static void main(String[] args) {
		DataBoxService service = new DataBoxService();
		List<String> fields = new ArrayList<String>();
		fields.add("age");
		fields.add("hgb");
		List<Document> docs = service.projections("test", fields);

		PearsonsCorrelation pc = new PearsonsCorrelation();
		int size = docs.size();
		double[] ages = new double[size];
		double[] hgbs = new double[size];
		for (int i = 0; i < size; ++i) {
			Document doc = docs.get(i);
			int age = doc.getInteger("age");
			double hgb = doc.getDouble("hgb");
			ages[i] = age;
			hgbs[i] = hgb;
		}

		System.out.println(pc.correlation(ages, hgbs));
	}
}
