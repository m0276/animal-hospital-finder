package MJ.animal_Hospital_Service.service.favorite;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

import MJ.animal_Hospital_Service.config.Role;
import MJ.animal_Hospital_Service.domain.Favorite;
import MJ.animal_Hospital_Service.domain.Hospital;
import MJ.animal_Hospital_Service.domain.User;
import MJ.animal_Hospital_Service.dto.FavoriteDto;
import MJ.animal_Hospital_Service.dto.UserDto;
import MJ.animal_Hospital_Service.repository.FavoriteRepository;
import MJ.animal_Hospital_Service.service.user.UserMapper;
import MJ.animal_Hospital_Service.service.user.UserService;
import MJ.animal_Hospital_Service.util.LoginUtil;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

  @InjectMocks
  private FavoriteService favoriteService;

  @Mock
  private UserService userService;

  @Mock
  private FavoriteMapper favoriteMapper;

  @Mock
  private FavoriteRepository favoriteRepository;

  @Mock
  private UserMapper userMapper;

  @BeforeEach
  void setUp(){
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken("testUser", "password", Collections.emptyList());
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);
  }

  @Test
  @DisplayName("즐겨찾기 목록 불러오기")
  void getList() {
    User user = User.builder()
        .username("testUser")
        .id(1L)
        .password("1234")
        .build();

    UserDto dto = new UserDto(1L,"testUser",Set.of(Role.ROLE_USER));

    given(userService.get(anyString())).willReturn(dto);
    given(favoriteRepository.findByUserId(user.getId())).willReturn(Collections.emptyList());

    List<FavoriteDto> list = favoriteService.getList();

    assertTrue(list.isEmpty());
  }

  @Test
  @DisplayName("즐겨찾기 추가")
  void saveOrDelete_save() {
    Hospital hospital = Hospital.builder()
        .locationId("1")
        .roadAddressName("hogukro")
        .x(111111.445)
        .y(1234.21345)
        .build();

    given(favoriteRepository.findByUserId(any(Long.class))).willReturn(Collections.emptyList());
    given(favoriteRepository.save(any(Favorite.class)))
        .willAnswer(invocation -> invocation.getArgument(0));


    favoriteService.saveOrDelete(hospital.getLocationId());

    verify(favoriteRepository,times(1)).save(any(Favorite.class));
  }

  @Test
  @DisplayName("즐겨찾기 삭제")
  void saveOrDelete_delete() {
    Hospital hospital = Hospital.builder()
        .locationId("1")
        .roadAddressName("hogukro")
        .x(111111.445)
        .y(1234.21345)
        .build();
    Favorite favorite = new Favorite(1L,1L,"1");

    given(favoriteRepository.findByUserId(any(Long.class))).willReturn(List.of(favorite));
    doNothing().when(favoriteRepository).deleteByHospitalIdAndUserId(anyString(),anyLong());

    favoriteService.saveOrDelete(hospital.getLocationId());

    verify(favoriteRepository,times(1))
        .deleteByHospitalIdAndUserId(anyString(),anyLong());
  }
}