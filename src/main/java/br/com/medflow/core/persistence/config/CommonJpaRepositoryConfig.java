package br.com.medflow.core.persistence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.com.medflow.MedflowApplication;
import br.com.medflow.core.persistence.impl.CommonRepositoryImpl;

/**
 * Habilita o {@link CommonRepositoryImpl} como implementacao base dos
 * repositories JPA do projeto.
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
    basePackageClasses = MedflowApplication.class,
    repositoryBaseClass = CommonRepositoryImpl.class)
public class CommonJpaRepositoryConfig {}
