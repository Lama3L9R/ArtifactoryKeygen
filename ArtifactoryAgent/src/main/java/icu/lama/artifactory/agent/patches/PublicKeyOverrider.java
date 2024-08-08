package icu.lama.artifactory.agent.patches;

import javassist.CtBehavior;
import javassist.CtClass;

import java.util.Arrays;
import java.util.Optional;

public class PublicKeyOverrider extends ClassPatch {

    public PublicKeyOverrider() {
        super("org.jfrog.license.a.a");
    }

    @Override
    byte[] onTransform(String className, CtClass clazz, byte[] classBuf) throws Throwable {
        Optional<CtBehavior> ctToStringOpt = Arrays.stream(clazz.getDeclaredBehaviors()).filter((it) -> "toString".equals(it.getMethodInfo().getName())).findAny();
        if (ctToStringOpt.isPresent()) {
            CtBehavior ctToString = ctToStringOpt.get();
            ctToString.setBody(
                    "if (icu.lama.artifactory.agent.Constants.JFROG_KEYS.contains($0.b)) {" +
                    "    return icu.lama.artifactory.agent.Constants.PUBLIC_KEY;" +
                    "} else { " +
                    "    return $0.b;" +
                    "}"
            );

            clazz.detach();
            return clazz.toBytecode();
        }

        return clazz.toBytecode();
    }
}
