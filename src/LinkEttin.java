import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class LinkEttin {
	static WebDriver driver;
	static DesiredCapabilities capabilities;
	static Document doc;
	final static long startTime = System.nanoTime();
	public static List<String> lf = new ArrayList<String>(); //final list
	public static List<String> lt = new ArrayList<String>(); //temporal list
	
	
	public static final String USERNAME = "YOUR_USERNAME";
	public static final String ACCESS_KEY = "YOUR_ACCESS_KEY";
	public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";
	public static List<String> lines = new ArrayList<String>();
	public static int nn, input, na;//test
	public static int nitext, nipwd, niemail, nitel, nicbox, niradio, nisubmit; //nº de elemento dentro del input
	public static int ninput, nselect, nbtn;//nº de elementos fuera de input
	private static final String newLine = System.getProperty("line.separator");
	static File f = new File(System.getProperty("user.home") + "/Desktop/SelSoupLog.txt");
        
	
	
	private static synchronized void writeToFile(String msg){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-YYYY");
            LocalDate localDate = LocalDate.now();
	    String fileName = System.getProperty("user.dir")+"/"+dtf.format(localDate)+"_run.txt";
	    PrintWriter printWriter = null;
	    File file = new File(fileName);
	    try {
	        if (!file.exists()) file.createNewFile();
	        //printWriter = new PrintWriter(new FileOutputStream(fileName, true)); //old
                 Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true), "UTF8"));

                out.append(newLine + msg);
                out.flush();
                out.close();
	       // printWriter.write(newLine + msg);//old
	    } catch (IOException ioex) {
	        ioex.printStackTrace();
	    } finally {
	        if (printWriter != null) {
	            printWriter.flush();
	            printWriter.close();
	        }
	    }
	 }
	
	public static void selUp(String url, boolean online) {
		if (!online) {
			url = "file:///" + url;
		}

		try {
			// Método para levantar selenium
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/driver/geckodriver.exe");
			driver = new FirefoxDriver();
			capabilities = DesiredCapabilities.htmlUnit();
			capabilities.setBrowserName("Firefox");
			driver.manage().timeouts().pageLoadTimeout(1000000, TimeUnit.MILLISECONDS);
			driver.manage().window().maximize();
			driver.get(url);
			System.out.println("[INFO]:selenium got " + url);
		} catch (Exception ex) {
			System.out.println("[INFO]:selenium PROBLEM CAN NOT GET UP. Check driver path.");
		}
	}

	public static void soupUp(String filepath, boolean online) {
		File input = new File(filepath);
		try {

			if (online) {
				// doc = Jsoup.connect(filepath).get();
				doc = Jsoup.connect(filepath).get();// start jsoup online
				System.out.println("[INFO]:jsoup got " + filepath);
			} else {
				doc = Jsoup.parse(input, "UTF-8");// start jsoup offline
				System.out.println("[INFO]:jsoup got " + filepath);
			}
		} catch (Exception ex) {
			System.out.println("[INFO]:jsoup PROBLEM CAN NOT GET UP");
		}

	}
	
	public static int occurrences(String string, String subString, boolean allowOverlapping) {
		//get n of occu
	    string += "";
	    subString += "";
	    if (subString.length() <= 0) return (string.length() + 1);

	    int n = 0,
	        pos = 0,
	        step = allowOverlapping ? 1 : subString.length();

	    while (true) {
	        pos = string.indexOf(subString, pos);
	        if (pos >= 0) {
	            ++n;
	            pos += step;
	        } else break;
	    }
	    return n;
	}
	
	public static void login(){
		String username="";
		String pass="";
		//id=login-email
		String text = doc.body().text();
		String title = doc.title();
		//System.out.println(text);
		//driver.manage().getCookies();
		//driver.findElement(By.id(str1));
		//driver.findElement(By.id(str1)).sendKeys(str2);


			// GET CONFIG -------
			// Open the file	
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(System.getProperty("user.dir")+"/auth.conf");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				if(strLine.startsWith("[Mail]:")){
					username=strLine.substring(7);
				}
				if(strLine.startsWith("[Pass]:")){
					pass=strLine.substring(7);
				}
				  
			}

			//Close the input stream
			br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			driver.findElement(By.id("login-email")).sendKeys(username);
			driver.findElement(By.id("login-password")).sendKeys(pass);
			driver.findElement(By.id("login-submit")).click();
			username="";
			pass="";

	}
	
	public static void SSExit(){
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("[INFO]:PROBLEM exit was interrupted");
		} 
		driver.close();
		driver.quit();
	}

	public static void Visit(String userURL, boolean humanmode){
            System.out.println("");
            System.out.println("Now browsing https://www.linkedin.com"+userURL);
		try {
			if(humanmode){
				int r = ThreadLocalRandom.current().nextInt(29*1000, 65*1000 + 1);
				System.out.println("Decided to wait "+r+"ms");
                                Thread.sleep(r);
			}else{
				Thread.sleep(6000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("[INFO]:PROBLEM visit was interrupted");
		} 
		driver.get("https://www.linkedin.com"+userURL);//1st time
		
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("[INFO]:PROBLEM visit was interrupted");
		} 
		//start visiting people
		Elements elements = doc.select("body");
		Elements elee = doc.select("h3");
		for (Element el : elements) {
			//System.out.println(el.toString());
			
		}
                //se puede borrar
		//System.out.println();
		for (Element el : elements) {
			if(el.className().contains("pv-browsemap")){
				System.out.println(" emberfound:"+el.className());
				
			}
                        
		String bodyAfter = driver.findElement(By.tagName("body")).getAttribute("innerHTML");
			//System.out.println();
			//System.out.println("PART 2");
			//System.out.println(bodyAfter);
			
			//System.out.println(el.className());
			
			//INTENTO SACAR EL HREF DE LOS <A> DEL NUEVO DOC PERO EL SELECT NO PILLA LOS <A> QUE SON  (CON JSOUP)
			//ember2866
			/*
			Document dAfter=Jsoup.parse(bodyAfter);
			System.out.println("gonna click");
			Elements aels = doc.select("a");
			for (Element ele : aels) {
				//System.out.println(el.toString());
				//System.out.println(ele.attr("href"));
				if (ele.attr("href").startsWith("")) {
					System.out.println("FOUNF"+ele.attr("href"));
				}
					System.out.println("Z");
			}*/
			//INTENTO MANUAL
			Pattern pattern = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");		
	        Matcher matcher = pattern.matcher(bodyAfter);
	        
	        List<String> l1 = new ArrayList<String>(); //list one
	       
	         
	        while (matcher.find()) {
	            //System.out.println("Found: " + matcher.group());
	            if(matcher.group().contains("in/")){
	            	//System.out.println("added");
	            	l1.add(matcher.group());
	            }
	        }
	        //System.out.println("");
	        for(String s:l1){
	        	String s2=s.substring(s.indexOf("/"), s.lastIndexOf("/"));
	        	if(!lf.contains(s2) && occurrences(s2, "/", true)==2){
		        	//System.out.println(s2);
		        	lt.add(s2);
		        	lf.add(s2);
		        	
	        	}
	        }
	        l1 = new ArrayList<String>();//list one back
			
			//FIN IM
	        writeToFile(" [visited]: "+userURL);
				
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://www.linkedin.com";
		/*String test = "<a talhref=\"/in/damien-bon-2b662b/\" id=\"ember2812\">"
				+ "<a talhref=\"/in/damien-DOS-2b662b/\" id=\"ember2812\">"
				+ "<a talhref=\"/in/TRI-2b662b/\" id=\"ember2812\">";
		String test2 = "awodimao href= \"/in/damien-bon-2b662b/485";
		//url="https://accounts.google.com/ServiceLogin?continue=#identifier";
		//url = "https://venta.renfe.com/vol/datosUsuario.do";
		//url = "https://signup.live.com/?uaid=57d28ba48f9f4df49f5e8136eb79cade&lic=1";
		*/
                boolean humanmode=false;
                Scanner scan = new Scanner(System.in);
                System.out.println(" /$$       /$$           /$$       /$$$$$$$$ /$$     /$$     /$$          \n" +
                                    "| $$      |__/          | $$      | $$_____/| $$    | $$    |__/          \n" +
                                    "| $$       /$$ /$$$$$$$ | $$   /$$| $$     /$$$$$$ /$$$$$$   /$$ /$$$$$$$ \n" +
                                    "| $$      | $$| $$__  $$| $$  /$$/| $$$$$ |_  $$_/|_  $$_/  | $$| $$__  $$\n" +
                                    "| $$      | $$| $$  \\ $$| $$$$$$/ | $$__/   | $$    | $$    | $$| $$  \\ $$\n" +
                                    "| $$      | $$| $$  | $$| $$_  $$ | $$      | $$ /$$| $$ /$$| $$| $$  | $$\n" +
                                    "| $$$$$$$$| $$| $$  | $$| $$ \\  $$| $$$$$$$$|  $$$$/|  $$$$/| $$| $$  | $$\n" +
                                    "|________/|__/|__/  |__/|__/  \\__/|________/ \\___/   \\___/  |__/|__/  |__/");
                System.out.println("author: Santiago Rodriguez");
                System.out.println("");
                System.out.println("Welcome to LinkEttin."); 
                System.out.println("Example URL 1: https://www.linkedin.com/in/antonio-garcia-26035aa8/?ppe=1");
                System.out.println("Example URL 2: /in/antonio-garcia-26035aa8/?ppe=1");
                System.out.print("Paste URL from any linkedin user to start: ");
                String first=scan.nextLine();
                
                selUp(url, true);
		soupUp(url, true);
		login();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                writeToFile("New Linkettin started at "+dtf.format(now));
                
                if(first.startsWith("https://www.linkedin.com/in/")){
                    
                    Visit(first.substring(first.indexOf(".com")+4, first.length()),humanmode);
                }else if(first.startsWith("/in/")){
                    Visit(first,humanmode);
                }else{
                    Visit("/in/antonio-garcia-26035aa8/?ppe=1",humanmode);
                }	
		
		for(int i=0; i<lf.size(); i++){
			Visit(lf.get(i),humanmode);
		}
		
		//System.out.println(occurrences("https://signup.live.com/?uaid=57d28ba48f9f4df49f5e8136eb79cade&lic=1/sig", "/", true));
		
		
		
		for(String s:lt){
			Visit(s,humanmode);
		}
		synchronized(lf) {
		      Iterator i = lf.iterator(); // Must be in synchronized block
		      while (i.hasNext())
		          Visit(i.next().toString(),humanmode);
		  }
		
		//SSExit();
		
		//href="/in/damien-bon-2b662b/"
		/*String abuscar="href=";
		int ocu = test.indexOf(abuscar);
		System.out.println("testing str: "+test);
		System.out.println(ocu);
		System.out.println(test.substring(ocu, test.length()-1));*/
		/*
		System.out.println("RES");
		String str = "ZZZZL <%= dsn %> AFFF <%= AFG %>";
		str=test;
		Pattern pattern = Pattern.compile("href=\"([^\"]*)\" (alt=\"[^\"]*\")>");//"href=(.*?)/"
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
		    System.out.println(matcher.group(1));
		}*/
		
		
		
		
		
		/*
		//WORKING 
		Pattern pattern = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");		
        Matcher matcher = pattern.matcher(test);
        
        List<String> l1 = new ArrayList<String>(); //list one
        List<String> lf = new ArrayList<String>(); //final list
         
        while (matcher.find()) {
            System.out.println("Found: " + matcher.group());
            if(matcher.group().contains("in/")){
            	System.out.println("added");
            	l1.add(matcher.group());
            }
        }
        //System.out.println("");
        for(String s:l1){
        	String s2=s.substring(s.indexOf("/"), s.lastIndexOf("/"));
        	System.out.println(s2);
        	lf.add(s2);
        }
        l1 = new ArrayList<String>();//list one back
        */
	}
	}
