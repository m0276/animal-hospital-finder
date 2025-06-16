package MJ.animal_Hospital_Service.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

  @GetMapping("/issueList")
  public String issue(){
    return "issueList";
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

  @GetMapping("/issue/{hospitalId}")
  public String getIssue(@PathVariable String hospitalId, Model model){
    model.addAttribute("hospitalId",hospitalId);
    return "issue";
  }

  @GetMapping("/issue/me/{id}")
  public String getPatchIssue(@PathVariable Long id, Model model){
    model.addAttribute("issueId",id);
    return "issuePath";
  }
}
