package com.github.daweizhou89.encryptconstant;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class EncryptConstantCompiler extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(EncryptConstant.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(EncryptConstant.class);
        for (Element element : elements) {
            processTypeElement(roundEnv, element);
        }
        return true;
    }

    private void processTypeElement(RoundEnvironment roundEnv, Element element) {
        // Just Process TypeElement
        if (!(element instanceof TypeElement)) {
            return;
        }
        TypeElement typeElement = (TypeElement) element;
        final String encryptKey = typeElement.getAnnotation(EncryptConstant.class).key();
        // Get All Members
        List<? extends Element> members = elementUtils.getAllMembers(typeElement);
        List<FieldSpec> fieldSpecList = new ArrayList<>();
        for (Element member : members) {
            final boolean isVariableElement = member instanceof VariableElement;
            if (!isVariableElement) {
                continue;
            }
            final boolean isWithFinal = member.getModifiers().contains(Modifier.FINAL);
            final boolean isWithStatic = member.getModifiers().contains(Modifier.STATIC);
            if (!isWithFinal || !isWithStatic) {
                continue;
            }
            final VariableElement variableElement = (VariableElement) member;
            final Object constantValue = variableElement.getConstantValue();
            final boolean isStringValue = constantValue != null && constantValue instanceof String;
            if (!isStringValue) {
                continue;
            }
            final String stringValue = (String) constantValue;
            final String encryptStringValue = EncryptUtil.encodeString(stringValue, encryptKey);
            FieldSpec fieldSpec = FieldSpec.builder(String.class, variableElement.getSimpleName().toString(), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("$T.decodeString($S, $S)", ClassName.get("com.github.daweizhou89.encryptconstant", "DecryptUtil"), encryptStringValue, encryptKey)
                    .build();
            fieldSpecList.add(fieldSpec);
        }

        // Create New Type
        String typeName = "Encrypt" + element.getSimpleName();
        TypeSpec typeSpec = TypeSpec.classBuilder(typeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(fieldSpecList)
                .build();

        // Write New File
        final String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
