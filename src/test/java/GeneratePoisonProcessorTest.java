import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

public class GeneratePoisonProcessorTest extends CamelSpringTestSupport {

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
                from("direct:checkGenerateProc").process("poisonEnricherProc").to("mock:pushToActiveMq");
            }
        };
    }

    @Test
    public void testInternalGeneratePoisonProcessor() throws Exception {
        MockEndpoint mockCreatePerson = getMockEndpoint("mock:pushToActiveMq");
        mockCreatePerson.setExpectedMessageCount(1);
        mockCreatePerson.message(0).body().convertToString().contains("personId=12");
        mockCreatePerson.message(0).body().convertToString().contains("concentration");

        // Воспроизведём данные, генерируемые компонентом sql
        List<Map> list = new ArrayList<>();
        Map body = new HashMap<String, Long>();
        body.put("personId", 12l);
        list.add(body);
        // Отправим на генерацию объекта для active mq
        template.sendBody("direct:checkGenerateProc", list);

        assertMockEndpointsSatisfied();
    }
}
