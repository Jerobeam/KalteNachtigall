package prototype;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import prod.Train;

public class JSONProto {

	public static void main(String[] args) {
		JSONParser parser = new JSONParser();

		// Read
				try {
					Object obj = parser.parse(new FileReader("savedata/trains.json"));
		
					JSONObject jsonObject = (JSONObject) obj;
		
					// Loop über alle Zugobjekte im JSON File
					JSONArray trains = (JSONArray) jsonObject.get("trains");
					Iterator<JSONObject> iterator = trains.iterator();
					while (iterator.hasNext()) {
						JSONObject train = (JSONObject) iterator.next();
						System.out.println(train.get("name"));
					}
		
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

		// Write
//		JSONArray trainsArray = new JSONArray();
//		JSONObject trainObject = new JSONObject();
//		trainObject.put("name", "Way");
//		trainsArray.add(trainObject);
//		trainObject = new JSONObject();
//		trainObject.put("name", "Zug2");
//		trainsArray.add(trainObject);
//		
//		JSONObject obj = new JSONObject();
//		obj.put("name", "mkyong.com");
//		obj.put("age", new Integer(100));
//
//		JSONArray list = new JSONArray();
//		list.add("msg 1");
//		list.add("msg 2");
//		list.add("msg 3");
//
//		obj.put("messages", list);
//
//		try {
//
//			FileWriter file = new FileWriter("savedata/trains.json");
//			file.write(trainsArray.toJSONString());
//			file.flush();
//			file.close();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
}

//{
//"trains":[
//	{
//		"name": "Way"
//	},
//	{
//		"name": "Okeey"
//	}
//]
//}
