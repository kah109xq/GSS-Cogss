import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith
import cucumber.api.CucumberOptions

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("src/test/resources/features"),
  glue = Array("src/test/scala/steps"),
  dryRun = true,
  strict = true,
  monochrome = true )
class RunTests extends {
}