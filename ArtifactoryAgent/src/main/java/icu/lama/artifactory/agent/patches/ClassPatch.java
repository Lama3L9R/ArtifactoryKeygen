package icu.lama.artifactory.agent.patches;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

abstract public class ClassPatch implements ClassFileTransformer {
    public final List<String> targetClasses;

    public ClassPatch(String... targetClasses) {
        this.targetClasses = Arrays.asList(targetClasses);
    }

    @Override
    public final byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        var clazz = className.replace("/", ".");
        if (targetClasses.contains(clazz)) {
            System.out.println("Artifactory Agent :: Patching Class: " + clazz + ", source = " + protectionDomain.getCodeSource().getLocation().toString());
            var ctPool = ClassPool.getDefault();

            try {
                if (classBeingRedefined == null) {
                    return this.onTransform(clazz, ctPool.makeClass(new ByteArrayInputStream(classfileBuffer)), classfileBuffer);
                } else {
                    return this.onRetransform(clazz, ctPool.makeClass(new ByteArrayInputStream(classfileBuffer)), classfileBuffer, classBeingRedefined);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return classfileBuffer;
    }

    byte[] onTransform(String className, CtClass clazz, byte[] classBuf) throws Throwable {
        return classBuf;
    }

    byte[] onRetransform(String className, CtClass clazz, byte[] classBuf, Class<?> classBeingRedefined) throws Throwable {
        return classBuf;
    }
}
