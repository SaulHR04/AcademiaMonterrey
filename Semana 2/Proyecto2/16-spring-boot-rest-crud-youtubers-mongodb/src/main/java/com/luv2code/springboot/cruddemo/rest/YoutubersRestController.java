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
            throw new RuntimeException("Youtubers id no encontrada - " + YoutubersId);
        }

        return theYoutubers;
    }


    @PostMapping("/Youtubers")
    public Youtubers addYoutubers(@RequestBody Youtubers theYoutubers) {

        
        theYoutubers.setId(null);

        Youtubers dbYoutubers = YoutubersService.save(theYoutubers);

        return dbYoutubers;
    }


    @PutMapping("/Youtubers")
    public Youtubers updateYoutubers(@RequestBody Youtubers theYoutubers) {

        Youtubers dbYoutubers = YoutubersService.save(theYoutubers);

        return dbYoutubers;
    }

 

    @PatchMapping("/Youtubers/{YoutubersId}")
    public Youtubers patchYoutubers(@PathVariable String YoutubersId,
            @RequestBody Map<String, Object> patchPayload) {

        Youtubers tempYoutubers = YoutubersService.findById(YoutubersId);

        if (tempYoutubers == null) {
            throw new RuntimeException(" - " + YoutubersId);
        }

       
        if (patchPayload.containsKey("id")) {
            throw new RuntimeException(
                    "Youtubers id no puede ser modificado. elimina  'id' ");
        }

        
        Youtubers patchedYoutubers = jsonMapper.updateValue(tempYoutubers, patchPayload);

        Youtubers dbYoutubers = YoutubersService.save(patchedYoutubers);

        return dbYoutubers;
    }


    @DeleteMapping("/Youtubers/{YoutubersId}")
    public String deleteYoutubers(@PathVariable String YoutubersId) {

        Youtubers tempEmployee = YoutubersService.findById(YoutubersId);

    
        if (tempEmployee == null) {
            throw new RuntimeException("Youtubers id no encontrado - " + YoutubersId);
        }

        YoutubersService.deleteById(YoutubersId);

        return "Deleted employee id - " + YoutubersId;
    }

}
