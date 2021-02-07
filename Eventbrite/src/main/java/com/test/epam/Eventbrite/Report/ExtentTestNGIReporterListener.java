package com.test.epam.Eventbrite.Report;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentTestNGIReporterListener implements IReporter, ITestListener, Serializable {

	public String OUTPUT_FOLDER = "TEST-OUTPUT" + File.separator;
	public String FILE_NAME = "AutomationTestReport";
	public ExtentReports extent;
	public String testNgFile;
	public String browser = null;
	public String failureIsInMethodMsg="Failure is in the method --> ";
	public String retryFailureMsg="Retrying Failure! <br> ";
	public String methodDependencyMsg="Method Dependency Failure! <br>Possible test failures are: <br>";
	public HashMap<String, Throwable> allSkipData = new HashMap<String, Throwable>();
	
	
	public String description;

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		init();

		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();
			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();

				buildTestNodes(context.getFailedTests(), Status.FAIL);
				buildTestNodes(context.getSkippedTests(), Status.SKIP);
				buildTestNodes(context.getPassedTests(), Status.PASS);

			}
		}

		for (String s : Reporter.getOutput()) {
			extent.setTestRunnerOutput(s);
		}

		extent.flush();

		new File(OUTPUT_FOLDER + FILE_NAME + ".html")
				.renameTo(new File(OUTPUT_FOLDER + FILE_NAME + System.currentTimeMillis() + ".html"));
	}

	private void init() {

		File directory = new File(OUTPUT_FOLDER);
		if (!directory.exists()) {
			directory.mkdir();
		}

		String nameOS = "os.name";
		String versionOS = "os.version";
		String architectureOS = "os.arch";

		nameOS = System.getProperty(nameOS);
		versionOS = System.getProperty(versionOS);
		architectureOS = System.getProperty(architectureOS);

		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME + ".html");

		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.setAppendExisting(true);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setReportUsesManualConfiguration(true);

		extent.setSystemInfo("Operating System", nameOS);
		extent.setSystemInfo("Version", versionOS);
		extent.setSystemInfo("OS Arch", architectureOS);
		extent.setSystemInfo("Browser", browser);

	}

	private void buildTestNodes(IResultMap tests, Status status) {
		ExtentTest test;
		ExceptionFormatter exceptionFormatter = new ExceptionFormatter();
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				test = extent.createTest(result.getMethod().getMethodName()).assignCategory(
						"qTest: " + result.getTestClass().getName() + "." + result.getMethod().getMethodName());

				if (result.getMethod().getDescription() != null)
					test.getModel().setDescription(result.getMethod().getDescription());

				test.getModel().setStartTime(getTime(result.getStartMillis()));
				test.getModel().setEndTime(getTime(result.getEndMillis()));

				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);

				if (result.getThrowable() != null) {

					// check if this can be >=0
					if (result.getStatus() == ITestResult.SKIP) {

						if (result.getTestContext().getFailedConfigurations().size() > 0) {

							Boolean applicationIssue = false;
							try {
								if (result.getThrowable().getMessage().contains("Application Issue")) {
									applicationIssue = true;
								}
							} catch (Exception e) {
								applicationIssue = false;
							}

							if (applicationIssue == true) {
								test.log(status.ERROR, "Retrying Failure! <br> "
										+ exceptionFormatter.toHtml(exceptionFormatter.format(result.getThrowable())));
							} else {
								
								String errorTrace=result.getThrowable().toString();
								
								if(errorTrace.contains("org.openqa.selenium.SessionNotCreatedException")) {
									test.log(status,
											"<br>" + "Session not created due to incompatible driver.  "
													+"Chrome version on slave is ---> "
													+"<u>"+(new ExtentTestNGIReporterListener()).fetchChromeVersion()+"</u><br>"
													+ exceptionFormatter.toHtml(exceptionFormatter.format(
															result.getThrowable())));
								}
								else if(errorTrace.contains("The driver executable does not exist")) {
									test.log(status,
											"<br>" + "Driver not found"+ "<br>"
													+ exceptionFormatter.toHtml(exceptionFormatter.format(
															result.getThrowable())));
								}
								
								else {
									StackTraceElement[] stckTrace=result.getThrowable().getStackTrace();
									
									String strTrace="";
									for(int trace=0; trace<stckTrace.length; trace++) {
										if(stckTrace[trace].toString().contains("com.apttus.sfdc")||
												stckTrace[trace].toString().contains("com.apttus.aic") ||
												stckTrace[trace].toString().contains("com.conga")
												) {
											strTrace = strTrace+stckTrace[trace].toString()+"<br>";
										}
										}
								test.log(status,
										"<br>" + failureIsInMethodMsg
												+ strTrace + "<br>"
												+ exceptionFormatter.toHtml(exceptionFormatter.format(
														result.getThrowable())));
								}
							}
						}

						else if (result.getThrowable().getMessage().contains("Application Issue")) {
							test.log(status.ERROR, retryFailureMsg
									+ exceptionFormatter.toHtml(exceptionFormatter.format(result.getThrowable())));
						}

						else if (result.getThrowable().getMessage()
								.contains("depends on not successfully finished methods")) {

							String methodsDependedOn = "";
							for (int i = 0; i < result.getMethod().getMethodsDependedUpon().length; i++) {
								methodsDependedOn = methodsDependedOn + result.getMethod().getMethodsDependedUpon()[i]
										+ "<br>";
							}

							test.log(status, methodDependencyMsg
									+ methodsDependedOn + "<br>"
									+ exceptionFormatter.toHtml(exceptionFormatter.format(result.getThrowable())));
						}
					}

					else if (result.getStatus() == ITestResult.FAILURE) {
						test.log(status, exceptionFormatter.toHtml(exceptionFormatter.format(result.getThrowable())));
					}

				} else {
					if (result.getStatus() == ITestResult.SKIP) {
							if(allSkipData.containsKey(result.getTestClass().getName() + "." + result.getMethod().getMethodName())) {
							
							test.log(status,
											exceptionFormatter.toHtml(
													exceptionFormatter.format(
															allSkipData.get(result.getTestClass().getName() + "." + result.getMethod().getMethodName()))));
						      						}
							
							else {
								test.log(status,"Test "+ status.toString().toLowerCase()+"ed");
							}
					}

					else if (result.getStatus() == ITestResult.SUCCESS) {
						test.log(status, "Test " + status.toString().toLowerCase() + "ed");
					}
				}
				test.getModel().setStartTime(getTime(result.getStartMillis()));
				test.getModel().setEndTime(getTime(result.getEndMillis()));

			}
		}

	}

	public String getTestClassName(String testName) {
		String[] reqTestClassname = testName.split("\\.");
		int i = reqTestClassname.length - 1;
		return reqTestClassname[i];
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	@Override
	public void onTestStart(ITestResult result) {
	}

	@Override
	public void onTestSuccess(ITestResult result) {

	}

	@Override
	public void onTestFailure(ITestResult result) {

		try {
			
			String name = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
			if(result.getThrowable().toString().contains("java.lang.AssertionError"))
			{
				Reporter.log("<span id =\"" + name + "\">Assertion Error: </span><br/>" +
							result.getThrowable().toString()
								.substring(result.getThrowable().toString().indexOf(":")+1, result.getThrowable().toString().length()));
			}
			
			else if (result.getThrowable() != null) {

				if (result.getThrowable().getMessage().contains("Exception:")
						&& result.getThrowable().getMessage().contains("Detailed Trace:")) {

					String error = result.getThrowable().getMessage().substring(0,
							result.getThrowable().getMessage().indexOf("Detailed Trace:"));
				
					Reporter.log("<span id =\"" + name + "\">" + error + "</span><br/>");
				}

			}
		} catch (NullPointerException e) {
		}

	}


	@Override
	public void onTestSkipped(ITestResult result) {
		
		if(result.getThrowable()==null) {
		IResultMap iresultmap = result.getTestContext().getFailedConfigurations();
		Throwable skipResult = iresultmap.getAllResults().iterator().next().getThrowable();
		String testMethodName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
		
		Iterator<ITestResult> itrResults = iresultmap.getAllResults().iterator();
		while(itrResults.hasNext()) {
			ITestResult nextResult = itrResults.next();
			StackTraceElement[] stckTrace=nextResult.getThrowable().getStackTrace();
			for(int i=0; i<stckTrace.length; i++) {
				if(stckTrace[i].toString().contains(result.getTestClass().getName())) {
					  allSkipData.put(testMethodName, nextResult.getThrowable());	  
					  break;
					}
			}
		}
		
		}
		
		

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStart(ITestContext context) {

	}

	@Override
	public void onFinish(ITestContext context) {
	}
	
	
	public String fetchChromeVersion() {
		ArrayList<String> output = new ArrayList<String>();
		String chVersion = null;
		try {
			Process p = Runtime.getRuntime().exec("wmic datafile where name=\"C:\\\\Program Files (x86)\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe\" get Version /value");
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((chVersion = stdInput.readLine()) != null) {
		        output.add(chVersion);
		    }
		 
			for(String arrOutput : output) {
				if(arrOutput.contains("Version=")) {
					chVersion=arrOutput;
					break;
				}
			}
			
			
		} catch (Exception e) {
		}
		return chVersion;
	}
	
	
	
	

}
