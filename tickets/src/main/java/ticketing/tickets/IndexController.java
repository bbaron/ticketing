package ticketing.tickets;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ticketing.common.exceptions.NotFoundException;

@RestController
public class IndexController {
    @RequestMapping("/**")
    void notFound() {
        throw new NotFoundException();
    }

}
