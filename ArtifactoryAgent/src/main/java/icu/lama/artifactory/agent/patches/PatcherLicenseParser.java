package icu.lama.artifactory.agent.patches;

import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Deprecated
public class PatcherLicenseParser extends ClassPatch {
    public PatcherLicenseParser() {
        super("org.jfrog.license.api.a", "org.jfrog.license.api.LicenseManager");
    }

    @Override
    byte[] onTransform(String className, CtClass clazz, byte[] classBuf) throws Throwable {
        var overrides = "";

        var publicKeyFieldC = tryGetDeclaredField(clazz, "c"); // 7.9.2 using c
        if (publicKeyFieldC != null) {
            publicKeyFieldC.setModifiers(Modifier.PRIVATE + Modifier.STATIC);
            overrides += "c = icu.lama.artifactory.agent.AgentMain.PUBLIC_KEY;";
        }

        var publicKeyFieldD = tryGetDeclaredField(clazz, "d"); // 7.59 changed to d
        if (publicKeyFieldD != null) {
            publicKeyFieldD.setModifiers(Modifier.PRIVATE + Modifier.STATIC);
            overrides += "d = icu.lama.artifactory.agent.AgentMain.PUBLIC_KEY;";
        }

        var publicKeyFieldNObf = tryGetDeclaredField(clazz, "jfrogPublicKey"); // 7.59 in license-manager-7.63.3.jar, no obfuscation version of artifactory-addons-manager
        if (publicKeyFieldNObf != null) {
            publicKeyFieldNObf.setModifiers(Modifier.PRIVATE + Modifier.STATIC);
            overrides += "jfrogPublicKey = icu.lama.artifactory.agent.Constants.PUBLIC_KEY;";
        }

        var clinitMethod = Arrays.stream(clazz.getDeclaredBehaviors()).filter((it) -> "<clinit>".equals(it.getMethodInfo().getName())).findAny();
        if (clinitMethod.isEmpty()) {
            throw new Throwable("Corrupted class!");
        }

        clinitMethod.get().insertAfter(overrides);

        // val methodParseLicense = ctClass.getMethod("a", "(Ljava/lang/String;)Lorg/jfrog/license/api/License;")
        // methodParseLicense.setBody("""
        //     try {
        //         return b($1, icu.lama.artifactory.tools.Constants.PUBLIC_KEY);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //         throw e;
        //     }
        // """.trimIndent())

        clazz.detach();
        return clazz.toBytecode();
    }

    private @Nullable CtField tryGetDeclaredField(CtClass clazz, String field) {
        try {
            return clazz.getDeclaredField(field);
        } catch (NotFoundException e) {
            return null;
        }
    }
}