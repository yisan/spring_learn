package com.bingo.enable;

import com.bingo.other.domain.Role;
import com.bingo.other.domain.User;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by ing on 2021/12/15 17:10
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AbstractBeanDefinition userDefinition = BeanDefinitionBuilder.rootBeanDefinition(User.class).getBeanDefinition();
        registry.registerBeanDefinition("user",userDefinition);
        AbstractBeanDefinition roleDefinition = BeanDefinitionBuilder.rootBeanDefinition(Role.class).getBeanDefinition();
        registry.registerBeanDefinition("Role",roleDefinition);
    }
}
