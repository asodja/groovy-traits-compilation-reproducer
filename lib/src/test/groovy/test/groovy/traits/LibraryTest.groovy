/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
package test.groovy.traits

import spock.lang.Specification

class LibraryTest extends Specification {
    def "someLibraryMethod returns true"() {
        setup:
        def lib = new MyTest()

        when:
        def result = lib.someLibraryMethod()

        then:
        result == true
    }
}
