package com.google.gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lessu on 14-6-27.
 */
public class GsonValidate {

    static Pattern arrayPattern = Pattern.compile("\\[(\\d+?)\\]");

    public static JsonElement getElementByKeyPath(JsonElement element, String keyPath) {
        return getElementByKeyPath(element, keyPath, null);
    }

    public static JsonElement getElementByKeyPath(JsonElement element, String keyPath, JsonElement defaultValue) {
        try {
            String keys[] = keyPath.split("\\.");
            JsonElement data = element.getAsJsonObject();

            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];

                Matcher matcher = arrayPattern.matcher(key);
                List<String> arrayIndexes = new ArrayList<String>();

                while (matcher.find()) {
                    arrayIndexes.add(matcher.group(1));
                }

                key = key.replaceFirst("([^\\[]+)(\\[\\d+?\\])*", "$1");

                data = data.getAsJsonObject().get(key);

                for (int j = 0; j < arrayIndexes.size(); j++) {
                    data = data.getAsJsonArray().get(Integer.parseInt(arrayIndexes.get(j)));
                }

            }
            return data;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getStringByKeyPath(JsonElement element, String keyPath) {
        return getStringByKeyPath(element, keyPath, null);
    }

    public static String getStringByKeyPath(JsonElement element, String keyPath, String defaultValue) {
        JsonElement resultElement = getElementByKeyPath(element, keyPath, null);
        try {
            return resultElement.getAsString();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static JsonArray getAsArraySafe(JsonElement element) {
        try {
            return element.getAsJsonArray();
        } catch (Exception e) {
            return new JsonArray();
        }
    }

}
