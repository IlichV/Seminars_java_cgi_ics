import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GenIcs {

    public static String dateIcs (String dateSeminar){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy",Locale.ENGLISH);
        LocalDate dateTime = LocalDate.parse(dateSeminar, formatter);
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'"));
        return formattedDate;
    }
	
	    
public static void main(String[] args) throws IOException {
	Locale.setDefault(Locale.FRANCE);
    String type = "Content-type: text/html; charset=utf-8\n\n";
   // System.out.print(type);
    String url = "https://www.lacl.fr/en/seminar/";
    Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8",url);
   
    
    final String compBegin = "BEGIN:VCALENDAR\n";
    final String compEnd = "END:VCALENDAR\n";
    final String compVersion = "VERSION:2.0\n";
    final String compScale = "CALSCALE:GREGORIAN\n";
    final String compBeginEvnt = "BEGIN:VEVENT\n";
    final String eolCal = "\\n";
    final String compEndEvnt= "END:VEVENT\n";
    final String compLoc= "LOCATION: University Paris-Est Créteil\n";
    final String compStat = "STATUS:CONFIRMED\n";
    
    System.out.write(compBegin.getBytes());
    System.out.write(compVersion.getBytes());
    System.out.write(compScale.getBytes());

    for (Element postPage : doc.select("div.content-area article")) {
        String place =  postPage.select("h7:nth-of-type(2)").text();
  
        String speakerSem  =  "("+postPage.select("h7 strong").text()+ ") ";
        String titleSem =  postPage.select(".entry-title").text()+ " ";	
        String contentSem = "DESCRIPTION:"+ titleSem + eolCal + speakerSem  + place + eolCal + postPage.select(".entry-content").text()+"\n";
        String dateSem =  postPage.select("h3").text();
        String sem_dateStart = "DTSTART;TZID=Europe/Paris:" + dateIcs(dateSem)+"140000"+"\n" ;
        String sem_dateEnd = "DTEND;TZID=Europe/Paris:" +  dateIcs(dateSem)+"150000"+"\n";
        String compSmry = "SUMMARY:Séminaire LACL: "+ titleSem + speakerSem+"\n" ;
        
       
        String selecElem[] = {compBeginEvnt,sem_dateStart,sem_dateEnd,compSmry, compLoc,contentSem,compStat,compEndEvnt};
        	for (String str : selecElem ) {
        		System.out.write(str.getBytes("UTF-8"));
			}
        }
    System.out.write(compEnd.getBytes());
    }
  
}

