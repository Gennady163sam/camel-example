package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service
public class ExceptionAnalyzerProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Exception - " + exchange.getProperty("CamelExceptionCaught"));
        exchange.getOut().setBody(exchange.getIn().getBody());
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
    }
}
