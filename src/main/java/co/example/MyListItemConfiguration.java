package co.example;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

public class MyListItemConfiguration extends MyBaseConfiguration {

    @ConfigValue(key = "name")
    String name;

    @ConfigValue(key = "color")
    String color;

    public MyListItemConfiguration(ImmutableHierarchicalConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String toString() {
        return "MyStbConfiguration{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

}
