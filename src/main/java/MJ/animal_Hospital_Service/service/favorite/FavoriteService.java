package MJ.animal_Hospital_Service.service.favorite;

import MJ.animal_Hospital_Service.domain.Favorite;
import MJ.animal_Hospital_Service.dto.FavoriteDto;
import MJ.animal_Hospital_Service.repository.FavoriteRepository;
import MJ.animal_Hospital_Service.service.user.UserService;
import MJ.animal_Hospital_Service.util.LoginUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

@RequiredArgsConstructor
@Service
@Transactional
public class FavoriteService {
  private FavoriteRepository favoriteRepository;
  private FavoriteMapper favoriteMapper;
  private UserService userService;

  public List<FavoriteDto> getList(){
    if(LoginUtil.isLogin()){
      String username = LoginUtil.getCurrentUser();

      Long userId = userService.get(username).id();

      List<Favorite> list = favoriteRepository.findByUserId(userId);

      List<FavoriteDto> result = new ArrayList<>();

      for(Favorite favorite: list){
        result.add(favoriteMapper.toFavoriteDto(favorite));
      }

      return result;
    }
    else throw new NotAcceptableStatusException("로그인 후 확인할 수 있습니다.");
  }

  private void save(){
    if(LoginUtil.isLogin()){
      String username = LoginUtil.getCurrentUser();

      //dto는 병원 이름을 가지고 있고 domain은 병원 위치를 가지고 있음 > hospital service 단에서 처리가 필요해보입


    }
    else throw new NotAcceptableStatusException("로그인 후 기능을 사용 할 수 있습니다.");
  }

  //delete와 delete 또는 save인지 판단하는 로직 구현 필요
}
