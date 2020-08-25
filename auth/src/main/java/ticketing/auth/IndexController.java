package ticketing.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketing.common.exceptions.NotFoundException;

@RestController
public class IndexController {

    @RequestMapping(path = "/**")
    void notFound() {
        throw new NotFoundException();
    }
}
