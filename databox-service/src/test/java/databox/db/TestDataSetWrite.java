package databox.db;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import databox.db.DataSet;
import databox.db.DataSetWriter;
import databox.db.Field;
import databox.db.Table;
import org.junit.Test;

public class TestDataSetWrite {

	@Test
	public void test() throws UnsupportedEncodingException, FileNotFoundException {
		DataSetWriter sample = new DataSetWriter();
		sample.outPut("C:/Users/youbei/Desktop/samplwrite.json");
		List<Table> tables = new ArrayList<Table>();
		DataSet dataset1 = new DataSet("甲状腺病历数据集", "甲状腺病历");
		List<String> pks1 = new ArrayList<String>();
		pks1.add("medical_record_id");
		
		List<String> fks1 = new ArrayList<String>();
		fks1.add("doctor_id");
		fks1.add("patient_id");
		
		List<Field> fields1 = new ArrayList<Field>();
		fields1.add(new Field("medical_record_id", "病历号", "varchar"));
		fields1.add(new Field("doctor_id", "就诊医生工号", "varchar"));
		fields1.add(new Field("patient_id", "就诊病人病号", "varchar"));
		fields1.add(new Field("symptom", "病情描述", "varchar"));
		fields1.add(new Field("diagnose", "诊断结论", "varchar"));
		
		dataset1.addTable(new Table("MedicalRecord", "病例", "病历信息", pks1, fks1, fields1));
		DataSet dataset2 = new DataSet("高血压病历数据集", "甲状腺病历");
		List<DataSet> datasets1 = new ArrayList<DataSet>();
		datasets1.add(dataset1);
		datasets1.add(dataset2);
		try {
			sample.writeDataSets(datasets1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}