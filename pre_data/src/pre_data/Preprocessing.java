package pre_data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;



public class Preprocessing {
	private List<String> query = new ArrayList<>();// �洢35�����͵ĸ��ض�Ӧ���ļ���,����дtest.json�ļ�
	private List<String> database_shell = new ArrayList<>();// �洢4�����ݿ�����ʱ��Ҫ��shell��������
	private Map<String, String> dataset_map = new HashMap<>();// �������������ݼ������ļ���ӳ��
	private Map<String, String> data_map = new HashMap<>();// �������������ݼ��ļ���ӳ��
	public String shell;// ����ִ�е�shell��������Ϣ
	private List<String> final_query = new ArrayList<>();//�洢��д�Ĳ�ѯ�ļ���
	public List<String> attribute_value = new ArrayList<>();//�洢���Լ���ֵ
	private List<String> att_query = new ArrayList<>();//�������йصĲ�ѯ�ļ���
	private Map<String,Integer> attribute_type=new HashMap<>();//�洢ÿ����������ֵ�ͣ�1�������ַ����ͣ�0��
	private String username="yangman";
	
	private String database_control="newdata_neo4j"; //��������
	public List<Integer> workloads_result = new ArrayList<>();
	private Set<String> titan_query=new HashSet<>();
	private int query_count=3;
	private long id_index=364085624;
	private long  id_no_index=65548469614l;
	private boolean id_flag=false;
	private int id_count=0;
	

	
	/**
	 * �漰���ԵĲ�ѯ�ļ��� Node-specific-property-search Edge-specific-property-search
	 * 
	 */

	/**
	 * ��ʼ������Ա��������
	 */
	public Preprocessing() {

		dataset_map.put("30.50.311814", "/pre_data/data/freebase.txt");
		dataset_map.put("343.112912", "/pre_data/data/freebase2.txt");
		dataset_map.put("620.18415815", "/pre_data/data/ldbc.txt"); 

		data_map.put("30.50.311814", "freebase_small.json");
		data_map.put("343.112912", "freebase_medium.json");
		data_map.put("620.18415815", "ldbc.json");

		att_query.add("node-specific-property-search.groovy");
		att_query.add("edge-specific-property-search.groovy");
		//att_query.add("update-node-property.groovy");
		//att_query.add("update-edge-property.groovy");
		//att_query.add("delete-node-property.groovy");
		//att_query.add("delete-edge-property.groovy");
		attribute_type.put("freebaseid", 1);
		attribute_type.put("mid", 0);		
		attribute_type.put("_id", 1);
		
		attribute_type.put("birthday", 0);
		attribute_type.put("browserUsed", 0);
		attribute_type.put("creationDate", 0);
		attribute_type.put("email", 0);
		attribute_type.put("firstName", 0);
		
		attribute_type.put("gender", 0);
		attribute_type.put("iid", 0);
		attribute_type.put("language", 0);
		attribute_type.put("lastName", 0);
		attribute_type.put("locationIP", 0);
		
		attribute_type.put("oid", 1);		
		attribute_type.put("content", 0);
		attribute_type.put("imageFile", 0);
		attribute_type.put("length", 0);
		attribute_type.put("xname", 0);
		
		attribute_type.put("url", 0);
		attribute_type.put("title", 0);
		attribute_type.put("type", 0);
		attribute_type.put("joinDate", 0);
		attribute_type.put("creationDate", 0);
		
		attribute_type.put("classYear", 0);
		attribute_type.put("workFrom", 0);
		
		titan_query.add("count-nodes.groovy");//5
		titan_query.add("count-edges.groovy");//5
		titan_query.add("find-unique-labels.groovy");//5
		titan_query.add("k-degree-in.groovy");//5
		titan_query.add("k-degree-out.groovy");//5
		titan_query.add("k-degree-both.groovy");//5

		

		// 32�ָ����ļ�
		query.add("node-specific-property-search.groovy");
		query.add("edge-specific-property-search.groovy");
		query.add("update-node-property.groovy");
		query.add("update-edge-property.groovy");
		
		query.add("insert-node.groovy");
		query.add("insert-edge.groovy");
		query.add("insert-edge-with-property.groovy");
		query.add("insert-node-property.groovy");
		query.add("insert-edge-property.groovy");
		query.add("insert-node-with-edges.groovy");

		query.add("count-nodes.groovy");//5
		query.add("count-edges.groovy");//5
		query.add("find-unique-labels.groovy");//5

		query.add("label-search.groovy");
		query.add("id-search-node.groovy");
		query.add("id-search-edge.groovy");

		
		query.add("NN-incoming.groovy");
		query.add("NN-outgoing.groovy");
		// query.add("");//δȷ����ѯ��Ӧ���ļ� v.both('l') v����߱�ǩ
		query.add("NN-incoming-unique-label.groovy");
		query.add("NN-outgoing-unique-label.groovy");
		query.add("NN-both-unique-label.groovy");
		query.add("k-degree-in.groovy");//5
		query.add("k-degree-out.groovy");//5
		query.add("k-degree-both.groovy");//5
		// query.add("");//g.v.out.dedup ����ߵĶ���
		query.add("BFS.groovy");
		query.add("BFS-labelled.groovy");
		query.add("shortest-path.groovy");
		query.add("shortest-path-labelled.groovy");
		query.add("delete-node-property.groovy");
		query.add("delete-edge-property.groovy");
		query.add("delete-nodes.groovy");
		query.add("delete-edges.groovy");


		// �������ݿ��Ӧ�Ļ�����Ϣ
		database_shell.add(" -i dbtrento/gremlin-neo4j -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\" ");// neo4j
		database_shell.add(" -i dbtrento/gremlin-arangodb  -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\"");// arangoDB
		database_shell.add(" -i dbtrento/gremlin-pg -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\"");// postgres
		database_shell.add(" -i dbtrento/gremlin-titan -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\" ");// titan

	}
	/*
	 * �����ļ������֣������л�benchmark��ͬ���ݿ�Ĳ���
	 */
	public void rename_file(int control) {
		String temp_name_meta="/home/"+username+"/graph-databases-testsuite-master/runtime/";
		String temp_name="/home/"+username+"/graph-databases-testsuite-master/runtime/tp2/";
		if(control==0) {//��neo4j��titan�ĵ�
			if(database_control.contains("neo4j")) {//��query��meta������
				File file1 = new File(temp_name_meta+"meta-neo4j");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file1.renameTo(new File(temp_name_meta+"meta"));
		        File file2 = new File(temp_name+"queries-neo4j");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file2.renameTo(new File(temp_name+"queries"));
				
			}else if(database_control.contains("titan")) {
				File file1 = new File(temp_name_meta+"meta-titan");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file1.renameTo(new File(temp_name_meta+"meta"));
		        File file2 = new File(temp_name+"queries-titan");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file2.renameTo(new File(temp_name+"queries"));
			}
			
		}else if (control==1) {//�Ļ�ԭ��������
			if(database_control.contains("neo4j")) {//��query��meta������
				File file1 = new File(temp_name_meta+"meta");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file1.renameTo(new File(temp_name_meta+"meta-neo4j"));
		        File file2 = new File(temp_name+"queries");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file2.renameTo(new File(temp_name+"queries-neo4j"));
				
			}else if(database_control.contains("titan")) {
				File file1 = new File(temp_name_meta+"meta");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file1.renameTo(new File(temp_name_meta+"meta-titan"));
		        File file2 = new File(temp_name+"queries");  
		        //��ԭ�ļ��и���ΪA������·���Ǳ�Ҫ�ġ�ע��  
		        file2.renameTo(new File(temp_name+"queries-titan"));
			}
			
		}
	}

	/**
	 * ������������Ϊ��ͬ�����Լ����ѯ����
	 * @param frequency �߻�����Ե�Ƶ��
	 * @param index �߻���ļ�����
	 * @return �ܲ�ѯ����
	 * @throws IOException
	 */
	public int choose_attribute(String datasetname,List<Integer> frequency, int index,int sum ) throws IOException {
		
		int sum_index=0;
		for (int i = 0; i < frequency.size(); i++) {
			int s = frequency.get(i)*sum/100;// ���Գ��ֵ�Ƶ��
			
			if(frequency.get(i)>0 && sum >0 &&s==0) {
				s=1;
			}
			System.out.println("���Ը���" + s);
			for (int j = 0; j < s; j++) {
				sum_index++;
				String[] attribute_split = attribute_value.get(i).split(":");
				String value = "";
				if (attribute_split.length == 2) {
					value += attribute_split[1];

				} else {
					value += attribute_split[1] + attribute_split[2];

				}
				
				String refilename = "/pre_data/data/" + att_query.get(index);
				if(database_control.contains("titan") ) { //titan���ݿ�
					
					if(!attribute_split[0].contains("_id")) {
						String requery = rewrite_query(refilename,att_query.get(index), attribute_split[0], value, sum_index);
						final_query.add(requery);
					}else {
						//id_flag=true;
						id_count=s;
						
					}
					
					
				}else {
					String requery = rewrite_query(refilename,att_query.get(index), attribute_split[0], value, sum_index);
					final_query.add(requery);
					
				}
				
			}
			System.out.println("id���ֵĴ���"+s);
		}
		
		return sum_index;
	}
	
	/*
	 * �������ݵ����ݿⲢ��������
	 */
	public void load_data_withindex(List<String> node_attributes,List<String> edge_attributes, String filepath,String database) {
		if (database.contains("1000")) { // neo4j
			create_index_neo4j(node_attributes,edge_attributes,filepath);
			
		} else if (database.contains("0100")) {// arangoDB
			create_index_arangodb(node_attributes,edge_attributes,filepath);
			
		} else if (database.contains("0010")) {// postgres
			create_index_pg(node_attributes,edge_attributes,filepath);
		} else { // titan
			create_index_titan(node_attributes,edge_attributes,filepath);
		}
	}
	
	/*
	 * neo4j���ݿⴴ������
	 */
	public void create_index_neo4j(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
			writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���				 
				
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				out.write("DEBUG = System.env.get(\"DEBUG\") != null\r\n");			
				for (int i = 0; i < node_attributes.size(); i++) { // ��Ӹ���
					out.write("graph.cypher(CREATE INDEX ON :vertex("+node_attributes.get(i)+"))\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // ��Ӹ���
					out.write("graph.cypher(CREATE INDEX ON :edge("+edge_attributes.get(i)+"))\r\n");
				}
				out.write("if(!SKIP_COMMIT){\r\n");
				out.write(" try {\r\n");
				out.write("  g.tx().commit();\r\n");
				out.write("} catch (MissingMethodException e) {\r\n");
				out.write(" System.err.println(\"Does not support g.commit(). Ignoring.\");\r\n");
				out.write("  }\r\n");
				out.write(" }\r\n");
				
				out.flush(); // �ѻ���������ѹ���ļ�
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Ϊtitan���ݿ�������ݲ���������
	 * @param node_attributes �������Լ���
	 * @param edge_attributes �����Լ���
	 * @param filepath loader�ļ�·��
	 */
	public void create_index_titan(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
			writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
			int control=node_attributes.size()+edge_attributes.size();
	
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				
                out.write("import com.thinkaurelius.titan.core.TitanFactory;\r\n" + 
                		"import com.thinkaurelius.titan.hadoop.TitanIndexRepair;\r\n");
				out.write("#META:\r\n" + 
						"def DATASET_FILE = System.env.get(\"DATASET\")\r\n" + 
						"def DEBUG = System.env.get(\"DEBUG\") != null\r\n" + 
						"def stime = System.currentTimeMillis()\r\n");
                
                //��������
                
                for (int i = 0; i < node_attributes.size(); i++) { // ��Ӹ���
                	out.write("PROPERTY_NAME"+i+"= \""+node_attributes.get(i)+"\";   \r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // ��Ӹ���
					int j=i+node_attributes.size();
					out.write("PROPERTY_NAME"+j+"= \""+edge_attributes.get(i)+"\";   \r\n");
				}
				int att_sum=node_attributes.size()+edge_attributes.size();
				for(int i=0;i<att_sum;i++) {
					out.write("def indx_name"+i+"=PROPERTY_NAME"+i+"+'_INDEX'\r\n");
				}
				             
                out.write("if (DATASET_FILE.endsWith('.xml')){\r\n" + 
                		" g.loadGraphML(DATASET_FILE)\r\n");
                //��������
                out.write("mgmt = g.getManagementSystem()\r\n");
                for(int i=0;i<att_sum;i++) {
                	
                	out.write("indx_name"+i+"_key = mgmt.getPropertyKey(PROPERTY_NAME"+i+")\r\n");
                   if(i<node_attributes.size()) {
                	   out.write("mgmt.buildIndex(indx_name"+i+", Vertex.class).addKey(indx_name"+i+"_key).buildCompositeIndex()\r\n");
                	}else {
                		out.write("mgmt.buildIndex(indx_name"+i+", Edge.class).addKey(indx_name"+i+"_key).buildCompositeIndex()\r\n");
                	}
                	
                }
                if(control==0) {
                	out.write("mgmt.commit()\r\n" + 
                    		"g.commit()\r\n" + 
                    		"\r\n" );
                   
                }else {
                	out.write("mgmt.commit()\r\n" + 
                    		"g.commit()\r\n" + 
                    		"\r\n" + 
                    		"// Block until the SchemaStatus transitions from INSTALLED to REGISTERED\r\n" + 
                    		"registered = false\r\n" + 
                    		"//System.err.print(\"SchemaStatus transitions from INSTALLED to REGISTERED: \")\r\n" + 
                    		"while (!registered) {\r\n" + 
                    		"    Thread.sleep(500L)\r\n" + 
                    		"    //System.err.print(\" . \")\r\n" + 
      
                    		"    mgmt = g.getManagementSystem()\r\n");
                }
                
                for(int i=0;i<att_sum;i++) {
                	out.write("  registered = true\r\n");
                	out.write(" idx  = mgmt.getGraphIndex(indx_name"+i+")\r\n" + 
                			"    for (k in idx.getFieldKeys()) {\r\n" + 
                			"        s = idx.getIndexStatus(k)\r\n" + 
                			"        registered &= s.equals(SchemaStatus.REGISTERED)\r\n" + 
                			"    }\r\n");
                }
                if(control==0) {
                	out.write(
                    		"System.err.println(\"End while\")\r\n" + 
                    		"\r\n" + 
                    		"// Run a Titan-Hadoop job to reindex (replace Murmur3 with your actual partitioner)\r\n" + 
                    		"pt = \"org.apache.cassandra.dht.Murmur3Partitioner\" // The default\r\n" + 
                    		"TITAN_PROPERTIES=System.env.get(\"TITAN_PROPERTIES\");\r\n");
                	
                }else {
                	out.write("  mgmt.rollback()\r\n" + 
                    		"}\r\n" + 
                    		"System.err.println(\"End while\")\r\n" + 
                    		"\r\n" + 
                    		"// Run a Titan-Hadoop job to reindex (replace Murmur3 with your actual partitioner)\r\n" + 
                    		"pt = \"org.apache.cassandra.dht.Murmur3Partitioner\" // The default\r\n" + 
                    		"TITAN_PROPERTIES=System.env.get(\"TITAN_PROPERTIES\");\r\n");
                }
                
                for(int i=0;i<att_sum;i++) {
                	out.write("TitanIndexRepair.cassandraRepair(TITAN_PROPERTIES, indx_name"+i+", \"\", pt)\r\n");
                	out.write("mgmt = g.getManagementSystem();mgmt.updateIndex(mgmt.getGraphIndex(indx_name"+i+"), SchemaAction.ENABLE_INDEX);mgmt.commit()\r\n");
                }
                
                
                out.write("}else {\r\n" + 
                		"    System.err.println(\"Start loading\")\r\n" + 
                		"    g.loadGraphSON(DATASET_FILE)\r\n");
                
                //��������
                out.write("mgmt = g.getManagementSystem()\r\n");
                for(int i=0;i<att_sum;i++) {
                	
                	out.write("indx_name"+i+"_key = mgmt.getPropertyKey(PROPERTY_NAME"+i+")\r\n");
                   if(i<node_attributes.size()) {
                	   out.write("mgmt.buildIndex(indx_name"+i+", Vertex.class).addKey(indx_name"+i+"_key).buildCompositeIndex()\r\n");
                	}else {
                		out.write("mgmt.buildIndex(indx_name"+i+", Edge.class).addKey(indx_name"+i+"_key).buildCompositeIndex()\r\n");
                	}
                	
                }
                if(control==0) {
                	out.write("mgmt.commit()\r\n" + 
                    		"g.commit()\r\n" + 
                    		"\r\n" );
                }else {
                	out.write("mgmt.commit()\r\n" + 
                    		"g.commit()\r\n" + 
                    		"\r\n" + 
                    		"// Block until the SchemaStatus transitions from INSTALLED to REGISTERED\r\n" + 
                    		"registered = false\r\n" + 
                    		"//System.err.print(\"SchemaStatus transitions from INSTALLED to REGISTERED: \")\r\n" + 
                    		"while (!registered) {\r\n" + 
                    		"    Thread.sleep(500L)\r\n" + 
                    		"    //System.err.print(\" . \")\r\n" + 
      
                    		"    mgmt = g.getManagementSystem()\r\n");
                }
                
                for(int i=0;i<att_sum;i++) {
                	out.write("  registered = true\r\n");
                	out.write(" idx  = mgmt.getGraphIndex(indx_name"+i+")\r\n" + 
                			"    for (k in idx.getFieldKeys()) {\r\n" + 
                			"        s = idx.getIndexStatus(k)\r\n" + 
                			"        registered &= s.equals(SchemaStatus.REGISTERED)\r\n" + 
                			"    }\r\n");
                }
                if(control==0) {
                	out.write( 
                    		"System.err.println(\"End while\")\r\n" + 
                    		"\r\n" + 
                    		"// Run a Titan-Hadoop job to reindex (replace Murmur3 with your actual partitioner)\r\n" + 
                    		"pt = \"org.apache.cassandra.dht.Murmur3Partitioner\" // The default\r\n" + 
                    		"TITAN_PROPERTIES=System.env.get(\"TITAN_PROPERTIES\");\r\n");
                }else {
                	out.write("  mgmt.rollback()\r\n" + 
                    		"}\r\n" + 
                    		"System.err.println(\"End while\")\r\n" + 
                    		"\r\n" + 
                    		"// Run a Titan-Hadoop job to reindex (replace Murmur3 with your actual partitioner)\r\n" + 
                    		"pt = \"org.apache.cassandra.dht.Murmur3Partitioner\" // The default\r\n" + 
                    		"TITAN_PROPERTIES=System.env.get(\"TITAN_PROPERTIES\");\r\n");
                }
                
                for(int i=0;i<att_sum;i++) {
                	out.write("TitanIndexRepair.cassandraRepair(TITAN_PROPERTIES, indx_name"+i+", \"\", pt)\r\n");
                	out.write("mgmt = g.getManagementSystem();mgmt.updateIndex(mgmt.getGraphIndex(indx_name"+i+"), SchemaAction.ENABLE_INDEX);mgmt.commit()\r\n");
                }
                
                
                
                out.write("System.err.println(\"End loading\")\r\n" + 
                		"}\r\n");						
				out.write("def exec_time = System.currentTimeMillis() - stime\r\n" + 
						"try {\r\n" + 
						"   g.commit();\r\n" + 
						"} catch (MissingMethodException e) {\r\n" + 
						" System.err.println(\"Does not support g.commit(). Ignoring.\");\r\n" + 
						"}\r\n" + 
						"if (DEBUG) {\r\n" + 
						"  v = g.V.count();\r\n" + 
						"    e = g.E.count();\r\n" + 
						" System.err.println(DATABASE + \" loaded V: \" + v + \" E: \" + e)\r\n" + 
						"}\r\n" + 
						"result_row = [DATABASE, DATASET, QUERY,'','','',String.valueOf(exec_time)]\r\n" + 
						"System.out.println(result_row.join(','));");
				out.flush(); // �ѻ���������ѹ���ļ�
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void create_index_pg(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
			writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
			
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				out.write("DEBUG = System.env.get(\"DEBUG\") != null;\r\n");

				for (int i = 0; i < node_attributes.size(); i++) { // ��Ӹ���
					if(attribute_type.get(node_attributes.get(i))==1) { //int or long type
						out.write("graph.createVertexLabeledIndex(\"vertex\","+ node_attributes.get(i)+", 0l);\r\n");
					}else {
						out.write("graph.createVertexLabeledIndex(\"vertex\","+node_attributes.get(i) +", \"x\");\r\n");
					}
					
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // ��Ӹ���
					if(attribute_type.get(node_attributes.get(i))==1) { //int or long type
						out.write("graph.createEdgeLabeledIndex(\"Edge\","+ edge_attributes.get(i)+", 0l);\r\n");
					}else {
						out.write("graph.createEdgeLabeledIndex(\"Edge\","+edge_attributes.get(i) +", \"x\");\r\n");
					}
				}
				out.write("g.tx().commit();\r\n");
				out.flush(); // �ѻ���������ѹ���ļ�
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void create_index_arangodb(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
			writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				

				for (int i = 0; i < node_attributes.size(); i++) { // ��Ӹ���
					out.write(" g.createKeyIndex(\"" + node_attributes.get(i) + "\",Vertex.class)\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // ��Ӹ���
					out.write(" g.createKeyIndex(\"" + edge_attributes.get(i) + "\",Edge.class)\r\n");
				}
				
				out.flush(); // �ѻ���������ѹ���ļ�
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��������������Ϣ�������ݵ����ݿⲢ��������
	 * 
	 * @param attributes ����������Ϣ
	 * @param filepath   �����ļ�·��
	 */
	public void load_data_withindex(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			int s=node_attributes.size()+edge_attributes.size();
			System.out.println("���������ĸ���"+s);
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
			writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
			
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				
                 out.write("#META:\r\n" + 
                 		"def DATASET_FILE = System.env.get(\"DATASET\")\r\n" + 
                 		"def DEBUG = System.env.get(\"DEBUG\") != null\r\n" + 
                 		"def stime = System.currentTimeMillis()\r\n" + 
                 		"if (DATASET_FILE.endsWith('.xml')){\r\n" + 
                 		" g.loadGraphML(DATASET_FILE)\r\n");
				for (int i = 0; i < node_attributes.size(); i++) { // ��Ӹ���
					out.write(" g.createKeyIndex(\"" + node_attributes.get(i) + "\",Vertex.class)\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // ��Ӹ���
					out.write(" g.createKeyIndex(\"" + edge_attributes.get(i) + "\",Edge.class)\r\n");
				}
				out.write("}");
				out.write("else {\r\n" + 
						"    System.err.println(\"Start loading\")\r\n" + 
						"    g.loadGraphSON(DATASET_FILE)\r\n");
				for (int i = 0; i < node_attributes.size(); i++) { // ��Ӹ���
					out.write(" g.createKeyIndex(\"" + node_attributes.get(i) + "\",Vertex.class)\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // ��Ӹ���
					out.write(" g.createKeyIndex(\"" + edge_attributes.get(i) + "\",Edge.class)\r\n");
				}
				out.write(" System.err.println(\"End loading\")\r\n" + 
						"}\r\n" + 
						"def exec_time = System.currentTimeMillis() - stime\r\n" + 
						"try {\r\n" + 
						"   g.commit();\r\n" + 
						"} catch (MissingMethodException e) {\r\n" + 
						" System.err.println(\"Does not support g.commit(). Ignoring.\");\r\n" + 
						"}\r\n" + 
						"if (DEBUG) {\r\n" + 
						"  v = g.V.count();\r\n" + 
						"    e = g.E.count();\r\n" + 
						" System.err.println(DATABASE + \" loaded V: \" + v + \" E: \" + e)\r\n" + 
						"}\r\n" + 
						"result_row = [DATABASE, DATASET, QUERY,'','','',String.valueOf(exec_time)]\r\n" + 
						"System.out.println(result_row.join(','));");

				out.flush(); // �ѻ���������ѹ���ļ�
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��д��ѯ�ļ�����������Ϣ�滻Ϊ�µ�����,�������ļ�������
	 * 
	 * @param path      ��ѯ�ļ�·��
	 * @param attribute ����
	 * @param value     ����ֵ
	 * @throws IOException
	 */
	public String rewrite_query(String path,String outpath, String attribute, String value, int index) throws IOException {
		//String s1 = this.getClass().getResource(path).getPath();
		InputStream is=this.getClass().getResourceAsStream(path); 
		//FileInputStream inputStream = new FileInputStream(s1);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

		String str = null;
		List<String> strs = new ArrayList<>();
		while ((str = bufferedReader.readLine()) != null) {
			if (str.contains("PROPERTY_NAME") && str.contains("=")) {
				str = "PROPERTY_NAME=\"" + attribute + "\";";
			} else if (str.contains("PROPERTY_VALUE") && str.contains("=")) {
				if(value.endsWith(" node int")) {
					//
					str = "PROPERTY_VALUE=" + value.replace(" node int", "") + ";";
				}else if(value.endsWith(" edge int")) {
					//;
					str = "PROPERTY_VALUE=" + value.replace(" edge int", "") + ";";
				}else if(value.endsWith(" node string")) {
					//);
					str = "PROPERTY_VALUE=\"" + value.replace(" node string", "") + "\";";
				}else if(value.endsWith(" edge string")) {
					//;
					str = "PROPERTY_VALUE=\"" + value.replace(" edge string", "") + "\";";
				}
				
			}
			strs.add(str);
		}

		// close
		is.close();
		bufferedReader.close();

		// д�뵽benchmark���ڵ�·��
		String temp_name="/home/"+username+"/graph-databases-testsuite-master/runtime/tp2/queries/";
		
		temp_name += outpath.split(".groovy")[0] + index + ".groovy";
		File writeName = new File(temp_name); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
		writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
		try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
			for (int i = 0; i < strs.size(); i++) {
				out.write(strs.get(i) + "\r\n");
			}
			out.flush(); // �ѻ���������ѹ���ļ�
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String temp_name_meta="/home/"+username+"/graph-databases-testsuite-master/runtime/meta/";
		
		temp_name_meta+=outpath.split(".groovy")[0] + index + ".groovy";
		File writeName_meta = new File(temp_name_meta); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
		writeName_meta.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
		try (FileWriter writer = new FileWriter(writeName_meta); BufferedWriter out = new BufferedWriter(writer)) {			
				out.write("#META:SID=[0-0]");
		
			out.flush(); // �ѻ���������ѹ���ļ�
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outpath.split(".groovy")[0] + index + ".groovy";

	}

	/**
	 * ִ��shell�����еõ����ս��
	 * 
	 * @param shell
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void exec(String shell) throws IOException, InterruptedException {
		
		String[] she   =     new   String[]       {"sh"  ,  "-c"  , shell }   ;
		
		StringBuilder result = new StringBuilder();

                BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        Process process=null;
        try {
           

             process = Runtime.getRuntime().exec(she);
    		//Process p  =  Runtime.getRuntime().exec(shell);
    		//ProcessBuilder builder = new ProcessBuilder(shell);
    		//File file = new File("/home/yangman/graph-databases-testsuite-master/");
    		//builder.directory(file);
    		//Process process=builder.start();
    		process.waitFor();
    		
    		
            // ��ȡ����ִ�н��, ���������: ��������� �� ����������PS: �ӽ��̵�������������̵����룩
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // ��ȡ���
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // �����ӽ���
            if (process != null) {
                process.destroy();
            }
        }

		
      
		
         String temp_name="/home/"+username+"/graph-databases-testsuite-master/runtime/mylog.txt";
 		
 		File writeName = new File(temp_name); // ���·�������û����Ҫ����һ���µ�output.txt�ļ�
 		writeName.createNewFile(); // �������ļ�,��ͬ�����ļ��Ļ�ֱ�Ӹ���
 		try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
 			
 				out.write(result.toString() + "\r\n");
 			
 			out.flush(); // �ѻ���������ѹ���ļ�
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
		
		
		
		
	}
	private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
	            // nothing
            }
        }
    }

	/**
	 * ��������������������ø�����Ϣ�����ݿ�+����+������Ϣ��
	 * 
	 * @param ve        ��������
	 * @param separator �ָ���
	 * @throws Exception
	 */
	public List<String> para_vector(String ve, String separator) throws Exception {
		 List<String> result=new ArrayList<>();
		String shell = "python test.py -r 1 ";
		String[] elements = ve.split(separator);
		// ǰ4λ�����ݿ���Ϣ
		// System.out.print(ve);
		String database = Double.valueOf(elements[0]).intValue() + "" + Double.valueOf(elements[1]).intValue()
				+ Double.valueOf(elements[2]).intValue() + Double.valueOf(elements[3]).intValue();
		shell += para_database(database);
		if(shell.contains("neo4j")) {
			database_control="neo4j";
		}else if(shell.contains("titan")) {
			database_control="titan";
		}
		int attribute_sum = Double.valueOf(elements[4]).intValue();
		// �������Ը�����4�����ݼ�����ȷ�������б�
		int index = 4 + attribute_sum;
		String key = attribute_sum +"";
		for(int i=1;i<=4;i++) {
			if(elements[index+i].endsWith(".0")) {
				key+=Double.valueOf(elements[index+i]).intValue()+"";
			}else {
				key+=elements[index+i];
			}
		}
		
		System.out.print("key��" + key);
		result.add(data_map.get(key));

		// ��ȡjar���ڵ����·��
		System.out.print("�ļ�"+dataset_map.get(key)+"\r\n");
		InputStream is=this.getClass().getResourceAsStream(dataset_map.get(key)); 
	//	String s1 = this.getClass().getResource().getPath();
	//	FileInputStream inputStream = new FileInputStream(s1);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

		String str = null;
		
		List<String> node_attrs = new ArrayList<>();
		List<String> edge_attrs = new ArrayList<>();
		
		int i = 5;
		while ((str = bufferedReader.readLine()) != null) {
			if (elements[i++].contains("1")) {
				if(str.contains("node")) {
					if(database_control.contains("titan") ) {
						if(!str.split(":")[0].contains("_id")) {
							node_attrs.add(str.split(":")[0]);
						}else {
							id_flag=true;
							System.out.println("id����������");
						}
						
					}
					
				}else {
					edge_attrs.add(str.split(":")[0]);
				}
				
			}
			attribute_value.add(str);

		}
		is.close();
		bufferedReader.close();

		String loadfile_path = "/home/"+username+"/graph-databases-testsuite-master/runtime/tp2/loader.groovy";
		String workload_path = "/home/"+username+"/graph-databases-testsuite-master/test.json";

		// String loadfile_path="src/pre_data/loader.groovy";
		// String workload_path="src/pre_data/test.json";
		Set<String> node_temp=new HashSet<>();
		Set<String> edge_temp=new HashSet<>();
		node_temp.addAll(node_attrs);
		edge_temp.addAll(edge_attrs);
		List<String> node_list=new ArrayList<>();
		List<String> edge_list=new ArrayList<>();
//		if(node_temp.contains("creationDate")&&edge_temp.contains("creationDate")) {
//			edge_temp.remove("creationDate");
//		}
		node_list.addAll(node_temp);
		edge_list.addAll(edge_temp);
		if(database_control.contains("titan")) {
			create_index_titan(node_list,edge_list, loadfile_path);
		}else if(database_control.contains("neo4j")) {
			load_data_withindex(node_attrs,edge_attrs, loadfile_path); // �������ݵ����ݿ���ļ�
		}
		
		
		List<Integer> node_frequency = new ArrayList<>();
		List<Integer> edge_frequency = new ArrayList<>();

		int fre_index = index + 37;
		System.out.println("����" + fre_index);
		for (int j = 0; j < attribute_sum; j++) {
			if(attribute_value.get(j).contains(" node")){
				node_frequency.add(Double.valueOf(elements[fre_index + j]).intValue());
			}else {
				edge_frequency.add(Double.valueOf(elements[fre_index + j]).intValue());
			}
			
		}

		List<Integer> percent = new ArrayList<>();
		for (int j = 0; j < 2; j++) {
			percent.add(Double.valueOf(elements[index + 5 + j]).intValue());
		}
		
		int total_percent=percent.get(0)+percent.get(1);//�漰���ԵĲ�ѯռ���ܲ�ѯ�İٷֱ�
		int query_sum=1000;//�趨�ܲ�ѯΪ1000��
		int attributes=10*total_percent;//�漰���ԵĲ�ѯ����
		choose_attribute(dataset_map.get(key),node_frequency, 0,attributes);
		choose_attribute(dataset_map.get(key),edge_frequency, 1,attributes);
		

		int workload_index = index + 5;
		List<Integer> workloads = new ArrayList<>();
		for (int j = 0; j < 32; j++) {

			workloads.add(query_sum * Double.valueOf(elements[j + workload_index]).intValue() / 100);
		}
		this.workloads_result.addAll(workloads);
		// �����ѯ�����ļ�
		System.out.println("�������"+this.workloads_result.size());
		List<String> datasets = new ArrayList<>();
		datasets.add(data_map.get(key));
		//System.out.print(database);
		if(database_control.contains("titan")) {
			//System.out.print("ִ��titan");
			para_workload(workload_path, datasets, workloads);
		}else if(database_control.contains("neo4j")) {
			//System.out.print("ִ��neo4j");
			para_workload_forneo4j(workload_path, datasets, workloads);
		}
		
		
		result.add(shell);

		return result;

	}

	/**
	 * �������ݿ���Ϣ��ѡ��洢
	 * 
	 * @param databases
	 */
	public String para_database(String databases) {
		String result = "";
		if (databases.contains("1000")) { // neo4j
			result += database_shell.get(0);
		} else if (databases.contains("0100")) {// arangoDB
			result += database_shell.get(1);
		} else if (databases.contains("0010")) {// postgres
			result += database_shell.get(2);
		} else { // titan
			result += database_shell.get(3);
		}
		return result;
	}

	/**
	 * ����������Ϣ�����ɸ��ص�ӳ���ļ�
	 * 
	 * @param workloads 35�ָ��ض�Ӧ�İٷֱ�
	 * @throws Exception
	 */
	public void para_workload(String filepath, List<String> datasets, List<Integer> workloads) throws Exception {
		List<String> queries = new ArrayList<>();
		//queries.add("a.groovy");
		queries.addAll(final_query);
		for (int i = 2; i < workloads.size(); i++) {
			//for (int j = 0; j < workloads.get(i); j++) {
			if(titan_query.contains(query.get(i))) {
				for (int j = 0; j <query_count ; j++) {//ÿ����ѯִ��3��
					queries.add(query.get(i));
				}
				

			}else {
				queries.add(query.get(i));
			}
			
		}
		
		JsonWrite(filepath, datasets, queries);
	}
	
	public void para_workload_forneo4j(String filepath, List<String> datasets, List<Integer> workloads) throws Exception {
		List<String> queries = new ArrayList<>();
		//queries.add("a.groovy");
		queries.addAll(final_query);
		//System.out.print("neo4j��test");
		for (int i = 2; i < workloads.size(); i++) {
			//System.out.print(workloads.get(i)+" ");
			for (int j = 0; j < workloads.get(i); j++) {			
				queries.add(query.get(i));
			}
			
		}
		
		JsonWrite(filepath, datasets, queries);
	}
	
	
	
	public void JsonWrite(String filepath, List<String> datasets, List<String> queries) throws Exception {
		//String s1 = this.getClass().getResource(filepath).getPath();
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8");

		// ����json��ʽ�ļ�
		try {
			// ��֤����һ�����ļ�
			File file = new File(filepath);
			if (!file.getParentFile().exists()) { // �����Ŀ¼�����ڣ�������Ŀ¼
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // ����Ѵ���,ɾ�����ļ�
				file.delete();
			}
			file.createNewFile();

			// ���´���json��ʽ����
			// ����һ��json�����൱��һ������
			JSONObject root = new JSONObject();
			// ���ݼ�
			/*
			 * { "datasets": [ "freebase_0000.json2" ], "queries": [ "count-edges.groovy",
			 * "count-nodes.groovy" ] }
			 */

			JSONArray array = new JSONArray();
			array.putAll(datasets);

			root.put("datasets", array);
			JSONArray array1 = new JSONArray();
			array1.putAll(queries);
			root.put("queries", array1);

			// ��ʽ��json�ַ���
			String jsonString = formatJson(root.toString());

			// ����ʽ������ַ���д���ļ�
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
			osw.close();
			System.out.println("���test.json�ļ�");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��λ�����ַ�����
	 */
	private static String SPACE = "   ";

	/**
	 * ���ظ�ʽ��JSON�ַ�����
	 *
	 * @param json δ��ʽ����JSON�ַ�����
	 * @return ��ʽ����JSON�ַ�����
	 */
	public static String formatJson(String json) {
		StringBuffer result = new StringBuffer();

		int length = json.length();
		int number = 0;
		char key = 0;

		// ���������ַ�����
		for (int i = 0; i < length; i++) {
			// 1����ȡ��ǰ�ַ���
			key = json.charAt(i);

			// 2�������ǰ�ַ���ǰ�����š�ǰ�����������´���
			if ((key == '[') || (key == '{')) {
				// ��1�����ǰ�滹���ַ��������ַ�Ϊ����������ӡ�����к������ַ��ַ�����
				if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
					result.append('\n');
					result.append(indent(number));
				}

				// ��2����ӡ����ǰ�ַ���
				result.append(key);

				// ��3��ǰ�����š�ǰ�����ţ��ĺ�����뻻�С���ӡ�����С�
				result.append('\n');

				// ��4��ÿ����һ��ǰ�����š�ǰ�����ţ�������������һ�Ρ���ӡ������������
				number++;
				result.append(indent(number));

				// ��5��������һ��ѭ����
				continue;
			}

			// 3�������ǰ�ַ��Ǻ����š������������´���
			if ((key == ']') || (key == '}')) {
				// ��1�������š������ţ���ǰ����뻻�С���ӡ�����С�
				result.append('\n');

				// ��2��ÿ����һ�κ����š������ţ�������������һ�Ρ���ӡ��������
				number--;
				result.append(indent(number));

				// ��3����ӡ����ǰ�ַ���
				result.append(key);

				// ��4�������ǰ�ַ����滹���ַ��������ַ���Ϊ����������ӡ�����С�
				if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
					result.append('\n');
				}

				// ��5��������һ��ѭ����
				continue;
			}

			// 4�������ǰ�ַ��Ƕ��š����ź��滻�У������������ı�����������
			if ((key == ',')) {
				result.append(key);
				result.append('\n');
				result.append(indent(number));
				continue;
			}

			// 5����ӡ����ǰ�ַ���
			result.append(key);
		}

		return result.toString();
	}

	/**
	 * ����ָ�������������ַ�����ÿһ�����������ո񣬼�SPACE��
	 *
	 * @param number ����������
	 * @return ָ�������������ַ�����
	 */
	private static String indent(int number) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < number; i++) {
			result.append(SPACE);
		}
		return result.toString();
	}

	/**
	 * �������ɵĽ���������ǩ������������ļ���
	 * 
	 * @param filepath
	 */
	public void write_results(String filepath, String vector,List<Integer> workloads) {
		try {
			
			String result = "/home/"+username+"/graph-databases-testsuite-master/runtime/results.csv";
			// String result="src/pre_data/data/results.csv";
			FileReader fr = new FileReader(result);
			//InputStream is=this.getClass().getResourceAsStream(result); 
			//String s1 = this.getClass().getResource(result).getPath();
			//FileInputStream inputStream = new FileInputStream(result);
			BufferedReader bufferedReader = new BufferedReader(fr);

			String str = null;

			long sum = 0;
			//List<Long> average_time=new ArrayList<>();//�洢ʣ��30����ѯ��ʱ��
			while ((str = bufferedReader.readLine()) != null) {
				String[] strs = str.split(",");
				
					if(strs[2].contains("loader")||strs[2].contains("node-specific-property-search")||strs[2].contains("edge-specific-property-search")) {
						sum += Long.valueOf(strs[6]);
					}else {
						Long ave=Long.valueOf(strs[6]);
						for(int i=0;i<query_count-1;i++) {
							str = bufferedReader.readLine();
							String[] strs_temp = str.split(",");
							ave+=Long.valueOf(strs_temp[6]);
						}
						sum+=ave/query_count*workloads.get(2);
						break;
					}
					
			
				
			}
			System.out.println("�������йص�shijian���"+sum);
			for (int i = 3; i < workloads.size(); i++) {
				long temp=0;
				for(int j=0;j<query_count;j++) {
					str = bufferedReader.readLine();
					String[] strs = str.split(",");
					temp+=Long.valueOf(strs[6]);
				}
				sum+=temp/query_count*workloads.get(i);
				
			}
			long id_sum=0;
			if(database_control.contains("titan")) {
				if(id_flag) {
					id_sum+=id_index*id_count;
				}else {
					id_sum+=id_no_index*id_count;
				}
			}
			System.out.println("id��ʱ��"+id_sum);
			sum+=id_sum;
			

			// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
			FileWriter writer = new FileWriter(filepath, true);
			writer.write(vector + "," + sum + "\r\n");
			writer.close();
			fr.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void write_results_forneo4j(String filepath, String vector,List<Integer> workloads) {
		try {
			
			String result = "/home/"+username+"/graph-databases-testsuite-master/runtime/results.csv";
			// String result="src/pre_data/data/results.csv";
			FileReader fr = new FileReader(result);
			//InputStream is=this.getClass().getResourceAsStream(result); 
			//String s1 = this.getClass().getResource(result).getPath();
			//FileInputStream inputStream = new FileInputStream(result);
			BufferedReader bufferedReader = new BufferedReader(fr);
			String str = null;
			long sum = 0;
			//List<Long> average_time=new ArrayList<>();//�洢ʣ��30����ѯ��ʱ��
			while ((str = bufferedReader.readLine()) != null) {
				String[] strs = str.split(",");
				sum += Long.valueOf(strs[6]);				
			}
			// ��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
			FileWriter writer = new FileWriter(filepath, true);
			writer.write(vector + "," + sum + "\r\n");
			writer.close();
			fr.close();
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void test() throws IOException, InterruptedException, Exception {
		Preprocessing pre = new Preprocessing();
		String vectors_path = "/pre_data/ve/"+database_control+".txt";
		InputStream is=this.getClass().getResourceAsStream(vectors_path); 
		//String s1 = this.getClass().getResource(vectors_path).getPath();
		//FileInputStream inputStream = new FileInputStream(s1);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

		String str = null;
		String result_path = "/home/"+username+"/test_result.txt";// ���ɵ������������ڵ��ļ�·��
		
		
 	
		int c=0;
		Scanner sc=new Scanner(System.in);
		String s1="";
		 System.out.print("����������������");  
	        s1=sc.nextLine();
	        int temp=Integer.valueOf(s1);
	        System.out.print("�����������Ϣ��");  
	        s1=sc.nextLine();
	        int control=Integer.valueOf(s1);
		while ((str = bufferedReader.readLine()) != null) {
			  c++;
	        
	        
	        if(c==temp) {
	        	System.out.println("��������");
				System.out.println(str);
				List<String> r= pre.para_vector(str, " ");
				String dataset=r.get(0);
				String shell =r.get(1);
				if(control==0) {
					shell="sudo make clean && sudo "+shell;
					shell+= "-s test.json";
					
					System.out.println("ִ������"+shell);
					
				}else if(control==1) {
					System.out.println("д���"+pre.workloads_result.size());
					if(database_control.contains("titan")) {
						pre.write_results(result_path, str,pre.workloads_result);
					}else if(database_control.contains("neo4j")) {
						pre.write_results_forneo4j(result_path, str,pre.workloads_result);
					}
					
					
					//ɾ�������ļ�
					String database="";
					if(database_control.contains("titan")) {
						database="titan";
					}else if(database_control.contains("neo4j")) {
						database="neo4j";
					}
					String delete_image="sudo make clean && sudo docker rmi gremlin-"+database+"_"+dataset;
					System.out.println("ɾ�������ļ�");
					pre.exec(delete_image);
				}
				

				//pre.exec(shell);
				
				
				break;
	        }
	        
				
			
		}

		// close
	
		is.close();
		sc.close();
		bufferedReader.close();
	}
	
	/*
	 * ��ȡ��ȡ���������ݵ�benchmark�в��Բ��ռ���ǩ
	 */
	public void label(String data_label_path) throws IOException, InterruptedException, Exception {
		Preprocessing pre = new Preprocessing();
		//String vectors_path = "/pre_data/ve/"+database_control+".txt";//��Ҫ���ǩ�������ļ�
		String vectors_path = data_label_path;//��Ҫ���ǩ�������ļ�
		 File file = new File(vectors_path);
	       BufferedReader bufferedReader = new BufferedReader(new FileReader(file));;
	//	InputStream is=this.getClass().getResourceAsStream(vectors_path); 
		
		//BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

		String str = null;
		String result_path = "/home/"+username+"/test_result.txt";// ���ɵ������������ڵ��ļ�·��
		
		
 	
		int c=0;
		/*Scanner sc=new Scanner(System.in);
		String s1="";
		 System.out.print("����������������");  
	        s1=sc.nextLine();
	        int temp=Integer.valueOf(s1);
	        System.out.print("�����������Ϣ��");  
	        s1=sc.nextLine();
	        int control=Integer.valueOf(s1);
	        */
		while ((str = bufferedReader.readLine()) != null) {
			 // c++;
	        
	        
	       
	        	System.out.println("��������");
				System.out.println(str);
				List<String> r= pre.para_vector(str, " ");
				String dataset=r.get(0);
				String shell =r.get(1);
				
					shell="sudo make clean && sudo "+shell;
					shell+= "-s test.json";
					
					System.out.println("ִ������"+shell);
					rename_file(0);
					pre.exec(shell);
					System.out.println("ִ���������");
					
				
					System.out.println("д���"+pre.workloads_result.size());
					if(database_control.contains("titan")) {
						pre.write_results(result_path, str,pre.workloads_result);
					}else if(database_control.contains("neo4j")) {
						pre.write_results_forneo4j(result_path, str,pre.workloads_result);
					}
					
					
					//ɾ�������ļ�
					String database="";
					if(database_control.contains("titan")) {
						database="titan";
					}else if(database_control.contains("neo4j")) {
						database="neo4j";
					}
					String delete_image="sudo make clean && sudo docker rmi gremlin-"+database+"_"+dataset;
					System.out.println("ɾ�������ļ�");
					pre.exec(delete_image);
					rename_file(1);
				

				//pre.exec(shell);
				
				
			
				
			
		}

		// close
	
		//is.close();
		//sc.close();
		bufferedReader.close();
	}
	public static void main(String[] args) throws Exception {

		Preprocessing pre = new Preprocessing();
		pre.test();
		

	}

}
