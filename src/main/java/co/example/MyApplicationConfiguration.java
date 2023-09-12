package co.example;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.List;
import java.util.Map;

public class MyApplicationConfiguration extends MyBaseConfiguration {

    @ConfigValue(key = "project.someStrings.string0")
    String myField0;

    @ConfigValue(key = "project.someStrings.string1")
    String myField1;

    @ConfigObject(key = "project.anObjectWithProperties")
    MyInnerObjectConfiguration myInnerObjectConfiguration;

    @ConfigMap(key = "project.aMapWithProperties")
    Map<String, List<MyListItemConfiguration>> aMapWithProperties;

    public MyApplicationConfiguration(ImmutableHierarchicalConfiguration configuration) {
        super(configuration);
    }


    @Override
    public String toString() {
        return "MyApplicationConfiguration{" +
                "myField0='" + myField0 + '\'' +
                ", myField1='" + myField1 + '\'' +
                ", myStbRacksConfiguration=" + myInnerObjectConfiguration +
                ", aMapWithProperties=" + aMapWithProperties +
                '}';
    }
}
