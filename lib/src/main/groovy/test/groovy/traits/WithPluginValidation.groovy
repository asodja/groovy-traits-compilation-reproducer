package test.groovy.traits

import groovy.transform.SelfType

@SelfType(MyTestBase)
trait WithPluginValidation {
    final AllPluginsValidation allPlugins = new AllPluginsValidation(this)

    static class AllPluginsValidation {
        final MyTestBase base
        AllPluginsValidation(MyTestBase base) {
            this.base = base
        }
    }
}