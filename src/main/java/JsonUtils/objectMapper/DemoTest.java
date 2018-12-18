package JsonUtils.objectMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author L
 * @description Jackson 高性能的JSON处理 ObjectMapper
 * JSON与对象之间的转换
 * @date 2018年11月28日
 */
public class DemoTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 示例参数 - List：
     */
    private List<Integer> resultList = new ArrayList<Integer>() {{
        add(1);
        add(2);
        add(3);
        add(4);
    }};
    /**
     * 示例参数 - Map：
     */
    private Map<String, Object> resultMap = new HashMap<String, Object>() {{
        put("a", "a");
        put("b", "b");
        put("c", "c");
        put("resultList", resultList);
    }};
    /**
     * 示例参数 - JSON：
     */
    private String reslutJson = "{\"a\":\"a\",\"b\":\"b\",\"c\":\"c\",\"resultList\":[1,2,3,4]}";
    /**
     * 示例参数 - JSON：
     */
    private String reslutLMJson =
            "[{\"a\":\"a\",\"b\":\"b\",\"c\":\"c\",\"resultList\":[1,2,3,4]}," +
                    "{\"a\":\"a\",\"b\":\"b\",\"c\":\"c\",\"resultList\":[1,2,3,4]}," +
                    "{\"a\":\"a\",\"b\":\"b\",\"c\":\"c\",\"resultList\":[1,2,3,4]}," +
                    "{\"a\":\"a\",\"b\":\"b\",\"c\":\"c\",\"resultList\":[1,2,3,4]}]";
    /**
     * 示例参数 - LIST_MAP：
     */
    private List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>() {{
        add(resultMap);
        add(resultMap);
        add(resultMap);
        add(resultMap);
    }};

    /**
     * 示例 - 1、将对象转换成 JSON 字符串:
     * 将对象以字符串的形式返回
     *
     * @throws Exception
     */
    @Test
    public void writeValueAsString() throws Exception {
        String reslutJson = objectMapper.writeValueAsString(resultMap);
        String reslutLMJson = objectMapper.writeValueAsString(resultListMap);
        System.out.println(reslutJson);
        System.out.println(reslutLMJson);
    }

    /**
     * 示例 - 2、将对象转成JSON并以file对象将数据写出：
     * 对象以文件的形式写出
     *
     * @throws Exception
     */
    @Test
    public void writeValueAsFile() throws Exception {
        //写出的json存储文件位置
        String path =
                this.getClass().getResource("").getPath().concat(File.separator).concat("result_file.json");
        System.out.println(path);
        objectMapper.writeValue(new File(path), resultMap);
    }

    /**
     * 示例 - 3、将对象转成JSON并以 OutputStream 对象将数据写出：
     * 对象以文件的形式写出
     *
     * @throws Exception
     */
    @Test
    public void writeValueAsOs() throws Exception {
        //写出的json存储文件位置
        String path =
                this.getClass().getResource("").getPath().concat(File.separator).concat("result_os.json");
        OutputStream outputStream = new FileOutputStream(new File(path));
        objectMapper.writeValue(outputStream, resultMap);
    }

    /**
     * 示例 - 4、将对象转成JSON并以 Writer 对象将数据写出：
     * 对象以文件的形式写出
     *
     * @throws Exception
     */
    @Test
    public void writeValueAsWriter() throws Exception {
        //写出的json存储文件位置
        String path =
                this.getClass().getResource("").getPath().concat(File.separator).concat("result_writer.json");
        Writer writer = new FileWriter(path);
        objectMapper.writeValue(writer, resultMap);
    }

    /**
     * 示例 - 5、将JSON字符串转换成MAP数据:
     * json 转换成Map类型的数据
     *
     * @throws Exception
     */
    @Test
    public void readValue() throws Exception {
        Map<String, Object> map = objectMapper.readValue(reslutJson, Map.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    /**
     * 示例 - 6、将JSON字符串转换成LIST - MAP 数据结构
     * 以 JavaType 的方式解析当前json数据并返回list-map结构的数据
     *
     * @throws Exception
     */
    @Test
    public void readValueToJavaType() throws Exception {
        //采用 JavaType 方式解析
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, Map.class);
        List<Map<String, Object>> listMap = objectMapper.readValue(reslutLMJson, javaType);
        int i = 0;
        for (Map<String, Object> map : listMap) {
            System.out.println("第 " + ++i + " 个");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }
    }

    /**
     * 示例 - 7、将JSON字符串转换成LIST - MAP 数据结构V2：
     * 以 TypeReference 的方式解析当前json数据并返回list-map结构的数据
     *
     * @throws Exception
     */
    @Test
    public void readValueToTypeReference() throws Exception {
        //与以上解析结果相同
        List<Map<String, Object>> listMap1 =
                objectMapper.readValue(reslutLMJson, new TypeReference<List<Map<String, Object>>>() {
                });
        int j = 0;
        for (Map<String, Object> map : listMap1) {
            System.out.println("第 " + ++j + " 个");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
        }
    }
}
