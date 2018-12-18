package JsonUtils;


import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

//import org.apache.log4j.Logger;

public class JsonMapUtils {
	/** 日志文件生成器 */
//	private static Logger log = Logger.getLogger(JsonToMapUtils.class);
	@SuppressWarnings("unchecked")
	/**
	 * 2018年8月14日 json转成map
	 */
	public static Map<String, Object> parseJsonToMap(String jsonStr){
	    Map<String, Object> map =Maps.newHashMap();
	    //最外层解析
	    JSONObject json = JSONObject.fromObject(jsonStr);
	    for(Object k : json.keySet()){
		      Object v = json.get(k); 
		      //如果内层还是数组的话，继续解析
		      if(v instanceof JSONArray){
					List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					Iterator<JSONObject> it = ((JSONArray)v).iterator();
					while(it.hasNext()){
					  JSONObject json2 = it.next();
					  //递归
					  list.add(parseJsonToMap(json2.toString()));
					}
					map.put(k.toString(), list);
//					log.info("k.toString()="+k.toString()+"list="+list);
		      } else {
		    	  map.put(k.toString(), v);
//		    	  log.info("k.toString()="+k.toString()+"v="+v);
		      }
	    }
	    return map;
	}

	/**
	 * 2018年8月14日
	 * map转成json
	 * @param map
	 * @return
	 */
   public static String mapToJson(Map<String, Object> map) {
	   
       Set<String> keys = map.keySet();
       String key = "";
       String value = "";
       StringBuffer jsonBuffer = new StringBuffer();
       jsonBuffer.append("{");
       for (Iterator<String> it = keys.iterator(); it.hasNext();) {
           key = (String) it.next();
           value = map.get(key).toString();
           jsonBuffer.append(key + ":" +"\""+ value+"\"");
           if (it.hasNext()) {
               jsonBuffer.append(",");
           }
       }
       jsonBuffer.append("}");
       return jsonBuffer.toString();
   }

	/**
	 * key:value转成map
	 * @param keyVstr
	 * @return
	 */
   public static Map<String, Object>  keyVtoMap(String keyVstr){
	   BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(keyVstr.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
	   String line;
	   StringBuffer strbuf=new StringBuffer();
	   Map<String, Object> map= Maps.newHashMap();
	   try {
		   while ( (line = br.readLine()) != null ) {
               if(StringUtils.isNotBlank(line)){
                   String[]strArray=line.split(":");
                   if(null!=strArray&&2==strArray.length){
					   map.put(strArray[0],strArray[1]);
				   }
               }
           }
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   return map;
   }
}
