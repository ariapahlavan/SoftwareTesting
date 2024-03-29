package pset6;


public class MinWebTestGenerator {
	public final static String NEG = "-3",
			ZERO = "0",
			POS = "7",
			INF = "infinity",
			INV = "Please enter integer values only!",
			MAX = "max",
			MIN = "min",
			EMPTY_ASSERT = "\t\tassertEquals(\"\", output);\n";

	public static void main(String[] a) {
		String suite = new MinWebTestGenerator().createTestSuite();
		System.out.println(suite);
	}

	String createTestSuite() {
		StringBuilder sb = new StringBuilder();
		sb.append(packageDecl());
		sb.append("\n");
		sb.append(imports());
		sb.append("\n");
		sb.append(testsuite());
		return sb.toString();
	}

	String packageDecl() {
		return "package pset6;\n";
	}

	String imports() {
		return "import static org.junit.Assert.*;\n\n"
		       + "import org.junit.After;\n"
		       + "import org.junit.AfterClass;\n"
		       + "import org.junit.Before;\n"
		       + "import org.junit.BeforeClass;\n"
		       + "import org.junit.Test;\n\n"
		       + "import org.openqa.selenium.By;\n"
		       + "import org.openqa.selenium.WebDriver;\n"
		       + "import org.openqa.selenium.WebElement;\n"
		       + "import org.openqa.selenium.firefox.FirefoxDriver;\n";
	}

	private String testsuite() {
		StringBuilder sb = new StringBuilder();
		sb.append("public class MinWebTestSuite {\n");

		String[] inputBlocks = {NEG, ZERO, POS, INF};
		String clickStr = "\t\twe = wd.findElement(By.id(\"computeButton\"));\n" +
		                  "\t\twe.click();\n";
		String closingBrace = "\t}\n\n";
		int    counter      = 0;

		sb.append(minInitGlobal());
		sb.append(minInitSetUpClass());
		sb.append(minInitSetUp());
		sb.append(minInitTearDown());
		sb.append(minInitTearDownClass());


		for (int i = 0; i < inputBlocks.length; i++) {
			for (int j = 0; j < inputBlocks.length; j++) {
				for (int k = 0; k < inputBlocks.length; k++) {
					String x = inputBlocks[i],
							y = inputBlocks[j],
							z = inputBlocks[k],
							min = calcMin(x, y, z),
							send = sendBlockWhere(x, y, z),
							out = retrieveOutput(),
							check = makeAssertWhere(x, y, z, min);

					sb.append(makeHeaderWhere(counter));
					counter++;
					sb.append(send)
					  .append(out)
					  .append(EMPTY_ASSERT)
					  .append(closingBrace);

					sb.append(makeHeaderWhere(counter));
					counter++;
					sb.append(send)
					  .append(clickStr)
					  .append(out)
					  .append(check)
					  .append(closingBrace);
				}
			}
		}

		sb.append("}\n");
		return sb.toString();
	}

	static String makeHeaderWhere(int counter) {
		return "\t@Test public void t" + counter + "() {\n";
	}

	private String makeAssertWhere(String x, String y, String z, String min) {
		return min.equals(INV) ? "\t\tassertEquals(\"" + INV + "\", output);\n" :
		       "\t\tassertEquals(\"min(" + x + ", "
		       + y + ", "
		       + z + ") = " + min + "\", output);\n";
	}

	static String retrieveOutput() {
		return "\t\toutput = result.getText();\n";
	}

	private String sendBlockWhere(String x, String y, String z) {
		return "\t\twe = wd.findElement(By.id(\"x\"));\n" +
		       "\t\twe.sendKeys(\"" + x + "\");\n" +
		       "\t\twe = wd.findElement(By.id(\"y\"));\n" +
		       "\t\twe.sendKeys(\"" + y + "\");\n" +
		       "\t\twe = wd.findElement(By.id(\"z\"));\n" +
		       "\t\twe.sendKeys(\"" + z + "\");\n";
	}

	static String calcMin(String x, String y, String z) {
		if (x.equals(INF) ||
		    y.equals(INF) ||
		    z.equals(INF)) return INV;

		if (x.equals(NEG) ||
		    y.equals(NEG) ||
		    z.equals(NEG)) return NEG;

		if (x.equals(ZERO) ||
		    y.equals(ZERO) ||
		    z.equals(ZERO)) return ZERO;

		return POS;
	}

	private String minInitGlobal() {
		return "\tprivate String output;\n" +
		       "\tprivate WebElement we;\n" +
		       "\tprivate static WebDriver wd;\n" +
		       "\tprivate static WebElement result;\n";
	}

	private String minInitSetUpClass() {
		return "\n\t@BeforeClass\n" +
		       "\tpublic static void setUpClass() {\n" +

		       "\t\tSystem.setProperty(\"webdriver.gecko.driver\", \"/home/aria/webdriver/firefox/geckodriver\");\n" +
		       "\t\twd = new FirefoxDriver();\n" +

		       "\t\tString classPath = MinWebTest.class.getResource(\"min.html\").toString();\n" +
		       "\t\twd.get(classPath);\n" +
		       "\t}\n\n";
	}

	private String minInitSetUp() {
		return "\t@Before\n" +
		       "\tpublic void setUp() {\n" +
		       "\t\tresult = wd.findElement(By.id(\"result\"));\n" +
		       "\t}\n\n";
	}

	private String minInitTearDown() {
		return "\t@After\n" +
		       "\tpublic void tearDown() {\n" +
		       "\t\twd.navigate().refresh();\n" +
		       "\t}\n\n";
	}

	private String minInitTearDownClass() {
		return "\t@AfterClass\n" +
		       "\tpublic static void tearDownClass(){\n" +
		       "\t\twd.quit();\n" +
		       "\t}\n\n";
	}
}
