import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GeneratePoisonTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/camel-context.xml");
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        RouteDefinition replacedRoute = camelContext.getRouteDefinition("getPoison");
        camelContext.getRouteDefinitions().remove(replacedRoute);
        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("activemq://poisons").unmarshal("json").to("mock:pushToActiveMq");
            }
        };
    }

    @Test
    public void testGeneratePoisonByModel() throws Exception {
        MockEndpoint mockCreatePerson = getMockEndpoint("mock:pushToActiveMq");
        mockCreatePerson.setExpectedMessageCount(1);
        mockCreatePerson.message(0).body().convertToString().contains("personId");
        mockCreatePerson.message(0).body().convertToString().contains("concentration");

        Thread.sleep(3000);

        assertMockEndpointsSatisfied();
    }
}
