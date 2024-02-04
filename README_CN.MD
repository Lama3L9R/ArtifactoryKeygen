# ArtifactoryKeygen

正如其名，这是 `Artifactory` 的 `Keygen`

已在 7.59.11 (当前为止最新版), 7.9.2 (在 [releases-docker](https://releases-docker.jfrog.io/) 可以下载到的最新版) 上进行测试

如果您认为这个项目对您有帮助，请为这个项目送上您的 `Star`

**警告！！ _您不得使用本项目对 JFrog 的任何应用进行 破解，修改或非法使用！_**

**警告！！ _本项目仅用于教育目的_**

**警告！！ _使用本软件造成的一切后果由使用者承担_**

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu)
[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE)
---

点击切换不同语言
- [English](README.MD)
- [简体中文](README_CN.MD)

目录
- [ArtifactoryKeygen](#artifactorykeygen)
    * [0x00 安装](#0x00-安装)
    * [0x01 配置 Keygen  [可选]](#0x01-配置-keygen)
    * [0x02 配置 Agent  [可选]](#0x02-配置-agent)
        + [覆盖默认公钥](#覆盖默认公钥)
            - [使用 Keygen ](#使用-Keygen )
            - [手动](#手动)
    * [0x03 其他有用的工具](#0x03-其他有用的工具)
        + [ Keygen ](#Keygen)
    * [0x04 编译](#0x04-编译)
    * [0x05 TODOs 和更新](#0x05-todos-和更新)

![](imgs/Success.png)

## 0x00 安装

从 Github Releases 下载 `ArtifactoryKeygen` 和 `ArtifactoryAgent`

使用 Java 11 及以上版本运行 `ArtifactoryKeygen`.

```shell
$ java -jar /path/to/ArtifactoryKeygen-1.0-SNAPSHOT-all.jar [sub-command]
```

> 如果 sub-command 为空，稍后会提示您输入

使用 `gen` 子命令生成一个许可证

当然,  Keygen 生成的许可证不能直接使用.
您需要 `ArtifactoryAgent` 配合来修补一些文件.

导航到 Artifactory 的 Tomcat 文件夹 (/opt/jfrog/artifactory/app/artifactory/tomcat/) 然后编辑 `bin/setenv.sh` (在 Windows 上为 `setenv.bat` )

将以下内容添加到该文件. 不要忘记修改 path!
```shell
# Linux / macOS(但是为什么要用 macOS??)
CATALINA_OPTS=-javaagent:/path/to/ArtifactoryAgent-1.0-SNAPSHOT-all.jar
```

```batch
:: Windows
SET CATALINA_OPTS=-javaagent:path\to\ArtifactoryAgent-1.0-SNAPSHOT-all.jar
```

警告: **_切勿将此软件用于非法用途!_**

## 0x01 配置 Keygen 

*目前暂不支持配置. 我将在未来增加这个功能.*

## 0x02 配置 Agent 

目前, 覆盖默认公钥是唯一可配置的条目.

### 覆盖默认公钥

首先, 您需要使用 Keygen 生成一个配置, 当然您也可以手动处理它.

#### 使用 Keygen 

运行 Keygen 并使用子命令 'mkconfig', 然后输入您的公钥.

需要注意的是，公钥应该是**X509格式的RSA公钥**, 并且密钥长度至少为4096位

复制生成的文本并将其放入 Agent 参数中, 如下所示:
```shell
# Linux
CATALINA_OPTS=-javaagent:/path/to/ArtifactoryAgent-1.0-SNAPSHOT-all.jar=<GeneratedConfigHere>
```

```batch
:: Windows
SET CATALINA_OPTS=-javaagent:/path/to/ArtifactoryAgent-1.0-SNAPSHOT-all.jar=<GeneratedConfigHere>
```

配置应采用十六进制格式, 所有字母大写

#### 手动

编辑下面的模板, 使用Base64进行编码, 然后将编码结果转换为所有字母大写的十六进制字符串.

```XML
<config>
    <publicKey>Your public key here</publicKey>
</config>
```

您可以通过将文本传递给 Agent 来获取十六进制文本 (对,  Agent 是可执行的!)


```
$ java -jar /path/to/ArtifactoryAgent-1.0-SNAPSHOT-all.jar <Text>
```

并且, 您可以使用 Keygen 中的 `genkey` 子命令生成密钥对 (RSA 4096)

## 0x03 其他有用的工具

###  Keygen 

您可以使用子命令 'help' 获取所有可用的子命令

我懒得再写帮助, 所以直接把子命令 'help' 的输出内容复制过来了

```kotlin
println("""
    List of all sub-commands:
        obf <text>:
            Obfuscate text with JFrog's 'ObfuscatedString' class
        pub:
            Get the current public key (RSA)
        pri:
            Get the current private key (RSA)
        genkey:
            Generate a key pair
        gen:
            Generate a license with the current private key
        verify <license>:
            Verify a license with the current public key
        enc: [ NOT TESTED ]
            Encrypt a license (I guess this is used for the license of the old version)
        verifyAgent:
            You can verify ArtifactoryAgent by attaching the agent to ArtifactoryKeygen
        mkconfig:
            Create agent config but friendly!
    To inject into artifactory
        Open Artifactory's tomcat folder and add the following JVM options to tomcat.
        In case you don't know how to pass extra JVM args you can see the README of this project
        OR Google it by yourself!
        
        -javaagent:</path/to/this/jar/file>
        
        his will patch class 'org.jfrog.license.api.a'
                    
""".trimIndent())
```

## 0x04 编译

与其他 gradle 项目相比，编译本项目代码可能略有不同.

您会注意到我使用了 JFrog 的一个库，它看起来不是公共库.
所以我决定不上传了. 但是, 您可以从 `artifactory.war` 中提取.

我假定您能熟悉使用 Java 和 SpringFramework 进行 Web 开发.所以这里不会有关于如何提取它的教程.如果您遇到了问题请先使用谷歌!

提取必要的库后, 您应该将它们放到 `libs` 中.
如果这些库已更改其版本, 请不要忘记在 `build.gradle.kts` 中进行更新.

要编译代码并打包包含所有依赖项的 jar 文件, 请使用以下 gradle 任务

```shell
# Root project (Keygen)
$ gradlew :shadowJar
```

```shell
# Agent
$ graldew :ArtifactoryAgent:shadowJar
```

**_随时欢迎PRs！感谢您对此项目的贡献!_**

## 0x05 TODOs 和更新

- [ ] 代码清理
- [ ] 向 Keygen 和 Agent 添加更多可配置项
- [ ] Artifactory 版本检测
- [ ] 从 `artifactory-addons-manager` 迁移到 `LicenseManager` (这是 `artifactory-addons-manager` 的反混淆版本)
- [ ] 中文版本 README.MD
