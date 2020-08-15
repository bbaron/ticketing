package app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketing.exceptions.NotFoundException;

@RestController
public class IndexController {
    @RequestMapping("/**")
    void notFound() {
        throw new NotFoundException();
    }

}
