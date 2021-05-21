package com.hrms.testcases;

import com.hrms.pages.AddEmployeePage;
import com.hrms.pages.DashboardPage;
import com.hrms.pages.EmployeeListPage;
import com.hrms.pages.LoginPage;
import com.hrms.utils.CommonMethods;
import com.hrms.utils.ConfigsReader;
import com.hrms.utils.Constants;
import com.hrms.utils.ExcelReading;

import java.util.List;
import java.util.Map;

public class MultipleEmployeeTest extends CommonMethods {

   // @Test(groups = "smoke")
    public void addMultipleEmployees() throws InterruptedException {
        //creating object of pages
        LoginPage login=new LoginPage();
        DashboardPage dash=new DashboardPage();
        AddEmployeePage newEmployee=new AddEmployeePage();
        EmployeeListPage employeeListPage=new EmployeeListPage();
        //login in to HRMS
        login.login(ConfigsReader.getPropertyValue("username"), ConfigsReader.getPropertyValue("password"));
        //open excel file
        List<Map<String, String>> employeeData = ExcelReading.excelIntoListMap(Constants.TESTDATA_FILEPATH,"EmployeeData");
        for (int i=0;i<employeeData.size(); i++) {
            //navigating to add employee page
            dash.PIMButton.click();
            Thread.sleep(2000);
            dash.addEmployeeBtn.click();
            //sending values from excel file and adding new employees
            sendText(newEmployee.firstNameTextBox,employeeData.get(i).get("FirstName"));
            sendText(newEmployee.middleNameTextBox,employeeData.get(i).get("MiddleName"));
            sendText(newEmployee.lastNameTextbox,employeeData.get(i).get("LastName"));
            Thread.sleep(3000);
            System.out.println("employeeData.get(i).get(\"Photograph\")" +employeeData.get(i).get("Photograph"));
            sendText(newEmployee.profilePhoto,employeeData.get(i).get("Photograph"));
            Thread.sleep(3000);
            String employeeIDValue=newEmployee.empIDTextBox.getAttribute("value");
            click(newEmployee.createLoginCheckbox);
            sendText(newEmployee.usernameTextField,employeeData.get(i).get("Username"));
            sendText(newEmployee.password,employeeData.get(i).get("Password"));
            sendText(newEmployee.confirmPasswordField,employeeData.get(i).get("Password"));
            click(newEmployee.saveButton);
            Thread.sleep(2000);
            // driver.findElement(By.linkText("Employee List")).click();
            //Thread.sleep(2000);
        }
    }
}
}
