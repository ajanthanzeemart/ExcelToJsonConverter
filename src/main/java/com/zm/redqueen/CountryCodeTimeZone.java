package com.zm.redqueen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.ibm.icu.util.TimeZone;

public class CountryCodeTimeZone {

	public static void main(String... args) throws JSONException {
		ClassLoader classLoader = CountryCodeTimeZone.class.getClassLoader();
		File file = new File(classLoader.getResource("allCountries.json").getFile());

		try (Scanner scanner = new Scanner(file)) {
			StringBuilder result = new StringBuilder("");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			JSONArray countryList = new JSONArray(result.toString());

			for (int c = 0; c < countryList.length(); c++) {
				JSONObject country = countryList.getJSONObject(c);
				// System.out.println(country);

				String countryCode2 = country.getString("alpha-2");
				String countryCode3 = country.getString("alpha-3");
				String[] timeZones = TimeZone.getAvailableIDs(countryCode2);

				JSONArray timezoneOffsets = new JSONArray();

				Arrays.asList(timeZones).stream().forEach(tz -> {

					TimeZone timeZone = TimeZone.getTimeZone(tz);
					int offset = timeZone.getRawOffset();
					int offsetMinutes = offset / 1000 / 60;

					int offsetHour = Math.abs(offsetMinutes) / 60;
					int offsetMunite = Math.abs(offsetMinutes) % 60;

					String sign = offsetMinutes < 0 ? "-" : "+";
					String gmtOffset = "GMT" + sign + String.format("%02d", offsetHour) + ":"
							+ String.format("%02d", offsetMunite);

					String offsetTimeZone = "(" + gmtOffset + ") " + tz;
					JSONObject tzObj = new JSONObject();
					try {
						tzObj.put("timeZoneId", tz);
						tzObj.put("offsetTimeZone", offsetTimeZone);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					timezoneOffsets.put(tzObj);
				});

				country.put("countryCode", countryCode2);
				country.put("countryCode3", countryCode3);
				country.put("timeZoneOffset", timezoneOffsets);

				country.remove("intermediate-region-code");
				country.remove("sub-region-code");
				country.remove("intermediate-region");
				country.remove("iso_3166-2");
				country.remove("alpha-2");
				country.remove("alpha-3");
				country.remove("country-code");
				country.remove("region-code");

			}

			Path outPath = Paths.get("src/main/resources/AllCountryDetails.json");

			// Use try-with-resource to get auto-closeable writer instance
			try (BufferedWriter writer = Files.newBufferedWriter(outPath)) {
				writer.write(countryList.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
