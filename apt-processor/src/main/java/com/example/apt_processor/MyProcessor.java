package com.example.apt_processor;


import com.example.apt_annotaion.Print;
import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        //java输出
        System.out.println("Hello ,APT");
        //注释处理器提供的api接口
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Hello ,APT");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //拿到所有添加Print注解的成员变量
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Print.class);
        try {
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile("PrintUtil");
            Writer writer = fileObject.openWriter();
            //包路径
            writer.write("package com.example.myaptdemo;\n");
            writer.write("\n");
            writer.write("public class PrintUtil { \n");
            //因为可能会有多个方法。所以这里要用循环
            for (Element element : elements) {
                Name simpleName = element.getSimpleName();
                writer.write("   //输出" + simpleName + "\n");
                writer.write("   public static void print$$" + simpleName + "(){ \n");
                writer.write("  System.out.println(\"Hello" + simpleName + "\");\n   }\n\n");
            }
            writer.write("}");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 要扫描扫描的注解，可以添加多个
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(Print.class.getCanonicalName());
        return hashSet;
    }

    /**
     * 编译版本，固定写法就可以
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

}
