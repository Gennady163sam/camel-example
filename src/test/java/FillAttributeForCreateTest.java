import com.zic.Person;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.Properties;

@ContextConfiguration
public class FillAttributeForCreateTest extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("/META-INF/spring/camel-context.xml");
    }

    @Override
    protected Properties useOverridePropertiesWithPropertiesComponent() {
        Properties properties = new Properties();
        properties.put("generator.use", "false");
        return properties;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:checkFillAttrs").to("direct:fillAttrs").to("mock:createPerson");
            }
        };
    }

    @Test
    public void testFillAttributesForCreateModel() throws Exception {

        MockEndpoint mockCreatePerson = getMockEndpoint("mock:createPerson");
        mockCreatePerson.setExpectedMessageCount(1);
        mockCreatePerson.message(0).header("name").isEqualTo("Jack");
        mockCreatePerson.message(0).header("surname").isEqualTo("London");

        Person testPerson = new Person();
        testPerson.setName("Jack");
        testPerson.setSurname("London");
        template.sendBody("direct:checkFillAttrs", testPerson);

        assertMockEndpointsSatisfied();
    }
}
