# Incremental compilation with traits reproducer

This reproduces issue with Groovy traits and Gradle incremental compilation.

Steps to reproduce:
1. git clone git@github.com:asodja/groovy-traits-compilation-reproducer.git
2. run ./gradlew :lib:compileGroovy
3. Change `println("a")` in MyTest.groovy to `println("b")`
4. run ./gradlew :lib:compileGroovy again
5. See error

To resolve the issue run `./gradlew :lib:compileGroovy --rerun-tasks`

Exception thrown:
```
java.lang.RuntimeException: java.lang.NoClassDefFoundError: Unable to load class test.groovy.traits.WithPluginValidation$AllPluginsValidation due to missing dependency Ltest/groovy/traits/MyTestBase;
        at org.codehaus.groovy.control.CompilationUnit$IPrimaryClassNodeOperation.doPhaseOperation(CompilationUnit.java:977)
        at org.codehaus.groovy.control.CompilationUnit.processPhaseOperations(CompilationUnit.java:672)
        at org.codehaus.groovy.control.CompilationUnit.compile(CompilationUnit.java:636)
        at org.codehaus.groovy.control.CompilationUnit.compile(CompilationUnit.java:611)
        at org.gradle.api.internal.tasks.compile.ApiGroovyCompiler.execute(ApiGroovyCompiler.java:271)
        ...
Caused by: java.lang.NoClassDefFoundError: Unable to load class test.groovy.traits.WithPluginValidation$AllPluginsValidation due to missing dependency Ltest/groovy/traits/MyTestBase;
        at org.codehaus.groovy.vmplugin.v8.Java8.configureClassNode(Java8.java:447)
        at org.codehaus.groovy.ast.ClassNode.lazyClassInit(ClassNode.java:273)
        at org.codehaus.groovy.ast.ClassNode.getUnresolvedSuperClass(ClassNode.java:1014)
        at org.codehaus.groovy.ast.ClassNode.getUnresolvedSuperClass(ClassNode.java:1006)
        at org.codehaus.groovy.ast.ClassNode.getSuperClass(ClassNode.java:1000)
        at org.codehaus.groovy.ast.ClassNode.isDerivedFrom(ClassNode.java:911)
        at org.codehaus.groovy.classgen.asm.InvocationWriter.castToNonPrimitiveIfNecessary(InvocationWriter.java:1144)
        at org.codehaus.groovy.classgen.asm.OperandStack.doConvertAndCast(OperandStack.java:363)
        at org.codehaus.groovy.classgen.asm.OperandStack.doGroovyCast(OperandStack.java:300)
        at org.codehaus.groovy.classgen.AsmClassGenerator.storeThisInstanceField(AsmClassGenerator.java:1259)
        at org.codehaus.groovy.classgen.AsmClassGenerator.visitFieldExpression(AsmClassGenerator.java:1196)
        at org.codehaus.groovy.ast.expr.FieldExpression.visit(FieldExpression.java:41)
        at org.codehaus.groovy.classgen.AsmClassGenerator.visitPropertyExpression(AsmClassGenerator.java:1124)
        at org.codehaus.groovy.ast.expr.PropertyExpression.visit(PropertyExpression.java:63)
        at org.codehaus.groovy.classgen.AsmClassGenerator.visitVariableExpression(AsmClassGenerator.java:1339)
        at org.codehaus.groovy.ast.expr.VariableExpression.visit(VariableExpression.java:71)
        ...
        at org.codehaus.groovy.ast.ClassCodeVisitorSupport.visitClass(ClassCodeVisitorSupport.java:52)
        at org.codehaus.groovy.classgen.AsmClassGenerator.visitClass(AsmClassGenerator.java:273)
        at org.codehaus.groovy.control.CompilationUnit$3.call(CompilationUnit.java:798)
        at org.codehaus.groovy.control.CompilationUnit$IPrimaryClassNodeOperation.doPhaseOperation(CompilationUnit.java:943)
        ... 34 more
```

Classes hierarchy:
```
class MyTest extends MyTestBase implements WithPluginValidation { ... }

class MyTestBase { ... } has a dependency to MyTest inside a method

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
```