package tramcity.client;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tramcity.client.common.ApiEnum;
import tramcity.client.common.SendPackage;
import tramcity.client.ui.CityAddNew;
import tramcity.client.ui.CityList;
import tramcity.client.ui.Dashboard;

public class TramcityClientMain {
	static Client client;
	public static void main(String args[]) throws InterruptedException {

		client = new Client("172.31.249.169", 1995);
//		client = new Client("127.0.0.1", 1995);
		client.start();
		//System.out.println("call view");
		CityList windowCityList  = new CityList(client);
		windowCityList.frame.setVisible(true);
	}
	
	

	public void getCityData() throws InterruptedException {
		// TODO Auto-generated method stub
		client.setResponseData(null);
		SendPackage sendP = new SendPackage();
		sendP.setApi(ApiEnum.CITY_FIND_ALL);		
		client.setSendP(sendP);
		JSONObject res = null;

		System.out.println("START:");
		Object obj = new Object();
		synchronized (obj) {
		while(res == null) {
			res = client.getResponseData();
			System.out.println("waiting:"+res);
			if(res!= null) {
				// if success true - get data bind to table 
				System.out.println(res.toString());
				boolean sMess;
				try {
					sMess = res.getBoolean("success");				
					if(sMess) {
						JSONArray jArray = res.getJSONArray("data");
						if(jArray.length()>0) {
							System.out.println("select last city");
							int cID = jArray.getJSONObject(jArray.length()-1).getInt("ID");
							Dashboard ctDetail = new Dashboard(client, cID);
							ctDetail.frame.setVisible(true);
						}else{
							System.out.println("Add new");
							CityAddNew ctAdd =	new CityAddNew(client);
							ctAdd.frame.setVisible(true);
						};
					}else {						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}obj.wait(50);
		} 
		//
		
		client.setResponseData(null);
	}
	
}