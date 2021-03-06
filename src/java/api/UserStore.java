/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Suguru
 */
public class UserStore {
    
    private List<User> tempUserList;
    private User tempUser;
    private UserAPI userAPI = new UserAPI();
    
    ObjectMapper mapper = new ObjectMapper();
    JSONParser jParser = new JSONParser();
    JSONObject jsonObject;
    String jsonString;
    JSONArray jsonArray;
    
    public List<User> getAllUsers() throws ParseException, IOException {
        jsonString = userAPI.getAllUsers();

        mapper = new ObjectMapper();
        tempUser = null;
        tempUserList = new ArrayList<>();
        try {
            jsonArray = (JSONArray) jParser.parse(jsonString);
            for (int i = 0; i < jsonArray.size(); i++) {
                tempUser = mapper.readValue(jsonArray.get(i).toString(), User.class);
                tempUserList.add(tempUser);
            }
        } catch (Exception ex) {
            JSONObject jsonObject = (JSONObject) jParser.parse(jsonString);
            System.out.println(jsonObject);
            tempUser = mapper.readValue(jsonObject.toString(), User.class);
            tempUserList.add(tempUser);
        }

        return tempUserList;
    }
    
    public User getUserForUsername(String username) throws ParseException, IOException {
        jsonString = userAPI.getAllUsers();

        mapper = new ObjectMapper();
        tempUser = null;
        tempUserList = new ArrayList<>();
        try {
            jsonArray = (JSONArray) jParser.parse(jsonString);
            for (int i = 0; i < jsonArray.size(); i++) {
                tempUser = mapper.readValue(jsonArray.get(i).toString(), User.class);
                tempUserList.add(tempUser);
            }
        } catch (Exception ex) {
            JSONObject jsonObject = (JSONObject) jParser.parse(jsonString);
            System.out.println(jsonObject);
            tempUser = mapper.readValue(jsonObject.toString(), User.class);
            tempUserList.add(tempUser);
        }

        return tempUserList.get(0);
    }
    
    
    
}
