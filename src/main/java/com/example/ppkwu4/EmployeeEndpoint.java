package com.example.ppkwu4;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeEndpoint {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showHomePage() {
        return new ModelAndView("employee", "employee", new Employee());
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8")
    public String searchEmployee(@Valid @ModelAttribute("employee") Employee employee, ModelMap model) {
        List<Employee> employees = fetchAllEmployeesFromBrowser(employee);

        model.addAttribute("employees", employees);

        return "employeeList";
    }

    @RequestMapping(value = "/vcard", method = RequestMethod.POST)
    public ResponseEntity<Resource> generateVCard(@ModelAttribute("employee") Employee employee)
            throws IOException {
        File file = new File("vcard.vcf");
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        bufferedWriter.write("BEGIN:VCARD\r\n");
        bufferedWriter.write("VERSION:4.0\r\n");
        bufferedWriter.write("N:" + employee.getSurname() + ";" + employee.getName() + ";;" + employee.getTitle() + "\r\n");
        bufferedWriter.write("FN:" + employee.getName() + " " + employee.getSurname() + "\r\n");
        bufferedWriter.write("ORG:" + employee.getPlace() + "\r\n");
        bufferedWriter.write("END:VCARD");
        bufferedWriter.close();

        Resource fileSystemResource = new FileSystemResource(file);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("text/vcard"))
                .body(fileSystemResource);
    }

    private List<Employee> fetchAllEmployeesFromBrowser(Employee employee) {
        try {
            String url = "";
            if (employee.getName().isEmpty()) {
                url = "https://adm.edu.p.lodz.pl/user/users.php?search=" + employee.getSurname();
            } else if (employee.getSurname().isEmpty()) {
                url = "https://adm.edu.p.lodz.pl/user/users.php?search=" + employee.getName();
            } else {
                url = "https://adm.edu.p.lodz.pl/user/users.php?search=" + employee.getName() + "+" + employee.getSurname();
            }

            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("div.user-info");
            Elements names = elements.select("h3");
            Elements titles = elements.select("h4");
            Elements places = elements.select("span.item-content");

            List<Employee> employees = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                Employee employeeToAdd = new Employee();
                employeeToAdd.setTitle(titles.get(i).text());
                employeeToAdd.setPlace(places.get(i).text());
                String[] splittedName = names.get(i).text().split(" ");
                employeeToAdd.setName(splittedName[0]);
                employeeToAdd.setSurname(splittedName[1]);

                employees.add(employeeToAdd);
            }

            return employees;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
