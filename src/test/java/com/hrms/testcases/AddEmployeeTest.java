package com.hrms.testcases;

import com.hrms.pages.*;
import com.hrms.utils.CommonMethods;
import com.hrms.utils.ConfigsReader;
import com.hrms.utils.Constants;
import com.hrms.utils.ExcelReading;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddEmployeeTest extends CommonMethods {

    @Test(groups = "smoke")
    public void addEmployee() {
        //login to the hrms
        LoginPage login = new LoginPage();
        login.login(ConfigsReader.getPropertyValue("username"), ConfigsReader.getPropertyValue("password"));
        //navigate to add employee page
        DashboardPage dash = new DashboardPage();
        jsClick(dash.PIMButton);
        jsClick(dash.addEmployeeBtn);
        //add employee
        AddEmployeePage addEmp = new AddEmployeePage(driver);
        sendText(addEmp.firstNameTextBox, "Daria");
        sendText(addEmp.lastNameTextbox, "Denchuk");
        click(addEmp.saveButton);
        //Assert.assertEquals();
    }

    //@Test(groups = "regression")
    public void addMultipleEmployees() throws InterruptedException {
        LoginPage login = new LoginPage();
        login.login(ConfigsReader.getPropertyValue("username"), ConfigsReader.getPropertyValue("password"));
        // navigate to add employee page
        DashboardPage dashboardPage = new DashboardPage();
        PersonalDetailPage personalDetailPage = new PersonalDetailPage(driver);
        // add employee
        List<Map<String, String>> newEmployees = ExcelReading.excelIntoListMap(Constants.TESTDATA_FILEPATH, "EmployeeData");
        AddEmployeePage addEmployeePage = new AddEmployeePage(driver);
        EmployeeListPage empList = new EmployeeListPage(driver);

        List<String> empIDlist = new ArrayList<>();
        SoftAssert softAssert = new SoftAssert();

        Iterator<Map<String, String>> it = newEmployees.iterator();

        while (it.hasNext()) {
            jsClick(dashboardPage.PIMButton);
            jsClick(dashboardPage.addEmployeeBtn);
            Map<String, String> mapNewEmployee = it.next();
            sendText(addEmployeePage.firstNameTextBox, mapNewEmployee.get("FirstName"));
            sendText(addEmployeePage.middleNameTextbox, mapNewEmployee.get("MiddleName"));
            sendText(addEmployeePage.lastNameTextbox, mapNewEmployee.get("LastName"));
            String employeeIDValue = addEmployeePage.empIDTextbox.getAttribute("value");
            sendText(addEmployeePage.photograph, mapNewEmployee.get("Photograph"));
            //click on checkbox
            if (!addEmployeePage.createLoginDetails.isSelected()) {
                addEmployeePage.createLoginDetails.click();
            }
            //add login credentials
            sendText(addEmployeePage.usernameCreate, mapNewEmployee.get("Username"));
            sendText(addEmployeePage.userPassword, mapNewEmployee.get("Password"));
            sendText(addEmployeePage.rePassword, mapNewEmployee.get("Password"));
            click(addEmployeePage.saveButton);
            //navigate to the employee list
            jsClick(dashboardPage.PIMButton);
            jsClick(dashboardPage.employeeList);
            //enter employee id
            waitForClickability(empList.idEmployee);
            sendText(empList.idEmployee, employeeIDValue);
            click(empList.searchBtn);

            List<WebElement> rowData = driver.findElements(By.xpath("//table[@id = 'resultTable']/tbody/tr"));

            for (int i = 0; i < rowData.size(); i++) {
                System.out.println(" ---- I am inside the the loop -------------");
                String rowText = rowData.get(i).getText();

                System.out.println(rowText);
                String expectedEmployeeDetails = employeeIDValue + " " + mapNewEmployee.get("FirstName") + " " + mapNewEmployee.get("MiddleName") + " " + mapNewEmployee.get("LastName");

                softAssert.assertEquals(rowText, expectedEmployeeDetails, "Data is NOT matched");

            }
        }
        softAssert.assertAll();
    }
}

