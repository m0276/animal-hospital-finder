package MJ.animal_Hospital_Service.service.favorite;

import MJ.animal_Hospital_Service.domain.Favorite;
import MJ.animal_Hospital_Service.dto.FavoriteDto;
import MJ.animal_Hospital_Service.repository.FavoriteRepository;
import MJ.animal_Hospital_Service.service.user.UserService;
import MJ.animal_Hospital_Service.util.LoginUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

@RequiredArgsConstructor
@Service
@Transactional
public class FavoriteService {
  private final FavoriteRepository favoriteRepository;
  private final FavoriteMapper favoriteMapper;
  private final UserService userService;

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

  public void saveOrDelete(String hospitalId){
    if(LoginUtil.isLogin()){
      String username = LoginUtil.getCurrentUser();
      if(favoriteRepository.findByUserId(userService.findByUserNameReturnId(username)).stream()
          .map(Favorite::getHospitalId).collect(Collectors.toSet()).contains(hospitalId)){
        delete(hospitalId,userService.findByUserNameReturnId(username));
      }
      else save(hospitalId,userService.findByUserNameReturnId(username));



    }
    else throw new NotAcceptableStatusException("로그인 후 기능을 사용 할 수 있습니다.");
  }

  private void save(String hospitalId,Long userId){
    Favorite favorite = Favorite.builder()
        .hospitalId(hospitalId)
        .userId(userId)
        .build();
    favoriteRepository.save(favorite);
  }

  private void delete(String hospitalId,Long userId){
    favoriteRepository.deleteByHospitalIdAndUserId(hospitalId,userId);
  }
}
