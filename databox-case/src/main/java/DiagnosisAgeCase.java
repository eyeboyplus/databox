

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.model.Filters;

import databox.service.DataBoxService;

public class DiagnosisAgeCase {

	public static void main(String[] args) {
		DataBoxService service = new DataBoxService();
		List<String> fields = new ArrayList<String>();
		fields.add("age");
		fields.add("diagnosis");
		List<Document> docs = service.projections("test", fields, Filters.eq("diagnosis", "��״�ٽ��"));
		long total = docs.size();
		long count1 = 0, count2 = 0, count3 = 0;
		for(Document doc : docs) {
			int age = doc.getInteger("age");
			if(age >= 20 && age < 30)
				count1 ++;
			else if(age >= 30 && age < 40)
				count2 ++;
			else if(age >= 40 && age < 50)
				count3 ++;
		}
		service.putResult("20 ~ 30", ((double)count1 / (double)total)*100.0 + " %");
		service.putResult("30 ~ 40", ((double)count2 / (double)total)*100.0 + " %");
		service.putResult("40 ~ 50", ((double)count3 / (double)total)*100.0 + " %");
	}

}
