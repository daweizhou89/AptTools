# AptTools
使用android-apt开发的工具库

## 加密字符串常量

1. 复制 "encrypt-constant", "encrypt-constant-annotation", "encrypt-constant-compiler" 到工程目录

2. 分别修改加密和解密算法：
1) "encrypt-constant-compiler" 下 EncryptUtil.encodeString(String value)
2) "encrypt-constant" 下 DecryptUtil.decodeString(String value)

3. Gradle 配置:
```groovy
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
  public static final String MY_EMAIL = DecryptUtil.decodeString("\u0003\u000b\u0007\u000f\u0015\u0015\u0015\n"
      + "\\\u0002\u0007\u0011\u0003\u000f\u001c\u000e\t\u0013^_&\u0001\u000b\u0007\u000f\n"
      + "H\u0005\t\u000b");
}
```

5. 使用EncryptConstant代替Constant
