
import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.bson.Document;

import com.mongodb.client.model.Filters;

import databox.service.DataBoxService;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.trees.J48;
import weka.clusterers.*;
import weka.core.*;
import weka.core.converters.CSVLoader;
import weka.datagenerators.clusterers.BIRCHCluster;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

public class Clustering {

	public static void main(String[] args) throws Exception {
		DataBoxService client = new DataBoxService();
		List<String> fields = new ArrayList<String>();
		fields.add("TSH");
		fields.add("FT3");
		fields.add("FT4");
		List<Document> docs = client.projections("diagnosis", fields, Filters.and(Filters.exists("TSH"),Filters.exists("FT3"),Filters.exists("FT4")));
		
		int size = 2000;
		
		Attribute TSH = new Attribute("TSH");
		Attribute FT3 = new Attribute("FT3");
		Attribute FT4 = new Attribute("FT4");
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(TSH);
		attributes.add(FT3);
		attributes.add(FT4);
		Instances dataset = new Instances("dataset",attributes,size);
		
		
		
		for (int i= 0;i<size;i++){
			Document doc = docs.get(i);
			double[] attValues = new double[dataset.numAttributes()] ;
			
			try{
			attValues[0]=doc.getDouble("TSH");
			attValues[1]=doc.getDouble("FT3");
			attValues[2]=doc.getDouble("FT4");
			}catch(ClassCastException e){
				continue;
			}
			Instance inst = new DenseInstance(1.0,attValues);
			dataset.add(inst);
		}
		
//		CSVLoader loader = new CSVLoader();
//		loader.setSource(new File("C:/Users/youbei/Desktop/ruijin_data/testc.csv"));
//		Instances dataset = loader.getDataSet();
		
//		Instance data1 = dataset.get(0);
		
		EM clustererEM = new EM();
		clustererEM.buildClusterer(dataset);
		//int EMk = clustererEM.clusterInstance(data1);
		int allEM = clustererEM.numberOfClusters();
		// double[] cluser1 = clusterer.distributionForInstance(data1);
		//System.out.print("EMK:"+EMk+" ");
		//String describing = clustererEM.globalInfo();
		
		System.out.println("聚类结果："+allEM+"类");
		//System.out.println(""+describing+"");
		double[] we = clustererEM.clusterPriors();
		for (int i = 0; i <we.length;i++){
			System.out.println("第"+ i + "类比例"+we[i]);
		}
		
		
//		int MaximumNumberOfClusters = clustererEM.getMaximumNumberOfClusters();
//		int MaxIterations = clustererEM.getMaxIterations();
//		int NumClusters = clustererEM.getNumClusters();
//		double sta = clustererEM.getMinStdDev();
//		
		
		
		
	//	System.out.println("MaximumNumberOfClusters" + MaximumNumberOfClusters +"   MaxIterations"+ MaxIterations +"    NumClusters"+NumClusters);
	//	System.out.println("标准偏差："+ sta);
		
		
		
		
		
		
		Instance searchInst = preprocess("unknowpatient",12.2,4.2,1.2);
		int predictCategory = clustererEM.clusterInstance(searchInst);
//		double[] possibility = clustererEM.distributionForInstance(searchInst) ;
//		for (int i=0;i<possibility.length;i++){	
//		 String d = String.format("%.4f", possibility[i]*100 );
//		 System.out.println("查询在第" +i+"类集群的可能性："+ d +"%");
//		}
		
		
		System.out.println("查询病人(TSH:12.2,FT3:4.2,FT4:1.2)所属类别：" + predictCategory + "类" );
		
	}
	
	public static Instance  preprocess(String description,double TSH,double FT3,double FT4){
		double[] searchDatas = new double [3];
		searchDatas[0] = TSH;
		searchDatas[0] = FT3;
		searchDatas[0] = FT4;
		
		Instance searchInst = new DenseInstance(1.0,searchDatas);
		return searchInst;
	}
	

}
