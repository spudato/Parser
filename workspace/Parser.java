import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Object.*;
import java.nio.file.*;
//import com.google.common.net.*;

public class Parser {


    /*

    Later these variables should be read from a config file and FILENAME will be replaced as it will be dynamic based on a directory listing

    */
    // not actually used now.
    private static final String FILENAME = "LT029982a-domainfw.log";
    //
    private static final String DHCP_CSV = "DHCP";
    private static final Path MYDIR = Paths.get(".");
    private static List<Path> files = new ArrayList<Path>();
		private static Properties prop = new Properties();
		private static File propfile = new File("config.properties");
	
	 /**
 * write the config.properties file
 *
 * @param  
 * @return 
 
 */
	 private static void writeProps() 
	 
	 {

			Properties prop = new Properties();
			OutputStream output = null;

			try {

					output = new FileOutputStream("config.properties");

					// set the properties value
					prop.setProperty("DHCP_CSV", "dhcp_file_location");
					prop.setProperty("LOG_DIR", "log_directory");
					//prop.setProperty("dbpassword", "password");

					// save properties to project root folder
					prop.store(output, null);

			} catch (IOException io) {
				io.printStackTrace();
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
 	 }
	
	
 /**
 * read the config.properties file
 *
 * @param  
 * @return 
 
 */
	
	 private static void readProps() 
	 
	 {
			InputStream targetStream = null;
    	try {
				//InputStream input = null;
		  	//FileInputStream input = new FileInputStream("config.properties");
				targetStream = new FileInputStream(propfile);
				
				if(!propfile.exists()) {
					System.out.println("Sorry, unable to find " + propfile);
					return;
				}
	   		//load a properties file from class path, inside static method
    		prop.load(targetStream);
				
				} catch (Exception ex) {
					ex.printStackTrace();
        } finally{
        	if(targetStream!=null){
        		try {
							targetStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
        	}
        }

    }

	
	
	
	/**
 * Given an IP converts it to a long, which is useful for comparing IPs
 *
 * @param IP an ip address 
 * @return long the IP converted to a long
 
 */

    private static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

/**
 * Given a String it will check it is in a valid IP format
 *
 * @param  String an IP address
 * @return Boolean whether it is an IP or not
 
 */

    private static boolean isIP(String ip) {
        if(ip == null || ip.length() < 7 || ip.length() > 15) return false;

        try {
            int x = 0;
            int y = ip.indexOf('.');

            if (y == -1 || ip.charAt(x) == '-' || Integer.parseInt(ip.substring(x, y)) > 255) return false;

            x = ip.indexOf('.', ++y);
            if (x == -1 || ip.charAt(y) == '-' || Integer.parseInt(ip.substring(y, x)) > 255) return false;

            y = ip.indexOf('.', ++x);
            return  !(y == -1 ||
                    ip.charAt(x) == '-' ||
                    Integer.parseInt(ip.substring(x, y)) > 255 ||
                    ip.charAt(++y) == '-' ||
                    Integer.parseInt(ip.substring(y, ip.length())) > 255 ||
                    ip.charAt(ip.length()-1) == '.');

        } catch (NumberFormatException e) {
            return false;
        }
    }
	
 /**
 * Given a BufferedReader and String separator  will parse a file into a StrinArray
 *
 * @param  BufferedReader from a filereader forr example
 * @param  String separator in the file e.g. comma, tab etc
 * @return  StringArray the file contents 
 
 */

    private static String[][] readMatrix(BufferedReader bfr, String separator) throws Exception {
        List<String[]> rows = new ArrayList<String[]>();
        for (String s = bfr.readLine(); s != null; s = bfr.readLine()) {
            //skip commented rows
            if (! s.trim().startsWith("#")) {
                String items[] = s.split(separator);
                String[] row = new String[items.length];
                for (int i = 0; i < items.length; ++i) {
                    row[i] = (items[i]);
                }
                rows.add(row);
            }
        }
        return rows.toArray(new String[rows.size()][]);
    }

		/**
 * Given a csv it will return a string array 
 *
 * @param  String A file name
 * @return  StringArray array of strings  
 
 */
	
    private static String[][] readFile(String file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader (file));
        String line = null;
        String[][] IP = null;
        String csvSplitBy = ",";
        int i = 0;

        try {
            while ((line = br.readLine()) != null) {
                //System.out.println(IP[3]);
            }
            return IP;
        } finally {
            br.close();
        }
    }

	/**
 * Given a directory path it returns a list of logs in that directory
 * Could add an extra param I guess to take the file type instead of hardcoding *.log suffix
 *
 * @param  Path Directory you want a file listing for
 * @return  <List>Paths 
 
 */

    private static List<Path> getDirList(Path dir_path) throws IOException {

        List<Path> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir_path, "*.log")) {
            for (Path entry: stream) {
                result.add(entry);
            }
        } catch (DirectoryIteratorException ex) {
            // I/O error encounted during the iteration, the cause is an IOException
            throw ex.getCause();
        }
        return result;
    }

	
	
	
	
	
	/**
 * Main function parsers DHCP file and compare src and dst file to see connection within the DHCP range
 * Run as:
 * java Parser > outputfile.txt
 *
 * @param  none1 blabd
 * @param  none2 Blah2
 * @return      nowt
 
 */

    public static void main(String[] args) {


/*

Read in the DHCP details. These will need to be parsed, though perhaps that can simply be done beforehand and then the format assumed.
*/

        try {
					
						//check for propfile first
					  File props = new File("config.properties");
						if(!props.exists()) {
								System.out.println("Sorry, unable to find " + props + ". Will create a properties file now. Please edit as appropriate and then rerun\n");
								writeProps();
								System.exit(0);
						}
					
						//read the propfile
						readProps();
						System.out.println(prop.getProperty("DHCP_CSV"));
						System.exit(0);
						
																
            BufferedReader br = new BufferedReader(new FileReader (DHCP_CSV));
            String [][] dhcp = readMatrix(br, ",");
            //Need to load these into an array for looping later
            files = getDirList(MYDIR);
            //System.out.println(getDirList(MYDIR));
            Iterator<Path> ListIterator = files.iterator();
            while (ListIterator.hasNext()) {
                //Now loop through reading each file
                //Read the logfile
                BufferedReader bfr = new BufferedReader(new FileReader ((ListIterator.next()).toFile()));
                String [][] logfile = readMatrix(bfr, " ");
                for(int i=0; i< logfile.length; i++) {
                    for (int j = 0; j < logfile[i].length; j++) {
                        //
                    }
		    bfr.close();
                    //check it is an IP and don't waste time if it isn't
                    if ((isIP(logfile[i][5]) && isIP(logfile[i][6]))) {
                        //if (!(InetAddress.getByName(logfile[i][5]).isLoopbackAddress() || InetAddress.getByName(logfile[i][6]).isLoopbackAddress())) {
			// Dump the loopback
			if (!((logfile[i][8]).trim().equals("53"))) {
				if (!(InetAddress.getByName(logfile[i][6]).isLoopbackAddress())) {
				    for (int a = 0; a < dhcp.length; a++) {
					for (int b = 0; b < dhcp[a].length; b++) {
					    long ipLo = ipToLong(InetAddress.getByName(dhcp[a][0]));
					    long ipHi = ipToLong(InetAddress.getByName(dhcp[a][1]));
					    //long srcIP = ipToLong(InetAddress.getByName(logfile[i][5]));
					    long dstIP = ipToLong(InetAddress.getByName(logfile[i][6]));
					    if (dstIP >= ipLo && dstIP <= ipHi) {
						System.out.println(Arrays.deepToString(logfile[i]).replace("], ", "]\n"));
					    } // End if
					} // End for loop b
				    } //End for loop a
				} //End loopback check
			} // End DNS check
                    } // End for loop i
                } // End if
            } // End for loop j
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}
