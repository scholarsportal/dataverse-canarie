package info.scholarsportal.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import info.scholarsportal.service.MillDbService;

@Controller
public class PageController {
	
	@Autowired
	private MillDbService millDbService;
	
	@RequestMapping("/")
	public String showPage() {
		return "index";
	}
	
	@RequestMapping(value = "doc", 
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showDoc() {
	    return "doc";
	}
	
	
	@RequestMapping("doc2")
	public RedirectView docRedirect() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://github.com/duracloud/deployment-docs");
	    return redirectView;
	}
	
	
	@RequestMapping(value = "factsheet",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showFactSheet() {
		return "factsheet";
	}
	
	@RequestMapping(value = "info", 
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showInfoHtml(Model model) {
		System.out.println("Show HTML");
		model.addAttribute("infoMap", getInfo("html"));
		return "info";
	}
	
	
	@RequestMapping(value = "info", 
			method = RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> showInfoJson() {
		System.out.println("Show JSON");
		Map<String, String> info = getInfo("json");
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		jsonMap.putAll(info);
		jsonMap.replace("tags",  getTags());
		return jsonMap;
	}

	
	@RequestMapping(value = "licence",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showLicence() {
		return "licence";
	}
	
	@RequestMapping(value = "provenance",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showProvenance() {
		return "provenance";
	}
	
	@RequestMapping(value = "releasenotes",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showReleaseNotes() {
		return "releasenotes";
	}
	
	@RequestMapping(value = "source",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showSourceCode() {
		return "source";
	}
	
	@RequestMapping(value = "support",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showSupport() {
		return "support";
	}
	
	
	@RequestMapping(value = "stats",
            method = {RequestMethod.GET, RequestMethod.HEAD})
	public ModelAndView showStatsHtml(Model model) {
	    ModelAndView vm = new ModelAndView("stats");
	    Map<String, Object> stats = getStats("html");
	    if (stats.containsKey("error")) {
	        vm.setViewName("error-503");
	        vm.setStatus(HttpStatus.SERVICE_UNAVAILABLE);
	    } else {
            vm.addObject("statsMap", stats);
            model.addAttribute("statsMap", stats);
	    }
	    return vm;
	}

    @RequestMapping(value = "stats", 
            method = RequestMethod.GET,
            produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> showStatsJson() {
        System.out.println("Show JSON");
        Map<String, Object> stats = getStats("json");
        if (stats.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(stats);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(stats);
        }
    }

	@RequestMapping(value = "tryme",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public String showTryMe() {
		return "tryme";
	}
	
	protected Map<String,String> getInfo(String requestType) {
	    Map<String,String> info = new LinkedHashMap<String, String>();
	    Map<String, String> header = new LinkedHashMap<String,String>();
	    header.put("name", "Name");
	    header.put("synopsis", "Synopsis");
	    header.put("version", "Versions");
	    header.put("institution", "Institution");
	    header.put("releaseTime", "Release Time");
	    header.put("researchSubject", "Research Subject");
	    header.put("supportEmail", "Support E-mail");
	    header.put("tags", "Tags");
	    
	    Map<String, String> values = new LinkedHashMap<String, String>();
	    values.put("name", "DuraCloud North");
	    values.put("synopsis", "DuraCloud Canada: Linking Data Repositories to Preservation Storage");
        values.put("version", "1.0");
        values.put("institution", "University of Toronto Libraries, Scholars Portal");
        values.put("releaseTime", "March 2020");
        values.put("researchSubject", "Software and development");
        values.put("supportEmail", "<a href=\"mailto:duracloud@scholarsportal.info\">duracloud@scholarsportal.info</a>");
        values.put("tags", String.join(", ", getTags()));
	    
	    if (requestType.equalsIgnoreCase("json")) {
	        for (String headerKey : header.keySet()) {
	            info.put(headerKey, values.get(headerKey));
	        }
	    } else {
	        for (String headerKey : header.keySet()) {
                info.put(header.get(headerKey), values.get(headerKey));
            }
	    }       
	    return info;
	}
	
	protected List<String> getTags() {
	    List<String> tags = Arrays.asList("cloud storage", "preservation storage", "research data preservation");
	    return tags;
	}
	
	protected Map<String, Object> getStats(String type) {
	    Map<String, Object> stats = new LinkedHashMap<String, Object>();
	    Integer active, deleted = null;
	    
	    Map<String, String> header = new LinkedHashMap<String, String>();
	    header.put("numberOfActiveFiles", "Active Files");
	    header.put("numberOfDeletedFiles", "Deleted Files");
	    header.put("lastReset", "Last Reset");
	    
	    Map<String, String> values = new LinkedHashMap<String, String>();
	    
	    try {
	        values.put("lastReset", millDbService.lastModified());
	        active  =  millDbService.activeFiles();
	        deleted  =  millDbService.deletedFiles();
	        values.put("numberOfActiveFiles", active.toString());
	        values.put("numberOfDeletedFiles", deleted.toString());
	        
	        if (type.equalsIgnoreCase("json")) {
	            for (String hdr : header.keySet()) {
	                stats.put(hdr, values.get(hdr));
	            }
	        } else {
	            for (String hdr : header.keySet()) {
                    stats.put(header.get(hdr), values.get(hdr));
                }
	        }
	        active  =  millDbService.activeFiles();
	        
	    } catch (RuntimeException e) {
	        System.out.println("NOT INTO IT");
	        Map<String, String> err = new LinkedHashMap<String, String>();
	        err.put("code", "503");
	        err.put("message", "Service Unavailable");
	        stats.put("error", err);
	        e.printStackTrace();
	    }
	    return stats;
	}
}
