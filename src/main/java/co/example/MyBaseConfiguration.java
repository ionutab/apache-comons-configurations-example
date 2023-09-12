package co.example;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class MyBaseConfiguration {

    public MyBaseConfiguration(ImmutableHierarchicalConfiguration configuration) {

        Arrays.stream(getClass().getDeclaredFields()).forEach(field -> {
            ConfigValue configValue = field.getAnnotation(ConfigValue.class);
            if (configValue != null) {
                parseConfigValue(configuration, field, configValue);
            }

            ConfigList configList = field.getAnnotation(ConfigList.class);
            if (configList != null) {
                parseConfigList(configuration, field, configList);
            }

            ConfigObject configObject = field.getAnnotation(ConfigObject.class);
            if (configObject != null) {
                parseConfigObject(configuration, field, configObject);
            }

            ConfigMap configMap = field.getAnnotation(ConfigMap.class);
            if (configMap != null) {
                parseConfigMap(configuration, field, configMap);
            }
        });
    }

    private void parseConfigMap(ImmutableHierarchicalConfiguration configuration, Field field, ConfigMap configMap) {
        System.out.println(field.getName() + " has " + configMap.key());
        ImmutableHierarchicalConfiguration mapConfiguration = configuration.immutableConfigurationAt(configMap.key());
        Spliterator<String>
                spliterator = Spliterators.spliteratorUnknownSize(mapConfiguration.getKeys(), 0);
        // get the root keys for the subconfiguration
        List<String> myRootKeys = StreamSupport.stream(spliterator, false).map(s -> {
            int indexOfDot = s.indexOf(".");
            if (indexOfDot != -1) {
                return s.substring(0, indexOfDot);
            }
            return null;
        }).distinct().filter(Objects::nonNull).collect(Collectors.toList());

        if (myRootKeys.size() > 0) {
            Class listItemType = null;
            Type mapType = field.getGenericType();
            if (mapType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) mapType;
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length == 2) {
                    // getting the type of the map
                    Type listType = typeArguments[1];
                    if (listType instanceof ParameterizedType) {
                        ParameterizedType listParameterizedType = (ParameterizedType) listType;
                        Type[] listTypeArguments = listParameterizedType.getActualTypeArguments();
                        if (listTypeArguments.length == 1) {
                            // getting the type of the list of the map
                            listItemType = (Class) listTypeArguments[0];
                        }
                    }
                }
            }
            if (listItemType != null) {
                try {
                    Constructor constuctor = listItemType.getDeclaredConstructor(ImmutableHierarchicalConfiguration.class);
                    HashMap fieldMap = new HashMap();
                    myRootKeys.forEach(s -> {
                        List listOfItems = new LinkedList();
                        List<ImmutableHierarchicalConfiguration> listItemConfigurations = mapConfiguration.immutableConfigurationsAt(s);
                        listItemConfigurations.forEach(immutableHierarchicalConfiguration -> {
                            try {
                                listOfItems.add(constuctor.newInstance(immutableHierarchicalConfiguration));
                            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }
                        });
                        fieldMap.put(s, listOfItems);
                    });
                    field.set(this, fieldMap);
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseConfigObject(ImmutableHierarchicalConfiguration configuration, Field field, ConfigObject configObject) {
        System.out.println(field.getName() + " has " + configObject.key());
        ImmutableHierarchicalConfiguration objectConfiguration = configuration.immutableConfigurationAt(configObject.key());
        try {
            Constructor constuctor = field.getType().getDeclaredConstructor(ImmutableHierarchicalConfiguration.class);
            field.set(this, constuctor.newInstance(objectConfiguration));
            System.out.println(field.getName() + " setting ");
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void parseConfigList(ImmutableHierarchicalConfiguration configuration, Field field, ConfigList configList) {
        System.out.println(field.getName() + " has " + configList.key());
        ParameterizedType objectListType = (ParameterizedType) field.getGenericType();
        Class<?> objectListClass = (Class<?>) objectListType.getActualTypeArguments()[0];

        List<ImmutableHierarchicalConfiguration> immutableConfigurations = configuration.immutableConfigurationsAt(configList.key());
        List list = new LinkedList();
        immutableConfigurations.forEach(immutableHierarchicalConfiguration -> {
            try {
                Constructor constructor = objectListClass.getDeclaredConstructor(ImmutableHierarchicalConfiguration.class);
                list.add(constructor.newInstance(immutableHierarchicalConfiguration));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
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

    private void parseConfigValue(ImmutableHierarchicalConfiguration configuration, Field field, ConfigValue configValue) {
        System.out.println(field.getName() + " has " + configValue.key());
        String value = configuration.getString(configValue.key());
        System.out.println(field.getName() + " setting " + value);
        try {
            field.set(this, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
