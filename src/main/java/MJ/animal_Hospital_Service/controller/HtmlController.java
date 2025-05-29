package MJ.animal_Hospital_Service.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {

  @GetMapping("/join")
  public String join(){
    return "join";
  }

  @GetMapping("/favList")
  public String fav(){
    return "favList";
  }

  @GetMapping("/")
  public String goMain(){
    return "main";
  }

}
