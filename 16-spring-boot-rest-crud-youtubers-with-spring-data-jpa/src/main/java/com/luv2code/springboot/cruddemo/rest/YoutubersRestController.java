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
    public Youtubers getYoutubers(@PathVariable int YoutubersId) {

        Youtubers theYoutubers = YoutubersService.findById(YoutubersId);

        if (theYoutubers == null) {
            throw new RuntimeException("Youtuber id.No encontrado - " + YoutubersId);
        }

        return theYoutubers;
    }


    @PostMapping("/Youtubers")
    public Youtubers addYoutubers(@RequestBody Youtubers theYoutubers) {

        

        theYoutubers.setId(0);

        Youtubers dbYoutubers = YoutubersService.save(theYoutubers);

        return dbYoutubers;
    }


    @PutMapping("/Youtubers")
    public Youtubers updateYoutubers(@RequestBody Youtubers theYoutubers) {

        Youtubers dbYoutubers = YoutubersService.save(theYoutubers);

        return dbYoutubers;
    }

  

    @PatchMapping("/Youtubers/{YoutubersId}")
    public Youtubers patchYoutubers(@PathVariable int YoutubersId,
            @RequestBody Map<String, Object> patchPayload) {

        
        Youtubers tempYoutubers = YoutubersService.findById(YoutubersId);

        if (tempYoutubers == null) {
            throw new RuntimeException("Youtubers id no encontrado. - " + YoutubersId);
        }

    
        if (patchPayload.containsKey("id")) {
            throw new RuntimeException(
                    "Youtubers id No puede ser modificado. Elimina 'id'.");
        }

     
        Youtubers patchedYoutubers = jsonMapper.updateValue(tempYoutubers, patchPayload);


        Youtubers dbYoutubers = YoutubersService.save(patchedYoutubers);

        return dbYoutubers;
    }


    @DeleteMapping("/Youtubers/{YoutubersId}")
    public String deleteYoutubers(@PathVariable int YoutubersId) {

        Youtubers tempYoutubers = YoutubersService.findById(YoutubersId);



        if (tempYoutubers == null) {
            throw new RuntimeException("Youtubers id. No encontrado - " + YoutubersId);
        }

        YoutubersService.deleteById(YoutubersId);

        return " id Youtubers eliminado - " + YoutubersId;
    }

}
