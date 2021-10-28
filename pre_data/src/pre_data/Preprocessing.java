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
	private List<String> query = new ArrayList<>();// 存储35种类型的负载对应的文件名,用于写test.json文件
	private List<String> database_shell = new ArrayList<>();// 存储4种数据库启动时需要的shell命令内容
	private Map<String, String> dataset_map = new HashMap<>();// 特征向量到数据集属性文件的映射
	private Map<String, String> data_map = new HashMap<>();// 特征向量到数据集文件的映射
	public String shell;// 最终执行的shell命令行信息
	private List<String> final_query = new ArrayList<>();//存储重写的查询文件名
	public List<String> attribute_value = new ArrayList<>();//存储属性及其值
	private List<String> att_query = new ArrayList<>();//与属性有关的查询文件名
	private Map<String,Integer> attribute_type=new HashMap<>();//存储每种属性是数值型（1）还是字符串型（0）
	private String username="yangman";
	
	private String database_control="newdata_neo4j"; //控制条件
	public List<Integer> workloads_result = new ArrayList<>();
	private Set<String> titan_query=new HashSet<>();
	private int query_count=3;
	private long id_index=364085624;
	private long  id_no_index=65548469614l;
	private boolean id_flag=false;
	private int id_count=0;
	

	
	/**
	 * 涉及属性的查询文件： Node-specific-property-search Edge-specific-property-search
	 * 
	 */

	/**
	 * 初始化各成员变量内容
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

		

		// 32种负载文件
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
		// query.add("");//未确定查询对应的文件 v.both('l') v的入边标签
		query.add("NN-incoming-unique-label.groovy");
		query.add("NN-outgoing-unique-label.groovy");
		query.add("NN-both-unique-label.groovy");
		query.add("k-degree-in.groovy");//5
		query.add("k-degree-out.groovy");//5
		query.add("k-degree-both.groovy");//5
		// query.add("");//g.v.out.dedup 有入边的顶点
		query.add("BFS.groovy");
		query.add("BFS-labelled.groovy");
		query.add("shortest-path.groovy");
		query.add("shortest-path-labelled.groovy");
		query.add("delete-node-property.groovy");
		query.add("delete-edge-property.groovy");
		query.add("delete-nodes.groovy");
		query.add("delete-edges.groovy");


		// 四种数据库对应的环境信息
		database_shell.add(" -i dbtrento/gremlin-neo4j -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\" ");// neo4j
		database_shell.add(" -i dbtrento/gremlin-arangodb  -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\"");// arangoDB
		database_shell.add(" -i dbtrento/gremlin-pg -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\"");// postgres
		database_shell.add(" -i dbtrento/gremlin-titan -e JAVA_OPTS=\"-Xms1G -Xmn128M -Xmx8G\" ");// titan

	}
	/*
	 * 更改文件夹名字，用于切换benchmark不同数据库的测试
	 */
	public void rename_file(int control) {
		String temp_name_meta="/home/"+username+"/graph-databases-testsuite-master/runtime/";
		String temp_name="/home/"+username+"/graph-databases-testsuite-master/runtime/tp2/";
		if(control==0) {//将neo4j和titan改掉
			if(database_control.contains("neo4j")) {//将query和meta都更改
				File file1 = new File(temp_name_meta+"meta-neo4j");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file1.renameTo(new File(temp_name_meta+"meta"));
		        File file2 = new File(temp_name+"queries-neo4j");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file2.renameTo(new File(temp_name+"queries"));
				
			}else if(database_control.contains("titan")) {
				File file1 = new File(temp_name_meta+"meta-titan");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file1.renameTo(new File(temp_name_meta+"meta"));
		        File file2 = new File(temp_name+"queries-titan");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file2.renameTo(new File(temp_name+"queries"));
			}
			
		}else if (control==1) {//改回原来的名字
			if(database_control.contains("neo4j")) {//将query和meta都更改
				File file1 = new File(temp_name_meta+"meta");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file1.renameTo(new File(temp_name_meta+"meta-neo4j"));
		        File file2 = new File(temp_name+"queries");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file2.renameTo(new File(temp_name+"queries-neo4j"));
				
			}else if(database_control.contains("titan")) {
				File file1 = new File(temp_name_meta+"meta");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file1.renameTo(new File(temp_name_meta+"meta-titan"));
		        File file2 = new File(temp_name+"queries");  
		        //将原文件夹更改为A，其中路径是必要的。注意  
		        file2.renameTo(new File(temp_name+"queries-titan"));
			}
			
		}
	}

	/**
	 * 根据特征向量为不同的属性计算查询个数
	 * @param frequency 边或点属性的频率
	 * @param index 边或点文件索引
	 * @return 总查询个数
	 * @throws IOException
	 */
	public int choose_attribute(String datasetname,List<Integer> frequency, int index,int sum ) throws IOException {
		
		int sum_index=0;
		for (int i = 0; i < frequency.size(); i++) {
			int s = frequency.get(i)*sum/100;// 属性出现的频率
			
			if(frequency.get(i)>0 && sum >0 &&s==0) {
				s=1;
			}
			System.out.println("属性个数" + s);
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
				if(database_control.contains("titan") ) { //titan数据库
					
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
			System.out.println("id出现的次数"+s);
		}
		
		return sum_index;
	}
	
	/*
	 * 加载数据到数据库并创建索引
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
	 * neo4j数据库创建索引
	 */
	public void create_index_neo4j(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // 相对路径，如果没有则要建立一个新的output.txt文件
			writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖				 
				
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				out.write("DEBUG = System.env.get(\"DEBUG\") != null\r\n");			
				for (int i = 0; i < node_attributes.size(); i++) { // 添加负载
					out.write("graph.cypher(CREATE INDEX ON :vertex("+node_attributes.get(i)+"))\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // 添加负载
					out.write("graph.cypher(CREATE INDEX ON :edge("+edge_attributes.get(i)+"))\r\n");
				}
				out.write("if(!SKIP_COMMIT){\r\n");
				out.write(" try {\r\n");
				out.write("  g.tx().commit();\r\n");
				out.write("} catch (MissingMethodException e) {\r\n");
				out.write(" System.err.println(\"Does not support g.commit(). Ignoring.\");\r\n");
				out.write("  }\r\n");
				out.write(" }\r\n");
				
				out.flush(); // 把缓存区内容压入文件
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 为titan数据库加载数据并创建索引
	 * @param node_attributes 顶点属性集合
	 * @param edge_attributes 边属性集合
	 * @param filepath loader文件路径
	 */
	public void create_index_titan(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // 相对路径，如果没有则要建立一个新的output.txt文件
			writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			int control=node_attributes.size()+edge_attributes.size();
	
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				
                out.write("import com.thinkaurelius.titan.core.TitanFactory;\r\n" + 
                		"import com.thinkaurelius.titan.hadoop.TitanIndexRepair;\r\n");
				out.write("#META:\r\n" + 
						"def DATASET_FILE = System.env.get(\"DATASET\")\r\n" + 
						"def DEBUG = System.env.get(\"DEBUG\") != null\r\n" + 
						"def stime = System.currentTimeMillis()\r\n");
                
                //加载数据
                
                for (int i = 0; i < node_attributes.size(); i++) { // 添加负载
                	out.write("PROPERTY_NAME"+i+"= \""+node_attributes.get(i)+"\";   \r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // 添加负载
					int j=i+node_attributes.size();
					out.write("PROPERTY_NAME"+j+"= \""+edge_attributes.get(i)+"\";   \r\n");
				}
				int att_sum=node_attributes.size()+edge_attributes.size();
				for(int i=0;i<att_sum;i++) {
					out.write("def indx_name"+i+"=PROPERTY_NAME"+i+"+'_INDEX'\r\n");
				}
				             
                out.write("if (DATASET_FILE.endsWith('.xml')){\r\n" + 
                		" g.loadGraphML(DATASET_FILE)\r\n");
                //创建索引
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
                
                //创建索引
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
				out.flush(); // 把缓存区内容压入文件
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void create_index_pg(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // 相对路径，如果没有则要建立一个新的output.txt文件
			writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				out.write("DEBUG = System.env.get(\"DEBUG\") != null;\r\n");

				for (int i = 0; i < node_attributes.size(); i++) { // 添加负载
					if(attribute_type.get(node_attributes.get(i))==1) { //int or long type
						out.write("graph.createVertexLabeledIndex(\"vertex\","+ node_attributes.get(i)+", 0l);\r\n");
					}else {
						out.write("graph.createVertexLabeledIndex(\"vertex\","+node_attributes.get(i) +", \"x\");\r\n");
					}
					
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // 添加负载
					if(attribute_type.get(node_attributes.get(i))==1) { //int or long type
						out.write("graph.createEdgeLabeledIndex(\"Edge\","+ edge_attributes.get(i)+", 0l);\r\n");
					}else {
						out.write("graph.createEdgeLabeledIndex(\"Edge\","+edge_attributes.get(i) +", \"x\");\r\n");
					}
				}
				out.write("g.tx().commit();\r\n");
				out.flush(); // 把缓存区内容压入文件
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void create_index_arangodb(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // 相对路径，如果没有则要建立一个新的output.txt文件
			writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				

				for (int i = 0; i < node_attributes.size(); i++) { // 添加负载
					out.write(" g.createKeyIndex(\"" + node_attributes.get(i) + "\",Vertex.class)\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // 添加负载
					out.write(" g.createKeyIndex(\"" + edge_attributes.get(i) + "\",Edge.class)\r\n");
				}
				
				out.flush(); // 把缓存区内容压入文件
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据属性索引信息加载数据到数据库并创建索引
	 * 
	 * @param attributes 属性索引信息
	 * @param filepath   加载文件路径
	 */
	public void load_data_withindex(List<String> node_attributes,List<String> edge_attributes, String filepath) {
		try {
			int s=node_attributes.size()+edge_attributes.size();
			System.out.println("建立索引的个数"+s);
			//String s1 = this.getClass().getResource(filepath).getPath();
			File writeName = new File(filepath); // 相对路径，如果没有则要建立一个新的output.txt文件
			writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				
                 out.write("#META:\r\n" + 
                 		"def DATASET_FILE = System.env.get(\"DATASET\")\r\n" + 
                 		"def DEBUG = System.env.get(\"DEBUG\") != null\r\n" + 
                 		"def stime = System.currentTimeMillis()\r\n" + 
                 		"if (DATASET_FILE.endsWith('.xml')){\r\n" + 
                 		" g.loadGraphML(DATASET_FILE)\r\n");
				for (int i = 0; i < node_attributes.size(); i++) { // 添加负载
					out.write(" g.createKeyIndex(\"" + node_attributes.get(i) + "\",Vertex.class)\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // 添加负载
					out.write(" g.createKeyIndex(\"" + edge_attributes.get(i) + "\",Edge.class)\r\n");
				}
				out.write("}");
				out.write("else {\r\n" + 
						"    System.err.println(\"Start loading\")\r\n" + 
						"    g.loadGraphSON(DATASET_FILE)\r\n");
				for (int i = 0; i < node_attributes.size(); i++) { // 添加负载
					out.write(" g.createKeyIndex(\"" + node_attributes.get(i) + "\",Vertex.class)\r\n");
				}
				for (int i = 0; i < edge_attributes.size(); i++) { // 添加负载
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

				out.flush(); // 把缓存区内容压入文件
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写查询文件，将属性信息替换为新的属性,返回新文件的名字
	 * 
	 * @param path      查询文件路径
	 * @param attribute 属性
	 * @param value     属性值
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

		// 写入到benchmark所在的路径
		String temp_name="/home/"+username+"/graph-databases-testsuite-master/runtime/tp2/queries/";
		
		temp_name += outpath.split(".groovy")[0] + index + ".groovy";
		File writeName = new File(temp_name); // 相对路径，如果没有则要建立一个新的output.txt文件
		writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
		try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
			for (int i = 0; i < strs.size(); i++) {
				out.write(strs.get(i) + "\r\n");
			}
			out.flush(); // 把缓存区内容压入文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String temp_name_meta="/home/"+username+"/graph-databases-testsuite-master/runtime/meta/";
		
		temp_name_meta+=outpath.split(".groovy")[0] + index + ".groovy";
		File writeName_meta = new File(temp_name_meta); // 相对路径，如果没有则要建立一个新的output.txt文件
		writeName_meta.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
		try (FileWriter writer = new FileWriter(writeName_meta); BufferedWriter out = new BufferedWriter(writer)) {			
				out.write("#META:SID=[0-0]");
		
			out.flush(); // 把缓存区内容压入文件
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outpath.split(".groovy")[0] + index + ".groovy";

	}

	/**
	 * 执行shell命令行得到最终结果
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
    		
    		
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
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

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

		
      
		
         String temp_name="/home/"+username+"/graph-databases-testsuite-master/runtime/mylog.txt";
 		
 		File writeName = new File(temp_name); // 相对路径，如果没有则要建立一个新的output.txt文件
 		writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
 		try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
 			
 				out.write(result.toString() + "\r\n");
 			
 			out.flush(); // 把缓存区内容压入文件
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
	 * 根据特征向量，解析获得各种信息（数据库+负载+索引信息）
	 * 
	 * @param ve        特征向量
	 * @param separator 分隔符
	 * @throws Exception
	 */
	public List<String> para_vector(String ve, String separator) throws Exception {
		 List<String> result=new ArrayList<>();
		String shell = "python test.py -r 1 ";
		String[] elements = ve.split(separator);
		// 前4位是数据库信息
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
		// 根据属性个数和4个数据集特征确定属性列表
		int index = 4 + attribute_sum;
		String key = attribute_sum +"";
		for(int i=1;i<=4;i++) {
			if(elements[index+i].endsWith(".0")) {
				key+=Double.valueOf(elements[index+i]).intValue()+"";
			}else {
				key+=elements[index+i];
			}
		}
		
		System.out.print("key是" + key);
		result.add(data_map.get(key));

		// 获取jar包内的相对路径
		System.out.print("文件"+dataset_map.get(key)+"\r\n");
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
							System.out.println("id属性有索引");
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
			load_data_withindex(node_attrs,edge_attrs, loadfile_path); // 加载数据到数据库的文件
		}
		
		
		List<Integer> node_frequency = new ArrayList<>();
		List<Integer> edge_frequency = new ArrayList<>();

		int fre_index = index + 37;
		System.out.println("索引" + fre_index);
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
		
		int total_percent=percent.get(0)+percent.get(1);//涉及属性的查询占的总查询的百分比
		int query_sum=1000;//设定总查询为1000条
		int attributes=10*total_percent;//涉及属性的查询个数
		choose_attribute(dataset_map.get(key),node_frequency, 0,attributes);
		choose_attribute(dataset_map.get(key),edge_frequency, 1,attributes);
		

		int workload_index = index + 5;
		List<Integer> workloads = new ArrayList<>();
		for (int j = 0; j < 32; j++) {

			workloads.add(query_sum * Double.valueOf(elements[j + workload_index]).intValue() / 100);
		}
		this.workloads_result.addAll(workloads);
		// 构造查询负载文件
		System.out.println("结果负载"+this.workloads_result.size());
		List<String> datasets = new ArrayList<>();
		datasets.add(data_map.get(key));
		//System.out.print(database);
		if(database_control.contains("titan")) {
			//System.out.print("执行titan");
			para_workload(workload_path, datasets, workloads);
		}else if(database_control.contains("neo4j")) {
			//System.out.print("执行neo4j");
			para_workload_forneo4j(workload_path, datasets, workloads);
		}
		
		
		result.add(shell);

		return result;

	}

	/**
	 * 解析数据库信息，选择存储
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
	 * 解析负载信息，生成负载的映射文件
	 * 
	 * @param workloads 35种负载对应的百分比
	 * @throws Exception
	 */
	public void para_workload(String filepath, List<String> datasets, List<Integer> workloads) throws Exception {
		List<String> queries = new ArrayList<>();
		//queries.add("a.groovy");
		queries.addAll(final_query);
		for (int i = 2; i < workloads.size(); i++) {
			//for (int j = 0; j < workloads.get(i); j++) {
			if(titan_query.contains(query.get(i))) {
				for (int j = 0; j <query_count ; j++) {//每个查询执行3次
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
		//System.out.print("neo4j的test");
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

		// 生成json格式文件
		try {
			// 保证创建一个新文件
			File file = new File(filepath);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();

			// 以下创建json格式内容
			// 创建一个json对象，相当于一个容器
			JSONObject root = new JSONObject();
			// 数据集
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

			// 格式化json字符串
			String jsonString = formatJson(root.toString());

			// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
			osw.close();
			System.out.println("完成test.json文件");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单位缩进字符串。
	 */
	private static String SPACE = "   ";

	/**
	 * 返回格式化JSON字符串。
	 *
	 * @param json 未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	public static String formatJson(String json) {
		StringBuffer result = new StringBuffer();

		int length = json.length();
		int number = 0;
		char key = 0;

		// 遍历输入字符串。
		for (int i = 0; i < length; i++) {
			// 1、获取当前字符。
			key = json.charAt(i);

			// 2、如果当前字符是前方括号、前花括号做如下处理：
			if ((key == '[') || (key == '{')) {
				// （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
				if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
					result.append('\n');
					result.append(indent(number));
				}

				// （2）打印：当前字符。
				result.append(key);

				// （3）前方括号、前花括号，的后面必须换行。打印：换行。
				result.append('\n');

				// （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
				number++;
				result.append(indent(number));

				// （5）进行下一次循环。
				continue;
			}

			// 3、如果当前字符是后方括号、后花括号做如下处理：
			if ((key == ']') || (key == '}')) {
				// （1）后方括号、后花括号，的前面必须换行。打印：换行。
				result.append('\n');

				// （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
				number--;
				result.append(indent(number));

				// （3）打印：当前字符。
				result.append(key);

				// （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
				if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
					result.append('\n');
				}

				// （5）继续下一次循环。
				continue;
			}

			// 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
			if ((key == ',')) {
				result.append(key);
				result.append('\n');
				result.append(indent(number));
				continue;
			}

			// 5、打印：当前字符。
			result.append(key);
		}

		return result.toString();
	}

	/**
	 * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
	 *
	 * @param number 缩进次数。
	 * @return 指定缩进次数的字符串。
	 */
	private static String indent(int number) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < number; i++) {
			result.append(SPACE);
		}
		return result.toString();
	}

	/**
	 * 根据生成的结果将打过标签的样本输出到文件中
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
			//List<Long> average_time=new ArrayList<>();//存储剩余30个查询的时间
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
			System.out.println("与属性有关的shijian结果"+sum);
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
			System.out.println("id的时间"+id_sum);
			sum+=id_sum;
			

			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
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
			//List<Long> average_time=new ArrayList<>();//存储剩余30个查询的时间
			while ((str = bufferedReader.readLine()) != null) {
				String[] strs = str.split(",");
				sum += Long.valueOf(strs[6]);				
			}
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
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
		String result_path = "/home/"+username+"/test_result.txt";// 生成的样本数据所在的文件路径
		
		
 	
		int c=0;
		Scanner sc=new Scanner(System.in);
		String s1="";
		 System.out.print("请输入数据索引：");  
	        s1=sc.nextLine();
	        int temp=Integer.valueOf(s1);
	        System.out.print("请输入控制信息：");  
	        s1=sc.nextLine();
	        int control=Integer.valueOf(s1);
		while ((str = bufferedReader.readLine()) != null) {
			  c++;
	        
	        
	        if(c==temp) {
	        	System.out.println("特征向量");
				System.out.println(str);
				List<String> r= pre.para_vector(str, " ");
				String dataset=r.get(0);
				String shell =r.get(1);
				if(control==0) {
					shell="sudo make clean && sudo "+shell;
					shell+= "-s test.json";
					
					System.out.println("执行命令"+shell);
					
				}else if(control==1) {
					System.out.println("写结果"+pre.workloads_result.size());
					if(database_control.contains("titan")) {
						pre.write_results(result_path, str,pre.workloads_result);
					}else if(database_control.contains("neo4j")) {
						pre.write_results_forneo4j(result_path, str,pre.workloads_result);
					}
					
					
					//删除镜像文件
					String database="";
					if(database_control.contains("titan")) {
						database="titan";
					}else if(database_control.contains("neo4j")) {
						database="neo4j";
					}
					String delete_image="sudo make clean && sudo docker rmi gremlin-"+database+"_"+dataset;
					System.out.println("删除镜像文件");
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
	 * 读取抽取的样本数据到benchmark中测试并收集标签
	 */
	public void label(String data_label_path) throws IOException, InterruptedException, Exception {
		Preprocessing pre = new Preprocessing();
		//String vectors_path = "/pre_data/ve/"+database_control+".txt";//需要打标签的数据文件
		String vectors_path = data_label_path;//需要打标签的数据文件
		 File file = new File(vectors_path);
	       BufferedReader bufferedReader = new BufferedReader(new FileReader(file));;
	//	InputStream is=this.getClass().getResourceAsStream(vectors_path); 
		
		//BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

		String str = null;
		String result_path = "/home/"+username+"/test_result.txt";// 生成的样本数据所在的文件路径
		
		
 	
		int c=0;
		/*Scanner sc=new Scanner(System.in);
		String s1="";
		 System.out.print("请输入数据索引：");  
	        s1=sc.nextLine();
	        int temp=Integer.valueOf(s1);
	        System.out.print("请输入控制信息：");  
	        s1=sc.nextLine();
	        int control=Integer.valueOf(s1);
	        */
		while ((str = bufferedReader.readLine()) != null) {
			 // c++;
	        
	        
	       
	        	System.out.println("特征向量");
				System.out.println(str);
				List<String> r= pre.para_vector(str, " ");
				String dataset=r.get(0);
				String shell =r.get(1);
				
					shell="sudo make clean && sudo "+shell;
					shell+= "-s test.json";
					
					System.out.println("执行命令"+shell);
					rename_file(0);
					pre.exec(shell);
					System.out.println("执行命令完毕");
					
				
					System.out.println("写结果"+pre.workloads_result.size());
					if(database_control.contains("titan")) {
						pre.write_results(result_path, str,pre.workloads_result);
					}else if(database_control.contains("neo4j")) {
						pre.write_results_forneo4j(result_path, str,pre.workloads_result);
					}
					
					
					//删除镜像文件
					String database="";
					if(database_control.contains("titan")) {
						database="titan";
					}else if(database_control.contains("neo4j")) {
						database="neo4j";
					}
					String delete_image="sudo make clean && sudo docker rmi gremlin-"+database+"_"+dataset;
					System.out.println("删除镜像文件");
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
