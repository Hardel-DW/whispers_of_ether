[Incubating] Problems report is available at: file:///C:/Users/Hardel/Desktop/repository/whispers_of_ether/build/reports/problems/problems-report.html

Deprecated Gradle features were used in this build, making it incompatible with Gradle 9.0.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/8.14.1/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.
4 actionable tasks: 1 executed, 3 up-to-date
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:14: error: cannot find symbol
import net.minecraft.client.renderer.ShaderDefines;
                                    ^
  symbol:   class ShaderDefines
  location: package net.minecraft.client.renderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:15: error: cannot find symbol
import net.minecraft.client.renderer.ShaderProgram;
                                    ^
  symbol:   class ShaderProgram
  location: package net.minecraft.client.renderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:23: error: cannot find symbol
    private static final ShaderProgram SINGULARITY_SHADER = new ShaderProgram(
                         ^
  symbol:   class ShaderProgram
  location: class SingularityRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:23: error: cannot find symbol
    private static final ShaderProgram SINGULARITY_SHADER = new ShaderProgram(
                                                                ^
  symbol:   class ShaderProgram
  location: class SingularityRenderer
C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:26: error: cannot find symbol
            ShaderDefines.EMPTY);
            ^
  symbol:   variable ShaderDefines
  location: class SingularityRenderer
5 errors

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':compileClientJava'.
> Compilation failed; see the compiler output below.
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:14: error: cannot find symbol
  import net.minecraft.client.renderer.ShaderDefines;
                                      ^
    symbol:   class ShaderDefines
    location: package net.minecraft.client.renderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:15: error: cannot find symbol
  import net.minecraft.client.renderer.ShaderProgram;
                                      ^
    symbol:   class ShaderProgram
    location: package net.minecraft.client.renderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:23: error: cannot find symbol
      private static final ShaderProgram SINGULARITY_SHADER = new ShaderProgram(
                           ^
    symbol:   class ShaderProgram
    location: class SingularityRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:23: error: cannot find symbol
      private static final ShaderProgram SINGULARITY_SHADER = new ShaderProgram(
                                                                  ^
    symbol:   class ShaderProgram
    location: class SingularityRenderer
  C:\Users\Hardel\Desktop\repository\whispers_of_ether\src\client\java\fr\hardel\whispers_of_ether\client\render\pipeline\SingularityRenderer.java:26: error: cannot find symbol
              ShaderDefines.EMPTY);
              ^
    symbol:   variable ShaderDefines
    location: class SingularityRenderer
  5 errors

* Try:
> Check your code and dependencies to fix the compilation error(s)
> Run with --scan to get full insights.

BUILD FAILED in 1s