const map = new Map();

//**************************通用**************************//
/** 请求参数错误 */
map.set("REQUEST_PARAM_ERROR", 400000)
/** 资源已经存在 */
map.set("RESOURCE_ALREADY_EXIST_ERROR", 400001)
/** 资源不存在 */
map.set("RESOURCE_NOT_EXIST_ERROR", 400002)

//**************************用户相关**************************//
/** 用户名格式错误 */
map.set("USERNAME_FORMAT_ERROR", 400100)
/** 密码格式错误 */
map.set("PASSWORD_FORMAT_ERROR", 400101)
/** 用户不存在 */
map.set("USER_NOT_EXIST", 400102)
/** 用户名重复 */
map.set("USERNAME_ALREADY_EXIST", 400103)
/** 验证码过期 */
map.set("VERIFY_CODE_EXPIRED", 400104)
/** 验证码错误 */
map.set("VERIFY_CODE_ERROR", 400105)
/** 邮箱格式错误 */
map.set("EMAIL_FORMAT_ERROR", 400106)
/** 手机号码格式错误 */
map.set("PHONE_FORMAT_ERROR", 400107)
/** 用户id不能为NULL */
map.set("USER_ID_NULL_ERROR", 400108)
/** 请输入用户名 */
map.set("INPUT_USERNAME", 400109)
/** 请输入密码 */
map.set("INPUT_PASSWORD", 400110)
/** 密码错误 */
map.set("PASSWORD_ERROR", 400111)
/** 验证码ID为空 */
map.set("VERIFY_CODE_NULL_ERROR", 400112)
/** 用户已经登录 */
map.set("USER_ALREADY_LOGIN_ERROR", 400113)

//**************************角色相关**************************//
/** 角色id不能为空 */
map.set("ROLE_ID_NULL_ERROR", 400200)
/** 角色名不能为空 */
map.set("ROLE_NAME_EMPTY_ERROR", 400201)
/** 角色已经存在 */
map.set("ROLE_ALREADY_EXIST", 400202)
/** 角色不存在 */
map.set("ROLE_NOT_EXIST", 400203)

//**************************权限相关**************************//
map.set("PERMISSION_NOT_EXIST", 400300)

/** token过期 */
map.set("TOKEN_EXPIRED_ERROR", 403001)
/** token格式错误 */
map.set("TOKEN_FORMAT_ERROR", 403002)
/** token签名错误 */
map.set("TOKEN_SIGNATURE_ERROR", 403003)
/** 用户权限异常 */
map.set("USER_PERMISSION_ERROR", 4003004)
/** 用户未认证 */
map.set("USER_NOT_AUTHENTICATION", 4003005)
/** 禁止访问 */
map.set("ACCESS_DENIED_ERROR", 4003006)

//**************************配置相关**************************//
map.set("CONFIG_ID_NULL_ERROR", 400400)

//**************************系统异常相关**************************//
/** 系统异常 */
map.set("SYSTEM_ERROR", 500000)
/** 数据库操作异常 */
map.set("DATABASE_OPERATION_ERROR", 500001)
/** 业务异常 */
map.set("BUSINESS_ERROR", 500002)
/** 用户校验异常 */
map.set("USER_VALIDATE_ERROR", 500003)


export const GlobalError = map;
