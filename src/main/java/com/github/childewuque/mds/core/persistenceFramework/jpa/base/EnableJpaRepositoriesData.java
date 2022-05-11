package com.github.childewuque.mds.core.persistenceFramework.jpa.base;


import org.springframework.asm.Opcodes;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @description:
 * @author: childewuque
 * @date: Created in 2020/6/7 上午11:10
 * @modified By:
 */
//SimpleAnnotationMetadataReadingVisitor
public class EnableJpaRepositoriesData extends AnnotationMetadataReadingVisitor implements AnnotationMetadata {

    private Map<String, ?> data;

    private void initIt() throws NoSuchMethodException, ClassNotFoundException {
        String name =  this.getClass().getName();
        super.visit(0, Opcodes.ACC_PUBLIC, name, null, null, new String[0]);
        //################## protected final Set<String> annotationSet = new LinkedHashSet<>(4);
        annotationSet.add(Configuration.class.getCanonicalName());
        annotationSet.add(EnableTransactionManagement.class.getCanonicalName());
        annotationSet.add(EnableJpaRepositories.class.getCanonicalName());

        //################## protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<>(4);
        metaAnnotationMap.put(Configuration.class.getCanonicalName(),
                new LinkedHashSet<>(Arrays.asList(
                        Component.class.getCanonicalName(),
                        Indexed.class.getCanonicalName()
                )));
        metaAnnotationMap.put(EnableTransactionManagement.class.getCanonicalName(),
                new LinkedHashSet<>(Arrays.asList(
                        Import.class.getCanonicalName()
                )));
        metaAnnotationMap.put(EnableJpaRepositories.class.getCanonicalName(),
                new LinkedHashSet<>(Arrays.asList(
                        Import.class.getCanonicalName()
                )));

        //################## protected final LinkedMultiValueMap<String, AnnotationAttributes> attributesMap = new LinkedMultiValueMap<>(4);
        attributesMap.put(Configuration.class.getCanonicalName(),
                new LinkedList<AnnotationAttributes>() {{
                    add(new AnnotationAttributes(new LinkedHashMap<String, Object>() {{
                        put("value", defaultFor(Configuration.class, "value"));
                    }}));
                }});

        attributesMap.put(Component.class.getCanonicalName(),
                new LinkedList<AnnotationAttributes>() {{
                    add(new AnnotationAttributes(new LinkedHashMap<String, Object>() {{
                        put("value", defaultFor(Component.class, "value"));
                    }}));
                }});
        attributesMap.put(Indexed.class.getCanonicalName(),
                new LinkedList<AnnotationAttributes>() {{
                    add(new AnnotationAttributes(new LinkedHashMap<String, Object>() {{
                    }}));
                }});
        attributesMap.put(EnableTransactionManagement.class.getCanonicalName(),
                new LinkedList<AnnotationAttributes>() {{
                    add(new AnnotationAttributes(new LinkedHashMap<String, Object>() {{
                        put("order", defaultFor(EnableTransactionManagement.class, "order"));
                        put("mode", defaultFor(EnableTransactionManagement.class, "mode"));
                        put("proxyTargetClass", defaultFor(EnableTransactionManagement.class, "proxyTargetClass"));
                    }}));
                }});
        attributesMap.put(Import.class.getCanonicalName(),
                new LinkedList<AnnotationAttributes>() {{
                    add(new AnnotationAttributes(new LinkedHashMap<String, Object>() {{
                        put("value", new Class<?>[]{TransactionManagementConfigurationSelector.class});
                    }}));
                    add(new AnnotationAttributes(new LinkedHashMap<String, Object>() {{
                        put("value", new Class<?>[]{Class.forName("org.springframework.data.jpa.repository.config.JpaRepositoriesRegistrar")});
                    }}));
                }});

        attributesMap.put(EnableJpaRepositories.class.getCanonicalName(),
                new LinkedList<AnnotationAttributes>() {{
                    add(new AnnotationAttributes(new LinkedHashMap<String, Object>() {{
                        put("value", (data.get("value") != null) ? data.get("value") : defaultFor(EnableJpaRepositories.class, "value"));
                        put("basePackages", (data.get("basePackages") != null) ? data.get("basePackages") : defaultFor(EnableJpaRepositories.class, "basePackages"));
                        put("basePackageClasses", (data.get("basePackageClasses") != null) ? data.get("basePackageClasses") : defaultFor(EnableJpaRepositories.class, "basePackageClasses"));
//                        put("includeFilters", (data.get("includeFilters") != null) ? data.get("includeFilters") : defaultFor(EnableJpaRepositories.class, "includeFilters"));
                        put("includeFilters", (data.get("includeFilters") != null) ? data.get("includeFilters") : new AnnotationAttributes[]{});

                        put("excludeFilters", (data.get("excludeFilters") != null) ? data.get("excludeFilters") : new AnnotationAttributes[]{});
                        put("repositoryImplementationPostfix", (data.get("repositoryImplementationPostfix") != null) ? data.get("repositoryImplementationPostfix") : defaultFor(EnableJpaRepositories.class, "repositoryImplementationPostfix"));
                        put("namedQueriesLocation", (data.get("namedQueriesLocation") != null) ? data.get("namedQueriesLocation") : defaultFor(EnableJpaRepositories.class, "namedQueriesLocation"));
                        put("queryLookupStrategy", (data.get("queryLookupStrategy") != null) ? data.get("queryLookupStrategy") : defaultFor(EnableJpaRepositories.class, "queryLookupStrategy"));
                        put("repositoryFactoryBeanClass", (data.get("repositoryFactoryBeanClass") != null) ? data.get("repositoryFactoryBeanClass") : defaultFor(EnableJpaRepositories.class, "repositoryFactoryBeanClass"));
                        put("repositoryBaseClass", (data.get("repositoryBaseClass") != null) ? data.get("repositoryBaseClass") : defaultFor(EnableJpaRepositories.class, "repositoryBaseClass"));
                        put("entityManagerFactoryRef", (data.get("entityManagerFactoryRef") != null) ? data.get("entityManagerFactoryRef") : defaultFor(EnableJpaRepositories.class, "entityManagerFactoryRef"));
                        put("transactionManagerRef", (data.get("transactionManagerRef") != null) ? data.get("transactionManagerRef") : defaultFor(EnableJpaRepositories.class, "transactionManagerRef"));
                        put("considerNestedRepositories", (data.get("considerNestedRepositories") != null) ? data.get("considerNestedRepositories") : defaultFor(EnableJpaRepositories.class, "considerNestedRepositories"));
                        put("enableDefaultTransactions", (data.get("enableDefaultTransactions") != null) ? data.get("enableDefaultTransactions") : defaultFor(EnableJpaRepositories.class, "enableDefaultTransactions"));
                        put("bootstrapMode", (data.get("bootstrapMode") != null) ? data.get("bootstrapMode") : defaultFor(EnableJpaRepositories.class, "bootstrapMode"));
                        put("escapeCharacter", (data.get("escapeCharacter") != null) ? data.get("escapeCharacter") : defaultFor(EnableJpaRepositories.class, "escapeCharacter"));


//                        put("repositoryBaseClass", data.get("repositoryBaseClass"));
//                        put("basePackages", data.get("basePackages"));
//                        put("value", defaultFor(EnableJpaRepositories.class, "value"));
//                        put("excludeFilters", new AnnotationAttributes[]{});
//                        put("includeFilters", new AnnotationAttributes[]{});
//                        put("basePackageClasses", defaultFor(EnableJpaRepositories.class, "basePackageClasses"));
//                        put("bootstrapMode", defaultFor(EnableJpaRepositories.class, "bootstrapMode"));
//                        put("transactionManagerRef", data.get("transactionManagerRef"));
//                        put("considerNestedRepositories", defaultFor(EnableJpaRepositories.class, "considerNestedRepositories"));
//                        put("namedQueriesLocation", defaultFor(EnableJpaRepositories.class, "namedQueriesLocation"));
//                        put("queryLookupStrategy", defaultFor(EnableJpaRepositories.class, "queryLookupStrategy"));
//                        put("entityManagerFactoryRef", data.get("entityManagerFactoryRef"));
//                        put("enableDefaultTransactions", defaultFor(EnableJpaRepositories.class, "enableDefaultTransactions"));
//                        put("repositoryImplementationPostfix", defaultFor(EnableJpaRepositories.class, "repositoryImplementationPostfix"));
//                        put("repositoryFactoryBeanClass", defaultFor(EnableJpaRepositories.class, "repositoryFactoryBeanClass"));


                    }}));
                }});

    }

    public EnableJpaRepositoriesData(@Nullable ClassLoader classLoader, Map<String, ?> data) throws NoSuchMethodException, ClassNotFoundException {
        super(classLoader);
        this.data = data;
        this.initIt();
    }

    private Object defaultFor(Class<?> clazz, String methodName) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName);
        return method.getDefaultValue();
    }
}