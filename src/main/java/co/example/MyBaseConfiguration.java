package co.example;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class MyBaseConfiguration {

    public MyBaseConfiguration(ImmutableHierarchicalConfiguration configuration) {

        Arrays.stream(getClass().getDeclaredFields()).forEach(field -> {
            ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            if (configValue != null) {
                System.out.println(field.getName() + " has " + configValue.key());
                String value = configuration.getString(configValue.key());
                System.out.println(field.getName() + " setting " + value);
                try {
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            ConfigList configList = field.getAnnotation(ConfigList.class);
            if (configList != null) {
                System.out.println(field.getName() + " has " + configList.key());
                ParameterizedType objectListType = (ParameterizedType) field.getGenericType();
                Class<?> objectListClass = (Class<?>) objectListType.getActualTypeArguments()[0];

                List<ImmutableHierarchicalConfiguration> immutableConfigurations = configuration.immutableConfigurationsAt(configList.key());
                List list = new LinkedList();
                immutableConfigurations.forEach(immutableHierarchicalConfiguration -> {
                    try {
                        Constructor constructor = objectListClass.getDeclaredConstructor(ImmutableHierarchicalConfiguration.class);
                        list.add(constructor.newInstance(immutableHierarchicalConfiguration));
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
                try {
                    field.set(this, list);
                    System.out.println(field.getName() + " setting " + list);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            ConfigObject configObject = field.getAnnotation(ConfigObject.class);
            if (configObject != null) {
                System.out.println(field.getName() + " has " + configObject.key());
                ImmutableHierarchicalConfiguration objectConfiguration = configuration.immutableConfigurationAt(configObject.key());
                try {
                    Constructor constuctor = field.getType().getDeclaredConstructor(ImmutableHierarchicalConfiguration.class);
                    field.set(this, constuctor.newInstance(objectConfiguration));
                    System.out.println(field.getName() + " setting ");

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
