package co.example;

import org.apache.commons.configuration2.ImmutableHierarchicalConfiguration;

import java.util.List;

public class MyInnerObjectConfiguration extends MyBaseConfiguration {

    @ConfigValue(key = "objectTitle")
    String objectTitle;

    @ConfigList(key = "someArray")
    List<MyListItemConfiguration> someArray;

    public MyInnerObjectConfiguration(ImmutableHierarchicalConfiguration configuration) {
        super(configuration);
    }

    @Override
    public String toString() {
        return "MyInnerObjectConfiguration{" +
                "objectTitle='" + objectTitle + '\'' +
                ", someArray=" + someArray +
                '}';
    }
}
