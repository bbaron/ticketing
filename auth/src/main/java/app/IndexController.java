package app;

import common.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping(path = "/**")
    void notFound() {
        throw new NotFoundException();
    }
}
