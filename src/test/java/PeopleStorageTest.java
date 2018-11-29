import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Properties;

public class PeopleStorageTest extends CamelSpringTestSupport {

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
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        RouteDefinition replacedRoute = camelContext.getRouteDefinition("createPerson");
        camelContext.getRouteDefinitions().remove(replacedRoute);
        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:createPerson").to("mock:createPerson");
            }
        };
    }

    @Test
    public void testDeleteFileFromDirectory() throws Exception {

        File directory = new File("peopleStorage");
        assertTrue(directory.isDirectory());
        assertEquals(0, directory.listFiles().length);
        createFileInDirectory("peopleStorage/city.xml");
        Thread.sleep(5000);
        assertEquals(0, directory.listFiles().length);

        // And if we create file with other name, file will be exist there
        File createdFile = createFileInDirectory("peopleStorage/other_name.xml");
        assertEquals(1, directory.listFiles().length);
        createdFile.delete();

    }

    @Test
    public void testSplitArrayPersons() throws Exception {

        MockEndpoint mockCreatePerson = getMockEndpoint("mock:createPerson");
        mockCreatePerson.setExpectedMessageCount(2);
        mockCreatePerson.message(0).header("name").isEqualTo("Jack");
        mockCreatePerson.message(0).header("surname").isEqualTo("London");
        mockCreatePerson.message(1).header("name").isEqualTo("Mark");
        mockCreatePerson.message(1).header("surname").isEqualTo("Ling");

        createFileInDirectory("peopleStorage/city.xml");
        Thread.sleep(5000);

        assertMockEndpointsSatisfied();
    }

    private File createFileInDirectory(String xmlFilePath) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            // root element
            Element root = document.createElement("city");
            document.appendChild(root);
            // person element
            Element person = document.createElement("person");
            root.appendChild(person);
            // name element
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode("Jack"));
            person.appendChild(name);
            // surname element
            Element surname = document.createElement("surname");
            surname.appendChild(document.createTextNode("London"));
            person.appendChild(surname);
            // person element
            person = document.createElement("person");
            root.appendChild(person);
            // name element
            name = document.createElement("name");
            name.appendChild(document.createTextNode("Mark"));
            person.appendChild(name);
            // surname element
            surname = document.createElement("surname");
            surname.appendChild(document.createTextNode("Ling"));
            person.appendChild(surname);

            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            File resultFile = new File(xmlFilePath);
            StreamResult streamResult = new StreamResult(resultFile);
            transformer.transform(domSource, streamResult);
            System.out.println("Done creating XML File");
            return resultFile;
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
            return null;
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
            return null;
        }
    }
}
