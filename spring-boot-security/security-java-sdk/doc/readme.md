# 系统说明

此文档主要描述作为客户端的Java应用，如何接入权限管理系统

## 1. 权限拦截点
### 1.1 什么叫权限拦截点？
**权限拦截点** - 又叫“**受保护资源**”，权限拦截点是指一个具体需要拦截的方法，此方法必须是Spring容器托管的对象，包括如下：
<!-- TOC -->
* [@RestController/@Controller](#@RestController/@Controller) 即一个API
* [@Service](#@Service) 
* [@Component](#@Component)
* [@Repository](#@Repository)
<!-- TOC -->

### 1.2 如何标识一个权限拦截点？
**权限拦截点** - 标识一个权限拦截点有两种方法：
<!-- TOC -->
* [@RBAC集中定义](#@RBAC集中定义) 从RBAC系统中集中定义，这种方法可以实现客户端代码0侵入。但是这种方法也有缺点如下
  * 即当方法所属的类的包、类名、方法名，或者是变量名（当有数据权限时）
  * 在同一类不能用同名方法（目前还不能处理）
* [@明码标识](#@明码标识) 在代码中通过@SecuredPoint进行标准
<!-- TOC -->

**note**, 如果同一个资源既被本地通过 @SecuredPoint标识，又被远程集中定义，则远程定义会覆盖本地定义

## Login
系统采用Spring Security默认用户登录, 其ID是 **user** , 密码是动态生成的，通过以下可以重系统日志中找到，比如：
```text
Using generated security password: 0329f247-02bb-4b40-859d-70175e5e47a0
```


