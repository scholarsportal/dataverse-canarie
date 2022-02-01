package info.scholarsportal.controller;

import java.util.Arrays;
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
import info.scholarsportal.util.PlatformUtil;

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
	public RedirectView showDoc() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://learn.scholarsportal.info/all-guides/dataverse/");
	    return redirectView;
	}	
	
	@RequestMapping(value = "factsheet",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public RedirectView showFactSheet() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://dataverse.scholarsportal.info/metrics/?selection=");
	    return redirectView;
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
	public RedirectView showLicence() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://learn.scholarsportal.info/all-guides/dataverse/terms-of-use/");
	    return redirectView;
	}
	
	@RequestMapping(value = "provenance",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public RedirectView showProvenance() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://spotdocs.scholarsportal.info/display/DAT/Provenance");
	    return redirectView;
	}
	
	@RequestMapping(value = "releasenotes",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public RedirectView showReleaseNotes() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://github.com/scholarsportal/dataverse/blob/dataverse-v5.8-SP/doc/release-notes/5.8-release-notes.md");
	    return redirectView;			
	}
	
	@RequestMapping(value = "source",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public RedirectView showSourceCode() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://github.com/scholarsportal/dataverse/tree/dataverse-v5.8-SP");
	    return redirectView;
	}
	
	@RequestMapping(value = "support",
			method = {RequestMethod.GET, RequestMethod.HEAD})
	public RedirectView showSupport() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://learn.scholarsportal.info/all-guides/dataverse/help/");
	    return redirectView;	
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
	public RedirectView showTryMe() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl("https://demodv.scholarsportal.info/index.html");
	    return redirectView;
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
	    values.put("name", "Dataverse");
	    values.put("synopsis", "Dataverse for the Canadian Research Community");
        values.put("version", PlatformUtil.getVersion());
        values.put("institution", "University of Toronto Libraries, Scholars Portal");
        values.put("releaseTime", PlatformUtil.getReleaseTime());
        values.put("researchSubject", "Software and development");
        values.put("supportEmail", "dataverse@scholarsportal.info");
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
	    List<String> tags = Arrays.asList("Research Data Management");
	    return tags;
	}
	
	protected Map<String, Object> getStats(String type) {
	    Map<String, Object> stats = new LinkedHashMap<String, Object>();
	    Integer downloadsCount = null;
	    
	    Map<String, String> header = new LinkedHashMap<String, String>();
	    header.put("numberOfDownloads", "Number of Downloads");
	    header.put("lastReset", "Last Reset");
	    
	    Map<String, String> values = new LinkedHashMap<String, String>();
	    
	    try {
	    	downloadsCount = millDbService.getNumberOfDownloads();	        
	        values.put("numberOfDownloads", downloadsCount.toString());
	        values.put("lastReset", PlatformUtil.getLastReset());
	        
	        if (type.equalsIgnoreCase("json")) {
	            for (String hdr : header.keySet()) {
	                stats.put(hdr, values.get(hdr));
	            }
	        } else {
	            for (String hdr : header.keySet()) {
                    stats.put(header.get(hdr), values.get(hdr));
                }
	        }
	    } catch (RuntimeException e) {
	        Map<String, String> err = new LinkedHashMap<String, String>();
	        err.put("code", "503");
	        err.put("message", "Service Unavailable");
	        stats.put("error", err);
	        e.printStackTrace();
	    }
	    return stats;
	}
}
