package MJ.animal_Hospital_Service.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {

  @Value("${javaScriptKey:NOT_FOUND}")
  private String javascriptKey;

  @GetMapping("/join")
  public String join(){
    return "join";
  }

  @GetMapping("/favList")
  public String fav(){
    return "favList";
  }

  @GetMapping("/")
  public String goMain(Model model){
    model.addAttribute("javascriptKey", javascriptKey);
    return "main";
  }

  @GetMapping("/kakao")
  public String goKLogin() {
    return "redirect:/oauth/kakao";
  }

  @GetMapping("/naver")
  public String goNLogin() {
    return "redirect:/oauth/naver";
  }
}
