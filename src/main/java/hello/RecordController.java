package hello;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller("/record")
public class RecordController {

  @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @GetMapping("/addRecord")
    public String RecordForm(Model model) {
        model.addAttribute("record", new Record());
        return "addRecord";
    }

    @PostMapping("/addRecord")
    public String RecordSubmit(@ModelAttribute Record record) {
    	
        jdbcTemplate.update("insert into lshoemake.recordvisit values (?, ?, ?, ?, ?, ?, ?, ?, ?)", 
        		record.getRecordNum() == 0 ? "NULL" : record.getRecordNum(), 
        		record.getApptNum(), record.getInitialHospDate(), 
        		record.getExpDischargeDate(), record.getActualDischargeDate(), 
        		record.getReason(), record.getTreatmentMethod(), 
        		record.getHospRoom() == 0 ? "NULL" : record.getHospRoom(),
        		record.getDID() == 0 ? "NULL" : record.getDID());

        return "resultRecord";
    }

    @GetMapping("/updateRecord")
    public String RecordFormUpdate(Model model) {
        model.addAttribute("record", new Record());
        return "updateRecord";
    }

    @PostMapping("/updateRecord") //FIX
    public String RecordUpdate(@ModelAttribute Record record) {
        
        List<String> strs = new ArrayList<String>();
        
    	if (record.getRecordNum() != 0)
    		strs.add("recordnum = " + record.getRecordNum());
    	if (record.getExpDischargeDate() != null)
    		strs.add("expDischargeDate = '" + record.getExpDischargeDate() + "'");
    	if (record.getActualDischargeDate() != null)
    		strs.add("actualDischargeDate = '" + record.getActualDischargeDate() + "'");
    	if (record.getTreatmentMethod() != null)
    		strs.add("treatmentMethod = '" + record.getTreatmentMethod() + "'");
    	if (record.getHospRoom() != 0)
    		strs.add("hosproom = " + record.getHospRoom());
    	if (record.getDID() != 0)
    		strs.add("did = " + record.getDID());
 
    	
    	String stmt = String.format("update lshoemake.recordvisit set %s where recordnum = %s",
    			String.join(", ", strs), record.getRecordNum());
        jdbcTemplate.update(stmt);
        
        

        return "updateRecordResult";
    }

}
