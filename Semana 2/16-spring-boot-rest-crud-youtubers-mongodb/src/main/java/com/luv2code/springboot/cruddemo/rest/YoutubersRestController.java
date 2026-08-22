package com.luv2code.springboot.cruddemo.rest;

import tools.jackson.databind.json.JsonMapper;
import com.luv2code.springboot.cruddemo.entity.Youtubers;
import com.luv2code.springboot.cruddemo.service.YoutubersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class YoutubersRestController {

    private YoutubersService YoutubersService;

    private JsonMapper jsonMapper;

    @Autowired
    public YoutubersRestController(YoutubersService theYoutubersService, JsonMapper theJsonMapper) {
    	YoutubersService = theYoutubersService;
        jsonMapper = theJsonMapper;
    }

    @GetMapping("/Youtubers")
    public List<Youtubers> findAll() {
        return YoutubersService.findAll();
    }

   

    @GetMapping("/Youtubers/{YoutubersId}")
    public Youtubers getYoutubers(@PathVariable String YoutubersId) {

        Youtubers theYoutubers = YoutubersService.findById(YoutubersId);

        if (theYoutubers == null) {
            throw new RuntimeException("Youtubers id no encontrado - " + YoutubersId);
        }

        return theYoutubers;
    }

  

    @PostMapping("/Youtubers")
    public Youtubers addYoutubers(@RequestBody Youtubers theYoutubers) {

      

        theYoutubers.setId(null);

        Youtubers dbYoutubers = YoutubersService.save(theYoutubers);

        return dbYoutubers;
    }

    // add mapping for PUT /employees - update existing employee

    @PutMapping("/employees")
    public Youtubers updateEmployee(@RequestBody Youtubers theEmployee) {

        Youtubers dbEmployee = YoutubersService.save(theEmployee);

        return dbEmployee;
    }

   

    @PatchMapping("/Youtubers/{YoutubersId}")
    public Youtubers patchYoutubers(@PathVariable String YoutubersId,
            @RequestBody Map<String, Object> patchPayload) {

        Youtubers tempYoutubers = YoutubersService.findById(YoutubersId);

        if (tempYoutubers == null) {
            throw new RuntimeException("Youtubers no encontrado- " + YoutubersId);
        }


        if (patchPayload.containsKey("id")) {
            throw new RuntimeException(
                    "Youtubers id no puede ser modificado.");
        }

      
        Youtubers patchedYoutubers = jsonMapper.updateValue(tempYoutubers, patchPayload);

        // Step 4: Save the updated employee to database and return it
        Youtubers dbYoutubers = YoutubersService.save(patchedYoutubers);

        return dbYoutubers;
    }

    // add mapping for DELETE /employees/{employeeId} - delete employee

    @DeleteMapping("/Youtubers/{YoutubersId}")
    public String deleteEmployee(@PathVariable String YoutubersId) {

        Youtubers tempYoutubers = YoutubersService.findById(YoutubersId);

        // throw exception if null

        if (tempYoutubers == null) {
            throw new RuntimeException("Youtubers id no encontrado- " + YoutubersId);
        }

        YoutubersService.deleteById(YoutubersId);

        return "borrado Youtubers id - " + YoutubersId;
    }

}
