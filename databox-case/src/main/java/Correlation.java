

import java.util.*;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.bson.Document;

import com.mongodb.client.model.Filters;

import databox.service.DataBoxService;

public class Correlation {

	public static void main(String[] args) {
		DataBoxService client= new DataBoxService();
		List<String> fields = new ArrayList<String>();
		fields.add("TSH");
		fields.add("FT3");
		fields.add("FT4");
		List<Document> docs = client.projections("diagnosis",fields, Filters.and(Filters.exists("TSH"),Filters.exists("FT3"),Filters.exists("FT4")));
		
		int size =docs.size();
		double[] TSH = new double[size];
		double[] FT3 = new double[size];
		double[] FT4 = new double[size];
		int j = 0 ;
		for(int i = 0;i< size;i++){
			double d1,d2,d3;
			Document doc = docs.get(i);
			try{
				d1 = doc.getDouble("FT4");
				d2 = doc.getDouble("FT3");
				d3 = doc.getDouble("TSH");
			}catch(ClassCastException e){
				continue;
			}
			TSH[j] = d3;
			FT4[j] = d1;
			FT3[j] = d2;
			j++;
		}
		
		
		PearsonsCorrelation correlation = new PearsonsCorrelation();
		double TSH_FT3 = correlation.correlation(TSH, FT3);
		double TSH_FT4 = correlation.correlation(TSH, FT4);
		System.out.print(TSH_FT4);
		System.out.print(TSH_FT3);
	}

}

