package net.csdn.bootstrap.loader.impl;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import net.csdn.ServiceFramwork;
import net.csdn.bootstrap.Bootstrap;
import net.csdn.bootstrap.loader.Loader;
import net.csdn.common.settings.Settings;
import net.csdn.enhancer.Enhancer;
import net.csdn.jpa.enhancer.JPAEnhancer;
import net.csdn.modules.scan.ScanService;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BlogInfo: WilliamZhu
 * Date: 12-7-2
 * Time: 上午11:29
 */
public class ModelLoader implements Loader {
    @Override
    public void load(Settings settings) throws IOException {
        final Enhancer enhancer = new JPAEnhancer(ServiceFramwork.injector.getInstance(Settings.class));
        final List<CtClass> classList = new ArrayList<CtClass>();
        ServiceFramwork.scanService.scanArchives(settings.get("application.model"), new ScanService.LoadClassEnhanceCallBack() {
            @Override
            public Class loaded(ClassPool classPool, DataInputStream classFile) {
                try {
                    classList.add(enhancer.enhanceThisClass(classFile));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        for (CtClass ctClass : classList) {
            try {
                ctClass.toClass();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }
}
