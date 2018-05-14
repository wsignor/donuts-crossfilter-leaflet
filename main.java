import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    public static void main(String[] args) {
        String csvFileFee = "C:/Users/Wagner/WebstormProjects/donuts-crossfilter-leaflet/DadosFEEE-2016.csv";
        String csvFileMun = "C:/Users/Wagner/WebstormProjects/donuts-crossfilter-leaflet/municipios-2016.csv";

        BufferedReader brFee = null;
        BufferedReader brMun = null;
        String cvsSplitBy = ",";

        try {

            //brFee = new BufferedReader(new FileReader(csvFileFee));
            brMun = new BufferedReader(new FileReader(csvFileMun));

            Scanner scannerFee = new Scanner(new File(csvFileFee));

            while (scannerFee.hasNext()) {
                List<String> lineFee = parseLine(scannerFee.nextLine());

                //System.out.println("Line fee " + lineFee.get(1));
                Scanner scannerMun = new Scanner(new File(csvFileMun));
                while (scannerMun.hasNext()) {
                    List<String> lineMun = parseLine(scannerMun.nextLine());
                    //System.out.println("Line mun " + lineMun);
                    if(lineMun.get(2).equalsIgnoreCase(lineFee.get(1))){
                        System.out.println("Municipio deu match: " + lineFee.get(1));
                    }

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (brFee != null) {
                try {
                    brFee.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

}