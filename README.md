JUnit Grader
============
This is a small collection of utilities to help grading Java assignments using junit.
I am not related to the JUnit team in any way.

Adding as a dependency
----------------------

To add as a dependency in a maven project, add the following to your pom:

```xml
   <repositories>
        <repository>
            <id>artifactory</id>
            <url>http://oss.jfrog.org/libs-snapshot</url>
        </repository>
    </repositories>


    <dependencies>
        <dependency>
            <groupId>org.github.tduberne</groupId>
            <artifactId>junitgrader</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

As I implement additional methods as needed, there are only snapshots for the moment.
I will freeze things in a release only if I make change that break the "API".

How does this work
------------------

Let's say you have the following tests that should be part of your grading:

```java
public class MyTest {
    @Test
    public void testStuff() {
        Assert.fail( "Some should fail." );
    }
}
```

Adding it to the grading scheme requires two lines of code:

```java
public class MyTest {
    @Rule public final GradingRule grader = new GradingRule();

    @Test
    @GradeValue( 3 )
    public void testStuff() {
        Assert.fail( "Some should fail." );
    }
}
```

Running this test will create a file grading.txt at the root of the project,
with the following content:

```
total score on all scored exercises: [0 / 3]
    com.acme.rocketscienceexam.MyTest got score [0 / 3]
        testStuff got score [0 / 3]
```

This will also be printed to the console after each test.
This summary contains grades summarized by:
- test method
- test class (sum of graded methods in the class, should correspond to an exercise)
- all tests run

How to change the output
------------------------

The GradingRule accepts listeners, that do something with an assignment object.
Look at the javadoc for this class for details


