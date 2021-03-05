
package io.cuillgln.toys.json;

import java.io.IOException;
import java.text.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperTest {

	public static void main(String[] args) throws ParseException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		// objectMapper.setDateFormat(new
		// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
		// DateFormat formatter = objectMapper.getDateFormat();
		// UserCreated us = new UserCreated("hello");
		// String json = objectMapper.writeValueAsString(us);
		// System.out.println(json);
		//
		// Date now = formatter.parse("2021-03-05T02:22:50.150+0000");
		// System.out.println(now);
		//
		// System.out.println("hello 'aax'''' bcc world!");

		String json = objectMapper.writeValueAsString(new JavaTimeBean());
		System.out.println(json);

		JavaTimeBean result = objectMapper.readValue(json, JavaTimeBean.class);
		System.out.println(result);
	}

}
