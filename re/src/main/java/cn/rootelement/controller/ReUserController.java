package cn.rootelement.controller;

import cn.rootelement.dto.PageDTO;
import cn.rootelement.dto.ReUserSaveDTO;
import cn.rootelement.dto.ReUserSearchDTO;
import cn.rootelement.dto.ReUserUpdateDTO;
import cn.rootelement.entity.ReUser;
import cn.rootelement.enumeration.HttpStatusEnum;
import cn.rootelement.service.IReUserService;
import cn.rootelement.util.JJWTUtil;
import cn.rootelement.util.Md5Util;
import cn.rootelement.vo.JsonResultVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

/**
 * 用户controller
 * @author ljt
 * @date 2020/1/18
 * @version 1.0.2
 */
@RestController
@RequestMapping("/user")
@Api(value = "ReUserController", tags = {"用户接口"})
public class ReUserController {

    private final IReUserService iReUserService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JJWTUtil jjwtUtil;

    @Autowired
    public ReUserController(IReUserService iReUserService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JJWTUtil jjwtUtil) {
        this.iReUserService = iReUserService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jjwtUtil = jjwtUtil;
    }

    @GetMapping("/{id:\\d+}")
    @ApiOperation(value = "根据id获取用户对象", httpMethod = "GET")
    public JsonResultVO getEntityById(@PathVariable Serializable id) {
        return iReUserService.getEntityById(id);
    }

    @GetMapping
    @ApiOperation(value = "查询所有用户列表", httpMethod = "GET")
    public JsonResultVO listEntityAll() {
        return iReUserService.listEntityAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "新增一个用户", httpMethod = "POST")
    public JsonResultVO saveEntity(@Validated ReUserSaveDTO reUserSaveDTO) {
        ReUser entity = new ReUser();
        BeanUtils.copyProperties(reUserSaveDTO, entity);
        entity.setPassword(Md5Util.getInstance().getMd5LowerCase(reUserSaveDTO.getPassword()));
        entity.setStatus((byte) 1);
        entity.setCreateTime(new Date());
        entity.setModifyTime(new Date());
        return iReUserService.saveEntity(entity);
    }

    @PutMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "根据id更新用户", httpMethod = "PUT")
    public JsonResultVO updateEntityById(@PathVariable(value = "id") Serializable id, @Validated ReUserUpdateDTO reUserUpdateDTO) {
        ReUser entity = new ReUser();
        BeanUtils.copyProperties(reUserUpdateDTO, entity);
        entity.setPassword(Md5Util.getInstance().getMd5LowerCase(reUserUpdateDTO.getPassword()));
        entity.setStatus((byte) 1);
        return iReUserService.updateEntityById(id, entity);
    }

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    public JsonResultVO deleteEntityById(@PathVariable(value = "id") Serializable id) {
        return iReUserService.deleteEntityById(id);
    }

    @PutMapping("/restore/{id:\\d+}")
    @PreAuthorize("hasRole('root')")
    @ApiOperation(value = "恢复删除的用户", notes = "id只能为数字类型", httpMethod = "PUT")
    public JsonResultVO restore(@PathVariable(value = "id") Serializable id) {
        return iReUserService.restore(id);
    }

    @PostMapping("/search")
    @ApiOperation(value = "根据username、tel和email模糊查询", notes = "根据username、tel和email模糊查询", httpMethod = "POST")
    public JsonResultVO search(ReUserSearchDTO reUserSearchDTO, @Validated PageDTO pageDTO) {
        return iReUserService.search(reUserSearchDTO, pageDTO);
    }

    @GetMapping("/listUserPage")
    @ApiOperation(value = "分页查询用户列表", httpMethod = "GET")
    public JsonResultVO listUserPage(@Validated PageDTO pageDTO) {
        return iReUserService.listUserPage(pageDTO.getPage(), pageDTO.getCount());
    }

    @GetMapping("/getUserInfoByToken")
    @ApiOperation(value = "通过token获取用户的信息", httpMethod = "GET")
    public JsonResultVO getUserInfoByToken(@RequestParam("token") String token) {
        JJWTUtil instance = JJWTUtil.getInstance();
        JsonResultVO resultVO;
        if (instance.isTokenExpired(token)) {
            resultVO = JsonResultVO.forMessage(HttpStatusEnum.TOKEN_EXPIRED.getMsg(), HttpStatusEnum.TOKEN_EXPIRED.getCode());
        } else {
            Claims claims = instance.getClaimsFromToken(token);
            String username = (String) claims.get("username");
            Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) claims.get("authorities");
            Map<String, Object> user = new HashMap<>(3);
            user.put("username", username);
            user.put("authorities", authorities);
            resultVO = JsonResultVO.newBuilder().data(null).message("请求成功")
                    .request("success").status(HttpStatusEnum.OK.getCode())
                    .totalCount(null).fields(user)
                    .build();
        }
        return resultVO;
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public JsonResultVO login(String username, String password) {
        // TODO 首先可以从数据库中查询是否有这个用户名或者密码，然后根据结果返回错误
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwt = jjwtUtil.generateToken(userDetails);
        JsonResultVO jsonResultVO = JsonResultVO.forMessage("登陆成功", 200);
        jsonResultVO.addField("token", jwt);
        return jsonResultVO;
    }
}
