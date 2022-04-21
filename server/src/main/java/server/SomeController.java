package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class SomeController {

    /**
     * Adding checkstyle
     * @return Adding checkstyle
     */
    @GetMapping("/")
    @ResponseBody // DO NOT DELETE USED TO TEST PRESENCE OF SERVER
    public String index() {
        return "1kxIEPWKIFKzjHFnnZYPHD43KFMGOP";
    }
}