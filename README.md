# AptTools
使用android-apt开发的工具库

## 加密字符串常量

1. 复制 "encrypt-constant", "encrypt-constant-annotation", "encrypt-constant-compiler" 到工程目录

2. 分别修改加密和解密算法：
1) "encrypt-constant-compiler" 下 EncryptUtil.encodeString(String value, String key)
2) "encrypt-constant" 下 DecryptUtil.decodeString(String value, String key)

3. Gradle 配置:
```
dependencies {
    compile project(':encrypt-constant')
    apt project(':encrypt-constant-compiler')
}
```

4. 对class使用注解@EncryptConstant，生成含有加密常量后的class（带前缀Encrypt），例如：

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

5. 使用EncryptConstant代替Constant
