package com.samsonan.counters.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.samsonan.counters.domain.Counter;
import com.samsonan.counters.domain.CounterType;
import com.samsonan.counters.dto.CounterForm;
import com.samsonan.counters.service.CounterValuesService;
import com.samsonan.counters.service.exception.RemoteNotFoundException;

/**
 * This controller is used to process UI requests, for REST API requests see {@link com.samsonan.counters.web.CounterRestController} 
 */
@Controller
public class CounterController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(CounterController.class);

    private CounterValuesService counterService;

    private final String EDIT_MODE_ADD = "ADD";
    private final String EDIT_MODE_EDIT = "EDIT";

    @Autowired
    public CounterController(CounterValuesService counterService) {
        this.counterService = counterService;
    }

    @GetMapping({ "", "/", "/counters", "/list" })
    public String list(@RequestParam(name = "type", required = false) Integer[] type, Model model) {

        populateCounters(type, model);
        
        return "counter/list";
    }

    /**
     * This method is used when we apply filters - we GET the PART (fragment) of page content as HTML using ajax and simply replacing the data on the page.
     * The page header/footer and filters are not affected. 
     */
    @GetMapping({ "/list/fragment" })
    public String requestFragment(@RequestParam(name = "type", required = false) Integer[] type, Model model) {

        populateCounters(type, model);
        
        return "counter/list :: counters_table_full";
    }    
    
    /* Add Counter */

    @GetMapping("/counters/add")
    public String addCounter(Model model) {

        model.addAttribute("counterForm", new CounterForm());
        model.addAttribute("mode", EDIT_MODE_ADD);

        populateCounterTypes(model);

        return "counter/edit";
    }

    @PostMapping("/counters/add")
    public String submitAddCounter(@Valid CounterForm counter, BindingResult bindingResult, Model model,
            final RedirectAttributes redirectAttributes) {

        log.debug("new counter submitted: {}", counter);

        if (bindingResult.hasErrors()) {
            populateCounterTypes(model);
            return "counter/edit";
        }

        counterService.addCounter(counter);

        redirectAttributes.addFlashAttribute("success", "message.success.counter.added");

        return "redirect:/";
    }

    /* Edit Counter */

    @GetMapping("/counters/{id}/edit")
    public String editCounter(@PathVariable("id") String strCounterId, Model model,
            final RedirectAttributes redirectAttributes) {

        Long counterId = Long.parseLong(strCounterId);

        Counter counter = counterService.getCounterById(counterId);
        
        if (counter == null || !isHasPermission(counter)) {
            redirectAttributes.addFlashAttribute("error", "message.error.no.permission");
            return "redirect:/";
        }
        
        model.addAttribute("counterForm",  new CounterForm(counter));
        model.addAttribute("mode", EDIT_MODE_EDIT);

        populateCounterTypes(model);
        
        return "counter/edit";
    }

    @PostMapping("/counters/{id}/edit")
    public String submitEditCounter(@Valid CounterForm counter, BindingResult bindingResult, Model model,
            final RedirectAttributes redirectAttributes) {

        log.debug("edit counter form submitted: {}", counter);

        if (bindingResult.hasErrors()) {
            return "counter/edit";
        }

        try {
            counterService.updateCounter(counter);
        } catch (RemoteNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "message.error.no.permission");
            return "redirect:/";
        }        
        
        redirectAttributes.addFlashAttribute("success", "message.success.counter.updated");

        return "redirect:/";
    }

    private void populateCounters(Integer[] type, Model model) {

        List<Counter> counters;

        if (isAdmin()) {
            counters = counterService.getAllCountersWithType(type);
        } else {
            counters = counterService.getAllCountersForUserWithType(type, getCurrentUserLogin());
        }

        model.addAttribute("counters", counters);
        
        populateCounterTypes(model);
    }
    
    private void populateCounterTypes(Model model) {
        List<CounterType> types = counterService.getCounterTypes();

        model.addAttribute("typesMap", types.stream().collect(
                Collectors.toMap(CounterType::getId, CounterType::getName)));
    
    }

}
