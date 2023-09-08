package co.example;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

public class MyApplicationConfiguration extends MyBaseConfiguration {

    @ConfigValue(key = "project.someStrings.string0")
    String myField0;

    @ConfigValue(key = "project.someStrings.string1")
    String myField1;

    @ConfigObject(key = "project.anObjectWithProperties")
    MyInnerObjectConfiguration myInnerObjectConfiguration;

    public MyApplicationConfiguration(ImmutableHierarchicalConfiguration configuration) {
        super(configuration);
    }


    @Override
    public String toString() {
        return "MyApplicationConfiguration{" +
                "myField0='" + myField0 + '\'' +
                ", myField1='" + myField1 + '\'' +
                ", myStbRacksConfiguration=" + myInnerObjectConfiguration +
                '}';
    }
}
