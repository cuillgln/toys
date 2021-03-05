
package io.cuillgln.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Base64;

public class RandomAccessFileTest {

	public static void main(String[] args) throws IOException {
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String token = "eyJraWQiOiJhZGJiZTA4Ny1hZDAwLTQ2NTktODk3Mi00ZjRiYTBiNzYwNTYiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1ZCI6InRlc3RfY2xpZW50IiwibG9naW5JZCI6MiwiYnVzaUNvZGUiOjAsInJvbGVzIjpbMV0sImlzcyI6Imh0dHBzOlwvXC9jMmlkLmNvbSIsImV4cCI6MTYxMTgyMTI3MSwicGFydHlJZCI6MSwib3JnSWQiOjB9.EH2EdSvB7stwxUNefRWQIJMs3w1znIa09RFRx4YCAAeTXtyPEqjHLTDO8pop-h2jG1WOWo3vPBfmWONuWy8GcektaERJb1cYM1j1IM6dbjsksz7J7GqHDdfiTcDaY7C-q9HqDMNpxliCWA0fZlD5xOIblpAKEMX0fV08y195VA9kX79iH6yCrU2NbSvKAFHUpSE-mKul32AdqDsVdEC9wVNE10N29e1SKkQ4v22FDouOKG9wzeX31hSaZD4-xX9rr28eTD_5DY4sY5UsJE7rpChtpic_ZtpkbM1nCRIKfTcu7b0P674F73opD01gaXfSMa7PdcUgSWEnBNx8eW7BQg";
		String[] sa = token.split("\\.");
		for (String as : sa) {
			System.out.println(as);
			System.out.println(new String(decoder.decode(as)));
		}
		
		
	}

}
