package com.vass.ip;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.appiancorp.services.ServiceContext;
import com.appiancorp.suiteapi.cfg.ConfigurationLoader;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;

import com.appiancorp.suiteapi.expression.annotations.Category;

@Category("category.name.AppianScriptingFunctions")

public class getActualIpss {
	
	

	  private String[] fileNameList() {
	    String[] filenames = new String[33];
	    filenames[0] = "login-audit.csv";
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
	    Calendar cal = Calendar.getInstance();

	    // get starting date
	    Calendar.getInstance().getTime();

	    // loop adding one day in each iteration
	    for (int i = 1; i < 33; i++) {
	      cal.add(Calendar.DAY_OF_MONTH, -1);
	      filenames[i] = ("login-audit.csv." + sdf.format(cal.getTime()));

	    }

	    return filenames;
	  }
	
	
	
	  @Function
	  public String getIp(ServiceContext sc, @Parameter String userName) {

		    String[] files = fileNameList();
		   
		    String lastLogin = null;
		    BufferedReader br = null;
		    FileInputStream fis = null;
		    // Get the user login details log file path.
		    String logFilePath = ConfigurationLoader.getConfiguration().getAeLogs();

		    for (int i = 0; i < files.length; i++) {

		      if (lastLogin != null) {
		        break;
		      } else {
		        String filename = logFilePath + File.separator + files[i];

		        // check if file exist
		        Path path = Paths.get(filename.trim());
		        if (path.toFile().exists()) {

		          try {

		            // Read the login file
		            fis = new FileInputStream(filename.trim().toString());
		            br = new BufferedReader(new InputStreamReader(fis));
		            List<String> loginList = new ArrayList<String>();
		            String s;

		            while ((s = br.readLine()) != null) {
		              loginList.add(s);

		            }
		            Collections.reverse(loginList);
		            // Check for the user last login details
		            for (String a : loginList) {
		              String LL[] = a.split(",");

		              if (LL[1].trim().equals(userName.trim()) && LL[2].trim().equals("Succeeded")) { 
		                lastLogin = LL[3];
		                break;
		              }
		            }
		          }

		          catch (FileNotFoundException e) {

		            return "Logs not found";
		          } catch (IOException e) {
		        	  return ("Error encounter");
		          } finally {
		            try {
		              if (fis != null)
		                fis.close();
		              if (br != null)
		                br.close();
		            } catch (Exception e) {
		            	

		            }
		          }

		        } else {

		        }
		      }

		    }

		    return lastLogin;
		  }
        }
    
    

