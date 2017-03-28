# AptTools
使用android-apt开发的工具库

## 加密字符串常量('encrypt-constant')

### Gradle 配置
添加maven仓库配置到根build.gradle
```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

添加依赖到应用的build.gradle
```
dependencies {
    compile 'com.github.daweizhou89:AptTools:encrypt-constant:0.1.0'
    annotationProcessor 'com.github.daweizhou89:AptTools:encrypt-constant-compiler:0.1.0'
}
```

### 使用方式
1. 对class使用注解@EncryptConstant，生成含有加密常量后的class（带前缀Encrypt），例如：

```java
@EncryptConstant
public class Constant {
    public static final String MY_EMAIL = "daweizhou89@gmail.com";

}
```

会生成

```java
public final class EncryptConstant {
  public static final String MY_EMAIL = DecryptUtil.decodeString("\u000f\u0004\u000e\u000e\f\u0003\u0003\n"
        + "\fS\\9\f\b\u0018\u0002\tW\b\n"
        + "\u0014", "key");
}
```

2. 使用EncryptConstant代替Constant
